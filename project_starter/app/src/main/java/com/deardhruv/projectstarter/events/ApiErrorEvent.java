
package com.deardhruv.projectstarter.events;

import retrofit.RetrofitError;

public class ApiErrorEvent {
	private final String requestTag;
	private RetrofitError retrofitError;

	/**
	 * Create an error event without any further error information.
	 *
	 * @param requestTag Identifies the request that failed.
	 */
	public ApiErrorEvent(String requestTag) {
		this.requestTag = requestTag;
	}

	/**
	 * Create an error event with detailed error information in the form of a
	 * RetrofitError.
	 *
	 * @param requestTag Identifies the request that failed.
	 * @param retrofitError An error object with detailed information.
	 */
	public ApiErrorEvent(String requestTag, RetrofitError retrofitError) {
		this.requestTag = requestTag;
		this.retrofitError = retrofitError;
	}

	public String getRequestTag() {
		return requestTag;
	}

	public RetrofitError getRetrofitError() {
		return retrofitError;
	}
}
