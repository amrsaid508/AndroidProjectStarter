
package com.deardhruv.projectstarter.response.model;

import com.deardhruv.projectstarter.response.AbstractApiResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The root response object for the api call image_list_json. Contains a list of
 * images.
 */
public class ImageListResponse extends AbstractApiResponse {

	@JsonProperty("image_list")
	private ImageListResult imageListResult;

	public ImageListResponse() {
		// nothing.
	}

	public ImageListResult getImageListResult() {
		return imageListResult;
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
