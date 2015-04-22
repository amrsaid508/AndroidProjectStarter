
package com.deardhruv.projectstarter.activities;

import java.io.File;
import java.util.List;

import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.abstracts.AbstractActivity;
import com.deardhruv.projectstarter.adapters.PictureViewerAdapter;
import com.deardhruv.projectstarter.events.model.FileDownloadComleteEvent;
import com.deardhruv.projectstarter.ui.views.CustomViewPager;
import com.deardhruv.projectstarter.utils.FileDownloader;
import com.deardhruv.projectstarter.utils.Helper;

import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

public class PictureViewerActivity extends AbstractActivity {

	private static final String LOGTAG = PictureViewerActivity.class.getSimpleName();
	// private static final Logger LOG = new Logger(LOGTAG);

	public static final String EXTRA_IMAGE_URLS = LOGTAG + ".IMAGES";
	public static final String EXTRA_IMAGE_SELECTION = LOGTAG + ".SELECTION";

	private EventBus mEventBus;

	private PictureViewerAdapter mPagerAdapter;
	private CustomViewPager mViewImagesPager;
	private int mSelection;
	private List<String> mImageUrls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pictureviewer);
		mEventBus = EventBus.getDefault();

		if (getIntent() == null || !getIntent().hasExtra(EXTRA_IMAGE_URLS)
				|| !getIntent().hasExtra(EXTRA_IMAGE_SELECTION)) {
			throw new IllegalArgumentException(
					"Intent should contain image urls and image selection!");
		}

		mImageUrls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);
		mSelection = getIntent().getIntExtra(EXTRA_IMAGE_SELECTION, 0);

		if (mImageUrls.size() < 1) {
			Toast.makeText(this, R.string.pictureviewer_error_no_pictures, Toast.LENGTH_LONG)
					.show();
		}

		initUi();
	}

	private void initUi() {
		mViewImagesPager = (CustomViewPager) findViewById(R.id.activity_pictureviewer_viewpager);

		mPagerAdapter = new PictureViewerAdapter(this, mImageUrls);
		mViewImagesPager.setAdapter(mPagerAdapter);
		mViewImagesPager.setCurrentItem(mSelection);
		mViewImagesPager.setOffscreenPageLimit(mImageUrls.size());

		mViewImagesPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// nothing.
			}

			public void onPageScrollStateChanged(int arg0) {
				// nothing.
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// nothing.
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.share_menu, menu);
		return true;
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.menu_item_share:

				FileDownloader fileDownloader = new FileDownloader(PictureViewerActivity.this);

				if (Helper.isHoneyCombOrHigher()) {
					fileDownloader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
							mImageUrls.get(mViewImagesPager.getCurrentItem()));
				} else {
					fileDownloader.execute(mImageUrls.get(mViewImagesPager.getCurrentItem()));
				}

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * EventBus listener. A file is downloaded from this activity/fragment
	 *
	 * @param event FileDownloadComleteEvent Contains the file path
	 */
	public void onEventMainThread(FileDownloadComleteEvent event) {
		// MediaStore.Images.Media.insertImage(ContentResolver cr, Bitmap
		// source, String title, String description);
		// MediaScannerConnection.scanFile(Context context, String[] path, null,
		// null);
		String[] filePaths = new String[] {
				new File(event.getFilePath()).toString()
		};
		MediaScannerConnection.scanFile(PictureViewerActivity.this, filePaths, null, null);

		// Uri screenshotUri = Uri.parse(event.getFilePath());
		shareImage(event.getFilePath());
	}

	void shareImage(String path) {

		Intent shareIntent = new Intent();
		// shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
		shareIntent.setAction(Intent.ACTION_SEND);
		// shareIntent.setType("image/jpeg");
		shareIntent.setType("image/*");
		shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path));

		startActivity(Intent.createChooser(shareIntent, "Share images to.."));
	}

}
