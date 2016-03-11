
package com.deardhruv.projectstarter.response.model;

import com.deardhruv.projectstarter.response.AbstractApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The root response object for the api call image_list_json. Contains a list of
 * images.
 */
public class ImageListResponse extends AbstractApiResponse {

	private static final long serialVersionUID = -6933878383763810916L;
	@JsonProperty("data")
	private Data data = new Data();

	public ImageListResponse() {
		// nothing.
	}

	public Data getData() {
		return data;
	}

}
