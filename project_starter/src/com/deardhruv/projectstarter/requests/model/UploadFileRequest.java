
package com.deardhruv.projectstarter.requests.model;

import retrofit.mime.TypedFile;
import android.content.Context;

import com.deardhruv.projectstarter.ProjectStarterApplication;
import com.deardhruv.projectstarter.R;
import com.deardhruv.projectstarter.network.Api;
import com.deardhruv.projectstarter.requests.AbstractApiRequest;
import com.deardhruv.projectstarter.response.AbstractApiCallback;
import com.deardhruv.projectstarter.response.model.UploadFileResponse;
import com.deardhruv.projectstarter.utils.Helper;
import com.deardhruv.projectstarter.utils.Logger;

/**
 * Use this request to retrieve the image list via the ImageListResponse api
 * call.
 */
public class UploadFileRequest extends AbstractApiRequest {
	private static final String LOGTAG = UploadFileRequest.class.getSimpleName();
	private static final Logger LOG = new Logger(LOGTAG);

	private static Context mContext;
	/**
	 * The callback used for this request. Declared globally for cancellation.
	 * See {@link #cancel()}.
	 */
	private AbstractApiCallback<UploadFileResponse> callback;

	/**
	 * See super constructor
	 * {@link AbstractApiRequest#AbstractApiRequest(Api, String)}.
	 */
	public UploadFileRequest(Api api, String tag) {
		super(api, tag);
		mContext = ProjectStarterApplication.getAppContext();
	}

	/**
	 * Executes the request asynchronously using the built-in mechanism of
	 * Retrofit. The api response is posted on the EventBus.
	 */
	public void execute(String requestTag, TypedFile file, String get_delete_key) {
		callback = new AbstractApiCallback<>(tag);
		if (!isInternetActive()) {
			callback.postUnexpectedError(mContext.getString(R.string.error_no_internet));
			return;
		}
		api.uploadFile(file, get_delete_key, callback);
	}

	@Override
	public void cancel() {
		callback.invalidate();
	}

	@Override
	public boolean isInternetActive() {
		return Helper.isInternetActive(mContext);
	}
}
