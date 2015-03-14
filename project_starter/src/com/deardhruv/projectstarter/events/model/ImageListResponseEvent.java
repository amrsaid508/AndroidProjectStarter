
package com.deardhruv.projectstarter.events.model;

import retrofit.RetrofitError;

import com.deardhruv.projectstarter.events.AbstractResponseEvent;
import com.deardhruv.projectstarter.response.model.ImageListResponse;

/** Delivers the api response which contains the update message information. */
public class ImageListResponseEvent extends AbstractResponseEvent {
	private ImageListResponse response;

	/**
	 * Create a UpdateMessageResponseEvent with a valid response object. The
	 * object can still contain error information.
	 * 
	 * @param requestTag Identifies the request which belongs to this response.
	 * @param response An object of type {@link AppMessageResponse}.
	 */
	public ImageListResponseEvent(String requestTag, ImageListResponse response) {
		super(requestTag);
		this.response = response;
	}

	/**
	 * Create a AppMessageResponseEvent with a {@link RetrofitError} object.
	 * This means that the api request failed with a severe error and no valid
	 * xml could be retrieved.
	 * 
	 * @param retrofitError An object of type {@link RetrofitError}.
	 */
	public ImageListResponseEvent(String requestTag, RetrofitError retrofitError) {
		super(requestTag, retrofitError);
	}

	public ImageListResponse getResponse() {
		return response;
	}

}
