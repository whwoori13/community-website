package com.woori.community.DTO;

public class CTmatchDTO {

	private String category;
	private String topic;

	public CTmatchDTO(String category, String topic) {
		this.category = category;
		this.topic = topic;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

}
