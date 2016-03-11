
package com.deardhruv.projectstarter.response.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Data {

    @JsonProperty("image_list")
    private List<ImageResult> imageResults = new ArrayList<>();

    public List<ImageResult> getImageResultList() {
        return imageResults;
    }

    public void setImageResultList(List<ImageResult> imageResults) {
        this.imageResults = imageResults;
    }

}
