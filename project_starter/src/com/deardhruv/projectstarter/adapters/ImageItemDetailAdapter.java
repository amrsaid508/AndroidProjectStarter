
package com.deardhruv.projectstarter.adapters;

import java.util.ArrayList;
import java.util.List;

import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.activities.PictureViewerActivity;
import com.deardhruv.projectstarter.response.model.ImageResult;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//public class ImageItemDetailAdapter extends BaseAdapter {
public class ImageItemDetailAdapter extends
		RecyclerView.Adapter<ImageItemDetailAdapter.MyViewHolder>implements OnClickListener {

	private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
	private static LayoutInflater inflater = null;
	private ArrayList<ImageResult> mList;
	private final ImageLoader mImageLoader;
	private final DisplayImageOptions mImageOptions;
	private final Context mContext;
	private final ArrayList<String> mImageUrls;
	private RecyclerView recyclerView;

	public ImageItemDetailAdapter(Activity activity, List<ImageResult> list) {
		mList = new ArrayList<>(list);
		mContext = activity.getApplicationContext();
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		mImageLoader = ImageLoader.getInstance();
		mImageOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher)
				.showImageForEmptyUri(R.drawable.ic_image_err).cacheInMemory(true).cacheOnDisk(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).considerExifParams(true).build();

		mImageUrls = new ArrayList<>();
		for (ImageResult imageResult : mList) {
			mImageUrls.add(imageResult.getImg());
		}

	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		// Replace all spaces from URL.
		String str = mList.get(position).getImg().replaceAll("[ ]", "%20");
		str = Uri.encode(str, ALLOWED_URI_CHARS);

		mImageLoader.displayImage(str, holder.image, mImageOptions);
		holder.text.setText("" + mList.get(position).getName());
		holder.itemView.setOnClickListener(this);

	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = inflater.inflate(R.layout.row_listview_item, parent, false);
		return new MyViewHolder(v);
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView text;
		ImageView image;

		public MyViewHolder(View itemView) {
			super(itemView);
			text = (TextView) itemView.findViewById(R.id.text);
			image = (ImageView) itemView.findViewById(R.id.image);
		}

	}

	@Override
	public void onClick(View v) {
		int position = recyclerView.getChildLayoutPosition(v);
		final Intent intent = new Intent(mContext, PictureViewerActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putStringArrayListExtra(PictureViewerActivity.EXTRA_IMAGE_URLS, mImageUrls);
		intent.putExtra(PictureViewerActivity.EXTRA_IMAGE_SELECTION, position);
		mContext.startActivity(intent);
	}

	public void setRecyclerView(RecyclerView recyclerView) {
		this.recyclerView = recyclerView;
	}
}
