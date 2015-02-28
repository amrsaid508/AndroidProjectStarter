
package com.deardhruv.projectstarter.requests.model;

import com.deardhruv.projectstarter.network.Api;
import com.deardhruv.projectstarter.requests.AbstractApiRequest;
import com.deardhruv.projectstarter.response.AbstractApiCallback;
import com.deardhruv.projectstarter.response.model.ImageListResponse;
import com.deardhruv.projectstarter.utils.Logger;

/**
 * Use this request to retrieve the image list via the ImageListResponse api
 * call.
 */
public class ImageListRequest extends AbstractApiRequest {
	private static final String LOGTAG = ImageListRequest.class.getSimpleName();
	private static final Logger LOG = new Logger(LOGTAG);

	/**
	 * The callback used for this request. Declared globally for cancellation.
	 * See {@link #cancel()}.
	 */
	private AbstractApiCallback<ImageListResponse> callback;

	/**
	 * See super constructor
	 * {@link AbstractApiRequest#AbstractApiRequest(Api, String)}.
	 */
	public ImageListRequest(Api api, String tag) {
		super(api, tag);
	}

	/**
	 * Executes the request asynchronously using the built-in mechanism of
	 * Retrofit. The api response is posted on the EventBus.
	 */
	public void execute() {
		callback = new AbstractApiCallback<>(tag);
		api.getImageList(callback);
	}

	@Override
	public void cancel() {
		callback.invalidate();
	}
}
