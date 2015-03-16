
package com.deardhruv.projectstarter.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.utils.AnimateImageListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase.DisplayType;

/**
 * Adapter for the image pager
 */
public class PictureViewerAdapter extends PagerAdapter {
	// private static final String LOGTAG =
	// PictureViewerAdapter.class.getSimpleName();
	// private static final Logger LOG = new Logger(LOGTAG);

	private final AnimateImageListener mAnimateImageListener = new AnimateImageListener();
	private final DisplayImageOptions mImageOptions;
	private final List<String> mImageUrls;
	private final Context mContext;
	private final ImageLoader mImageLoader;

	/**
	 * The constructor to use, setting everything up
	 * 
	 * @param context
	 * @param fileList
	 */
	public PictureViewerAdapter(final Context context, final List<String> fileList) {
		if (context == null) {
			throw new IllegalArgumentException("context shouldn't be null!");
		}

		if (fileList == null) {
			throw new IllegalArgumentException("fileList shouldn't be null!");
		}

		mImageUrls = fileList;
		mContext = context;
		mImageLoader = ImageLoader.getInstance();

		mImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(android.R.color.transparent)
				.showImageForEmptyUri(R.drawable.ic_image_err).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).build();
	}

	@Override
	public int getCount() {
		return mImageUrls.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object obj) {
		return view == ((View) obj);
	}

	@Override
	public Object instantiateItem(final View collection, int position) {
		final LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		@SuppressLint("InflateParams") final RelativeLayout layout = (RelativeLayout) inflater.inflate(
				R.layout.row_pictureviwer_image_item, null);

		((ViewPager) collection).addView(layout, 0);

		final ImageViewTouch iv = (ImageViewTouch) layout
				.findViewById(R.id.row_pictureviewer_image_item);

		iv.setDisplayType(DisplayType.FIT_TO_SCREEN);

		mImageLoader.displayImage(mImageUrls.get(position), iv, mImageOptions,
				mAnimateImageListener);

		return layout;
	}

	@Override
	public void destroyItem(View view, int index, Object obj) {
		((ViewPager) view).removeView((View) obj);
	}

}
