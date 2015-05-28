
package com.deardhruv.projectstarter.events.model;

public class FileDownloadComleteEvent {

	private final String filePath;

	public String getFilePath() {
		return filePath;
	}

	public FileDownloadComleteEvent(String filePath) {
		this.filePath = filePath;
	}

}
