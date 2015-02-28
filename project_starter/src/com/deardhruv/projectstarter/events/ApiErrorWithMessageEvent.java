
package com.deardhruv.projectstarter.events;

public class ApiErrorWithMessageEvent {

	private String requestTag;
	private String resultMsgUser;

	public ApiErrorWithMessageEvent(String requestTag, String resultMsgUser) {
		this.requestTag = requestTag;
		this.resultMsgUser = resultMsgUser;
	}

	public String getRequestTag() {
		return requestTag;
	}

	public String getResultMsgUser() {
		return resultMsgUser;
	}
}
