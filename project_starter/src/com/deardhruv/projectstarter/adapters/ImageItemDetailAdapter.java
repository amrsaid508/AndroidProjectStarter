
package com.deardhruv.projectstarter.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.response.model.ImageResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;

public class ImageItemDetailAdapter extends BaseAdapter {

	private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    private static LayoutInflater inflater = null;
	private ArrayList<ImageResult> mList;
	private final ImageLoader mImageLoader;
	private final DisplayImageOptions mImageOptions;

	public ImageItemDetailAdapter(Activity activity, List<ImageResult> list) {
		mList = new ArrayList<>(list);
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mImageLoader = ImageLoader.getInstance();
		mImageOptions = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_image_err).cacheInMemory(true)
				.cacheOnDisk(true).bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).considerExifParams(true).build();

	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row_listview_item, null);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
			// holder.image.setDefaultImageResId(R.drawable.ic_launcher);
			// holder.image.setErrorImageResId(R.drawable.ic_image_err);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// Replace all spaces from URL.
		String str = mList.get(position).getImg().replaceAll("[ ]", "%20");
		str = Uri.encode(str, ALLOWED_URI_CHARS);

		mImageLoader.displayImage(str, holder.image, mImageOptions);

		holder.text.setText("" + mList.get(position).getName());

		// convertView.setTag(holder);
		return convertView;
	}

	class ViewHolder {
		TextView text;
		ImageView image;
	}
}
