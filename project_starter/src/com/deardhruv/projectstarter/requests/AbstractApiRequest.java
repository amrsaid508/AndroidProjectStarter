
package com.deardhruv.projectstarter.requests;

import com.deardhruv.projectstarter.network.Api;

/**
 * Base class for the API requests. Provides functionality for cancelling api
 * requests.
 */
public abstract class AbstractApiRequest {
	/** The endpoint for excecuting the calls. */
	protected Api api;

	/** Identifies the request. */
	protected String tag;

	/**
	 * Initialize the request with the passed values.
	 * 
	 * @param api The {@link Api} used for executing the calls.
	 * @param tag Identifies the request.
	 */
	public AbstractApiRequest(Api api, String tag) {
		this.api = api;
		this.tag = tag;
	}

	/**
	 * Cancels the running request. The response will still be delivered but
	 * will be ignored. The implementation should call invalidate on the
	 * callback which is used for the request.
	 */
	public abstract void cancel();

	/**
	 * Check for active internet connection
	 * 
	 * @return
	 */
	public abstract boolean isInternetActive();
}
