package com.deardhruv.projectstarter.activities;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.greenrobot.event.EventBus;
import retrofit.mime.TypedFile;

public class UploadFileActivity extends AbstractActivity implements OnClickListener {

    private static final String LOGTAG = "UploadFileActivity";
    private static final Logger LOG = new Logger(LOGTAG);

    private static final int PHOTO_PICKER_CODE = 2001;
    private static final String UPLOAD_FILE_REQUEST_TAG = LOGTAG + ".uploadFileRequest";
    AutoCompleteTextView.Validator emptyValidator = new AutoCompleteTextView.Validator() {
        @Override
        public boolean isValid(CharSequence text) {
            return !TextUtils.isEmpty(text);
        }

        @Override
        public CharSequence fixText(CharSequence invalidText) {
            return null;
        }
    };
    private StoreImageHelper mStoreImageHelper;
    private File mTmpPictureFile;
    private DisplayImageOptions mImageOptions;
    private ImageLoader mImageLoader;
    private EventBus mEventBus;
    private ApiClient mApiClient;
    private ProgressDialog pd;
    private Button btnUploadFile;
    private ImageView imgAddPhoto;
    private com.rey.material.widget.EditText mTxtFirstName;
    private com.rey.material.widget.EditText mTxtLastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
        setProgressBarIndeterminateVisibility(true);
        setContentView(R.layout.upload_file_activity_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initSnackBar(UploadFileActivity.this);
        initUI();

        mEventBus = EventBus.getDefault();
        ProjectStarterApplication app = ((ProjectStarterApplication) getApplication());
        mApiClient = app.getApiClient();

        mImageLoader = ImageLoader.getInstance();
        mImageLoader.clearMemoryCache();

        mImageOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(android.R.color.transparent)
                .showImageForEmptyUri(R.drawable.ic_launcher)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        mStoreImageHelper = new StoreImageHelper(this);
    }

    private void initUI() {
        btnUploadFile = (Button) findViewById(R.id.btnUploadFile);
        imgAddPhoto = (ImageView) findViewById(R.id.imgAddPhoto);

        mTxtFirstName = (com.rey.material.widget.EditText) findViewById(R.id.txt_first_name);
        mTxtLastName = (com.rey.material.widget.EditText) findViewById(R.id.txt_last_name);

        initListener();
    }

    private void initListener() {
        btnUploadFile.setOnClickListener(this);
        imgAddPhoto.setOnClickListener(this);

        mTxtFirstName.setValidator(emptyValidator);
        mTxtLastName.setValidator(emptyValidator);
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
                validateAndUpload(mTmpPictureFile, mTxtFirstName.getText().toString(), mTxtLastName.getText().toString());
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
//                bundle.putParcelable(MediaStore.EXTRA_OUTPUT, saveUri);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, saveUri);
            } else {
//                bundle.putBoolean("return-data", true);
                takePictureIntent.putExtra("return-data", true);
            }
//            takePictureIntent.putExtras(bundle);

        } catch (IOException e) {
            Log.e(LOGTAG, e.getMessage());
        }

        Intent[] additionalIntents = new Intent[]{
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
            validateAndAddtoGallery(imagePath);
        }
    }

    /**
     * Check if the image exists and is valid. Add it to the gallery adapter.
     *
     * @param imagePath
     */
    private void validateAndAddtoGallery(String imagePath) {
        if (imagePath != null && imagePath.length() > 0) {
            if (imagePath.startsWith("file://")) {
                imagePath.replace("file://", "");
            }

            mTmpPictureFile = new File(imagePath);

            if (mTmpPictureFile.exists()) {
                if (!ImageValidator.isPictureValidForUpload(mTmpPictureFile.getAbsolutePath())) {
                    showMsg("Creating image failed.");
                } else {
                    addImageToView(mTmpPictureFile);
                }
            } else {
                Log.e(LOGTAG, "Photo picker: File does not exist!");
                showMsg("Image is not supported.");
            }
        } else {
            showMsg("Creating image failed.");
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
                showMsg("Creating image failed.");
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
            //noinspection ResultOfMethodCallIgnored
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
        String[] filePaths = new String[]{
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

    private void validateAndUpload(final File file, String firstName, String lastName) {

        if (TextUtils.isEmpty(firstName)) {
            mTxtFirstName.setError(getString(R.string.first_name_mandatory));
            mTxtLastName.setError("");
        } else if (TextUtils.isEmpty(lastName)) {
            mTxtLastName.setError(getString(R.string.last_name_mandatory));
            mTxtFirstName.setError("");
        } else if (file == null || !isPictureValid(file.getAbsolutePath())) {
            mTxtFirstName.setError("");
            mTxtLastName.setError("");
            showMsg(getString(R.string.invalid_file));
        } else {
//            startUploading(mTmpPictureFile);
            startUploading(file, firstName, lastName);
        }
    }

    private void startUploading(final File file, String firstName, String lastName) {
        if (file.exists()) {
            showProgressDialog();
            String mimeType = "image/jpeg"
                    // "application/octet-stream"
                    // "multipart/form-data"
                    // "image/*"
                    // "multipart/mixed"
                    ;

            TypedFile typedFile = new TypedFile(mimeType, file);
            mApiClient.uploadFile(UPLOAD_FILE_REQUEST_TAG, typedFile, firstName, lastName);

        } else {
            showMsg(getString(R.string.corrupted_file));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                // showMsg(Dumper.dump(uploadFileResponse));
                // showMsg(uploadFileResponse.getApiInfo().toString());
                showMsg(uploadFileResponse.getApiInfo().getMessage());

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
                showMsg(getString(R.string.error_server_problem));
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
                showMsg(event.getResultMsgUser());
                // LOG.e(Dumper.dump(event));
                LOG.e(Dumper.dump(event.getResultMsgUser()));
                break;

            default:
                break;
        }
    }

}
