
package com.deardhruv.projectstarter.response;

import android.text.TextUtils;

import com.deardhruv.projectstarter.events.ApiErrorEvent;
import com.deardhruv.projectstarter.events.ApiErrorWithMessageEvent;
import com.deardhruv.projectstarter.events.RequestFinishedEvent;
import com.deardhruv.projectstarter.network.Api;
import com.deardhruv.projectstarter.network.ApiClient;
import com.deardhruv.projectstarter.requests.model.ImageListRequest;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Use this class to have a callback which can be used for the api calls in
 * {@link Api}. Such a callback can be invalidated to not notify its caller
 * about the api response. Furthermore it handles finishing the request after
 * the caller has handled the response.
 */
public class AbstractApiCallback<T extends AbstractApiResponse> implements Callback<T> {

	/** Indicates if the callback was invalidated. */
	private boolean isInvalidated;

	/** The tag of the request which uses this callback. */
	private String requestTag;

	/**
	 * Creates an {@link AbstractApiCallback} with the passed request tag. The tag
	 * is used to finish the request after the response has been handled. See
	 * {@link #finishRequest}.
	 *
	 * @param requestTag The tag of the request which uses this callback.
	 */
	public AbstractApiCallback(String requestTag) {
		isInvalidated = false;
		this.requestTag = requestTag;
	}

	@Override
	public void failure(RetrofitError error) {
		if (!isInvalidated) {
			EventBus.getDefault().post(new ApiErrorEvent(requestTag, error));
		}

		finishRequest();
	}

	@Override
	public void success(T result, Response response) {
		if (isInvalidated) {
			finishRequest();
			return;
		}

		APIInfo info = result.getApiInfo();
		String resultCode = info.getResultCode();

		if (!"1".equals(resultCode)) {
			// Error occurred. Check for error message from api.
			String resultMsgUser = info.getMessage();

			if (!TextUtils.isEmpty(resultMsgUser)) {
				EventBus.getDefault().post(new ApiErrorWithMessageEvent(requestTag, resultMsgUser));

			} else {
				EventBus.getDefault().post(new ApiErrorEvent(requestTag));
			}

		} else {
			modifyResponseBeforeDelivery(result);
			result.setRequestTag(requestTag);
			EventBus.getDefault().post(result);
		}

		finishRequest();
	}

	/**
	 * Invalidates this callback. This means the caller doesn't want to be
	 * called back anymore.
	 */
	public void invalidate() {
		isInvalidated = true;
	}

	/**
	 * Posts a {@link RequestFinishedEvent} on the EventBus to tell the
	 * {@link ApiClient} to remove the request from the list of running
	 * requests.
	 */
	private void finishRequest() {
		EventBus.getDefault().post(new RequestFinishedEvent(requestTag));
	}

	/**
	 * This is for callbacks which extend ApiCallback and want to modify the
	 * response before it is delivered to the caller.
	 *
	 * @param result The api response.
	 */
	@SuppressWarnings("UnusedParameters")
	protected void modifyResponseBeforeDelivery(T result) {
		// Do nothing here. Only for subclasses.
	}

	/**
	 * Call this methode if No internet connection or other use. See
	 * {@link ImageListRequest#execute()}
	 * 
	 * @param resultMsgUser
	 */
	public void postUnexpectedError(String resultMsgUser) {
		EventBus.getDefault().post(new ApiErrorWithMessageEvent(requestTag, resultMsgUser));
	}
}
