
package com.deardhruv.projectstarter.response.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Data {

	@JsonProperty("image_list")
	private List<ImageResult> imageResults;

	public List<ImageResult> getImageResultList() {
		return imageResults;
	}

}
