
package com.deardhruv.projectstarter.activities;

import java.util.List;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.Toast;

import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.abstracts.AbstractActivity;
import com.deardhruv.projectstarter.adapters.PictureViewerAdapter;
import com.deardhruv.projectstarter.ui.views.CustomViewPager;

public class PictureViewerActivity extends AbstractActivity {

	private static final String LOGTAG = PictureViewerActivity.class.getSimpleName();
	// private static final Logger LOG = new Logger(LOGTAG);

	public static final String EXTRA_IMAGE_URLS = LOGTAG + ".IMAGES";
	public static final String EXTRA_IMAGE_SELECTION = LOGTAG + ".SELECTION";

	private PictureViewerAdapter mPagerAdapter;
	private CustomViewPager mViewImagesPager;
	private int mSelection;
	private List<String> mImageUrls;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pictureviewer);

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

	@Override
	protected void onResume() {
		super.onResume();

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

}
