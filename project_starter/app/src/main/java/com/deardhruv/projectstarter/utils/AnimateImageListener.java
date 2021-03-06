
package com.deardhruv.projectstarter.utils;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.deardhruv.projectstarter.R;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class AnimateImageListener extends SimpleImageLoadingListener {
	private final List<String> mDisplayedImage = Collections.synchronizedList(new LinkedList<String>());

	@Override
	public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
		if (loadedImage != null) {
			ImageView imageView = (ImageView) view;
			View rootView = imageView.getRootView();

			if (rootView != null) {
				ProgressBar pb = (ProgressBar) rootView
						.findViewById(R.id.row_adactivity_image_item_progressbar);

				if (pb != null) {
					pb.setVisibility(View.GONE);
				}
			}

			boolean firstDisplay = !mDisplayedImage.contains(imageUri);

			if (firstDisplay) {
				FadeInBitmapDisplayer.animate(imageView, 500);
				mDisplayedImage.add(imageUri);
			}
		}
	}

}
