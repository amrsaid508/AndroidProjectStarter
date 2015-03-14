
package com.deardhruv.projectstarter.response;

import java.io.Serializable;

import com.deardhruv.projectstarter.utils.Dumper;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The abstract base class for all possible API responses. It contains all
 * elements which are common to all API responses.
 */
public abstract class AbstractApiResponse implements Serializable {

	private static final long serialVersionUID = 6253190108505741009L;

	@JsonProperty("status")
	protected APIInfo apiInfo;

	public APIInfo getApiInfo() {
		return apiInfo;
	}

	public void setApiInfo(APIInfo apiInfo) {
		this.apiInfo = apiInfo;
	}

	/**
	 * Identifies the request which was executed to receive this response. The
	 * tag is used to make sure that a class which executes a requests only
	 * handles the response which is meant for it. This implies that the tag is
	 * unique.
	 */
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String requestTag;

	public void setRequestTag(String requestTag) {
		this.requestTag = requestTag;
	}

	public String getRequestTag() {
		return requestTag;
	}

	@Override
	public String toString() {
		return Dumper.dump(this);
	}

}
