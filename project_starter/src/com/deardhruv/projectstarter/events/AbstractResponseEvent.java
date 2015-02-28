
package com.deardhruv.projectstarter.events;

import retrofit.RetrofitError;

/**
 * Abstract base class for all response events. Contains general information
 * about the response.
 */
public abstract class AbstractResponseEvent {
	/**
	 * A {@link RetrofitError} object containing detail information about the
	 * error. If this field is set, a severe error occured.
	 */
	protected RetrofitError retrofitError;

	/** The tag for identifying the request which belongs to this response. */
	protected String requestTag;

	/**
	 * Create some kind of ResponseEvent. The passed error indicates that a
	 * severe error happened when making the api request.
	 * 
	 * @param requestTag Identifies the request which belongs to this response.
	 * @param retrofitError An object of type {@link RetrofitError}.
	 */
	public AbstractResponseEvent(String requestTag, RetrofitError retrofitError) {
		this.requestTag = requestTag;
		this.retrofitError = retrofitError;
	}

	/**
	 * Create some kind of ResponseEvent. The passed request tag is for
	 * identifying the request which belongs to this response.
	 * 
	 * @param requestTag Identifies the request.
	 */
	public AbstractResponseEvent(String requestTag) {
		this.requestTag = requestTag;
	}

	public RetrofitError getRetrofitError() {
		return retrofitError;
	}

	public String getRequestTag() {
		return requestTag;
	}
}
