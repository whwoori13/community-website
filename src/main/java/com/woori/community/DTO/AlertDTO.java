package com.woori.community.DTO;

public class AlertDTO {

	private int replyNum;
	private String customerId;
	private String content;
	private String author;

	public AlertDTO(int replyNum, String customerId, String content, String author) {
		super();
		this.replyNum = replyNum;
		this.customerId = customerId;
		this.content = content;
		this.author = author;
	}

	public int getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
