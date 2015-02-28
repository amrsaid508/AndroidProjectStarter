
package com.deardhruv.projectstarter.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class APIInfo {

	@JsonProperty("result_code")
	private String result_code;

	@JsonProperty("message")
	private String message;

	public String getResultCode() {
		return result_code;
	}

	public void setResultCode(String result_code) {
		this.result_code = result_code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
