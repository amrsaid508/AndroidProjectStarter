
package com.deardhruv.projectstarter.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.deardhruv.projectstarter.ProjectStarterApplication;
import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.abstracts.AbstractActivity;
import com.deardhruv.projectstarter.events.ApiErrorEvent;
import com.deardhruv.projectstarter.events.ApiErrorWithMessageEvent;
import com.deardhruv.projectstarter.network.ApiClient;
import com.deardhruv.projectstarter.response.model.UploadFileResponse;
import com.deardhruv.projectstarter.utils.Dumper;
import com.deardhruv.projectstarter.utils.ImageValidator;
import com.deardhruv.projectstarter.utils.Logger;
import com.deardhruv.projectstarter.utils.StoreImageHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import de.greenrobot.event.EventBus;
import retrofit.mime.TypedFile;

public class UploadFileActivity extends AbstractActivity implements OnClickListener {

	private static final String LOGTAG = "UploadFileActivity";
	private static final Logger LOG = new Logger(LOGTAG);

	private static final int PHOTO_PICKER_CODE = 2001;
	private static final String UPLOAD_FILE_REQUEST_TAG = LOGTAG + ".uploadFileRequest";

	private StoreImageHelper mStoreImageHelper;
	private File mTmpPictureFile;

	private DisplayImageOptions mImageOptions;
	private ImageLoader mImageLoader;

	private EventBus mEventBus;
	private ApiClient mApiClient;

	private Button btnUploadFile;
	ImageView imgAddPhoto;
	private ProgressDialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setProgressBarIndeterminateVisibility(true);

		setContentView(R.layout.upload_file_activity_layout);

		initUI();

		mEventBus = EventBus.getDefault();
		ProjectStarterApplication app = ((ProjectStarterApplication) getApplication());
		mApiClient = app.getApiClient();

		mImageLoader = ImageLoader.getInstance();
		mImageLoader.clearMemoryCache();

		mImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(android.R.color.transparent)
				.showImageForEmptyUri(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisk(true)
				.considerExifParams(true).build();
		mStoreImageHelper = new StoreImageHelper(this);
	}

	private void initUI() {
		btnUploadFile = (Button) findViewById(R.id.btnUploadFile);
		imgAddPhoto = (ImageView) findViewById(R.id.imgAddPhoto);
		initListener();
	}

	private void initListener() {
		btnUploadFile.setOnClickListener(this);
		imgAddPhoto.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		return id == R.id.action_settings || super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mEventBus.register(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		mEventBus.unregister(this);
	}

	private void showProgressDialog() {
		btnUploadFile.setEnabled(false);
		pd = ProgressDialog.show(this, "Please wait", "uploading file...");
		if (!pd.isShowing()) {
			pd.show();
		}
		setProgressBarIndeterminateVisibility(true);
	}

	private void dismissProgressDialog() {
		btnUploadFile.setEnabled(true);
		if (pd.isShowing()) {
			pd.dismiss();
		}
		setProgressBarIndeterminateVisibility(false);
	}

	// ============================================================================================
	// User Clicks and Actions
	// ============================================================================================

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

			case R.id.imgAddPhoto:
				showAddPhotoDialog();
				break;

			case R.id.btnUploadFile:
				startUploading(mTmpPictureFile);
				break;

			default:
				LOG.i("default case");
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case PHOTO_PICKER_CODE:
				if (data == null) {
					// The camera picture does not come with the data. It is set
					// via Extra
					// android.provider.MediaStore.EXTRA_OUTPUT when the camera
					// intent is started.
					handleTakePictureResult(resultCode);
				} else {
					handlePhotoPickerResult(resultCode, data);
				}
				break;
			default:
				break;
		}

	}

	@TargetApi(Build.VERSION_CODES.ECLAIR)
	private void showAddPhotoDialog() {

		Intent getContentIntent = new Intent();
		getContentIntent.setAction(Intent.ACTION_GET_CONTENT);
		getContentIntent.setType("image/*");

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		try {
			mTmpPictureFile = null;
			mTmpPictureFile = mStoreImageHelper.createImageFile();
			Uri saveUri = Uri.fromFile(mTmpPictureFile);
			// takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
			// Uri.fromFile(mTmpPictureFile));

			Bundle bundle = new Bundle();

			if (saveUri != null) {
				bundle.putParcelable(MediaStore.EXTRA_OUTPUT, saveUri);
			} else {
				bundle.putBoolean("return-data", true);
			}

			takePictureIntent.putExtras(bundle);

		} catch (IOException e) {
			Log.e(LOGTAG, e.getMessage());
		}

		Intent[] additionalIntents = new Intent[] {
				takePictureIntent
		};

		Intent chooserIntent = Intent.createChooser(getContentIntent, "Pick your choice");
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, additionalIntents);
		startActivityForResult(chooserIntent, PHOTO_PICKER_CODE);
	}

	/**
	 * Handles the result which is returned when the user picked one or more
	 * photos from the multi picture chooser or another source.
	 * 
	 * @param resultCode
	 * @param data
	 */
	private void handlePhotoPickerResult(int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && data != null) {
			// The image comes from another source.
			String imagePath = ImageValidator.getPath(this, data.getData());
			validateAndAddtoGallery(imagePath, data.getData());
		}
	}

	/**
	 * Check if the image exists and is valid. Add it to the gallery adapter.
	 * 
	 * @param imagePath
	 * @param uri
	 */
	private void validateAndAddtoGallery(String imagePath, Uri uri) {
		if (imagePath != null && imagePath.length() > 0) {
			if (imagePath.startsWith("file://")) {
				imagePath.replace("file://", "");
			}

			mTmpPictureFile = new File(imagePath);

			if (mTmpPictureFile.exists()) {
				if (!ImageValidator.isPictureValidForUpload(mTmpPictureFile.getAbsolutePath())) {
					showToast("Creating image failed.");
				} else {
					addImageToView(mTmpPictureFile);
				}
			} else {
				Log.e(LOGTAG, "Photo picker: File does not exist!");
				showToast("Image is not supported.");
			}
		} else {
			showToast("Creating image failed.");
		}
	}

	/**
	 * Handles the result which is returned when the user took a picture with
	 * the camera.
	 * 
	 * @param resultCode
	 */
	private void handleTakePictureResult(int resultCode) {
		if (resultCode == Activity.RESULT_OK) {
			if (mTmpPictureFile == null) {
				showToast("Creating image failed.");
			} else {
				saveImageAndUpdateGallery();
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	private void saveImageAndUpdateGallery() {
		File dir = new File(Environment.getExternalStorageDirectory() + "/"
				+ getApplication().getApplicationInfo().getClass().getSimpleName() + "/");

		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file_save = new File(dir.getAbsoluteFile(), System.currentTimeMillis() + ".jpg");

		try {
			InputStream in = new FileInputStream(mTmpPictureFile);
			OutputStream out = new FileOutputStream(file_save);
			// Copy the bits from instream to outstream
			byte[] buf = new byte[1024];
			int len;

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			Log.e(LOGTAG, "FileNotFoundException while saving image. Message: " + e.getMessage());
		} catch (IOException e) {
			Log.e(LOGTAG, "IOException while saving image. Message: " + e.getMessage());
		}

		// Tell the MediaScanner to scan the newly created image to make it
		// available to the user.
		// http://developer.android.com/reference/android/os/Environment.html#getExternalStoragePublicDirectory%28java.lang.String%29
		String[] filePaths = new String[] {
				file_save.toString()
		};
		MediaScannerConnection.scanFile(this, filePaths, null, null);

		addImageToView(mTmpPictureFile);
	}

	private void addImageToView(final File image) {
		if (image == null) {
			throw new IllegalArgumentException("image cannot be null");
		}

		mImageLoader.displayImage(mStoreImageHelper.getImagePath(image), imgAddPhoto,
				mImageOptions);

	}

	private void startUploading(final File file) {

		if (file == null || !isPictureValid(file.getAbsolutePath())) {
			showToast("File is not valid, try again.");
		} else if (file.exists()) {
			showProgressDialog();
			String mimeType = "image/jpeg"
			// "application/octet-stream"
			// "multipart/form-data"
			// "image/*"
			// "multipart/mixed"
			;

			TypedFile typedFile = new TypedFile(mimeType, file);
			mApiClient.uploadFile(UPLOAD_FILE_REQUEST_TAG, typedFile, "yes");

		} else {
			showToast("File is corrupted.");
		}

	}

	// ============================================================================================
	// Validation
	// ============================================================================================

	private boolean isPictureValid(String filePath) {
		return ImageValidator.isPictureValidForUpload(filePath);
	}

	// ============================================================================================
	// EventBus callbacks
	// ============================================================================================

	/**
	 * Response of Image list.
	 * 
	 * @param uploadFileResponse UploadFileResponse
	 */
	public void onEventMainThread(UploadFileResponse uploadFileResponse) {
		switch (uploadFileResponse.getRequestTag()) {
			case UPLOAD_FILE_REQUEST_TAG:
				dismissProgressDialog();
				// showToast(Dumper.dump(uploadFileResponse));
				// showToast(uploadFileResponse.getApiInfo().toString());
				showToast(uploadFileResponse.getApiInfo().getMessage());

				break;

			default:
				break;
		}
	}

	/**
	 * EventBus listener. An API call failed. No error message was returned.
	 *
	 * @param event ApiErrorEvent
	 */
	public void onEventMainThread(ApiErrorEvent event) {
		switch (event.getRequestTag()) {
			case UPLOAD_FILE_REQUEST_TAG:
				dismissProgressDialog();
				showToast(getString(R.string.error_server_problem));
				// LOG.e(Dumper.dump(event));
				LOG.e(event.getRetrofitError().toString());
				break;

			default:
				break;
		}
	}

	/**
	 * EventBus listener. An API call failed. An error message was returned.
	 *
	 * @param event ApiErrorWithMessageEvent Contains the error message.
	 */
	public void onEventMainThread(ApiErrorWithMessageEvent event) {
		switch (event.getRequestTag()) {
			case UPLOAD_FILE_REQUEST_TAG:
				dismissProgressDialog();
				showToast(event.getResultMsgUser());
				// LOG.e(Dumper.dump(event));
				LOG.e(Dumper.dump(event.getResultMsgUser()));
				break;

			default:
				break;
		}
	}

}
