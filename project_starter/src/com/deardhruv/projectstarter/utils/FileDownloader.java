
package com.deardhruv.projectstarter.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.deardhruv.projectstarter.events.model.FileDownloadComleteEvent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;
import de.greenrobot.event.EventBus;

public class FileDownloader extends AsyncTask<String, Integer, String> {
	ProgressDialog mProgressDialog;
	String strFolderName;
	Context mContext;

	public FileDownloader(Context context) {
		mContext = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMessage("Downloading");
		mProgressDialog.setIndeterminate(false);
		mProgressDialog.setMax(100);
		mProgressDialog.setCancelable(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		mProgressDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				cancel(true);
			}
		});
		mProgressDialog.show();
	}

	@Override
	protected String doInBackground(String... urls) {
		int count;
		try {
			URL url = new URL((String) urls[0]);
			URLConnection conexion = url.openConnection();
			conexion.connect();

			String filename = url.toString();
			String filenameArray[] = filename.split("\\.");
			String extension = filenameArray[filenameArray.length - 1];

			String targetFileName = "image_" + System.currentTimeMillis() + "." + extension;
			// Change name and subname

			int lenghtOfFile = conexion.getContentLength();

			String appName = Helper.getAppName(mContext);
			String PATH = Environment.getExternalStorageDirectory() + "/" + appName + "/";

			File dir = new File(PATH);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(PATH + targetFileName);
			byte data[] = new byte[1024];
			long total = 0;
			while ((count = input.read(data)) != -1) {
				total += count;
				publishProgress((int) (total * 100 / lenghtOfFile));
				output.write(data, 0, count);
			}
			output.flush();
			output.close();
			input.close();

			return PATH;
		} catch (Exception e) {
			return null;
		}

	}

	protected void onProgressUpdate(Integer... progress) {
		mProgressDialog.setProgress(progress[0]);
		if (mProgressDialog.getProgress() == mProgressDialog.getMax()) {
			mProgressDialog.dismiss();
			Toast.makeText(mContext, "File Downloaded", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPostExecute(String screenshotUri) {
		super.onPostExecute(screenshotUri);
		EventBus.getDefault().post(new FileDownloadComleteEvent(screenshotUri));
	}
}
