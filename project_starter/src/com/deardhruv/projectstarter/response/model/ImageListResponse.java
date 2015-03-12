
package com.deardhruv.projectstarter.response.model;

import com.deardhruv.projectstarter.response.AbstractApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The root response object for the api call image_list_json. Contains a list of
 * images.
 */
public class ImageListResponse extends AbstractApiResponse {

	@JsonProperty("data")
	private Data data;

	public ImageListResponse() {
		// nothing.
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public Data getData() {
		return data;
	}

}
