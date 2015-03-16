
package com.deardhruv.projectstarter.response.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Data {

	@JsonProperty("image_list")
	private List<ImageResult> imageResults;

	public List<ImageResult> getImageResultList() {
		return imageResults;
	}

}
