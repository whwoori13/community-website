package com.woori.community.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Article {

	private int num;
	private String customerId;
	private String writeDate;
	private String title;
	private String content;
	private int viewCount;
	private int replyCount;
	private String topic;

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getViewCount() {
		return viewCount;
	}

	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}

	public int getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public String toString() {
		return "Article [num=" + num + ", customerId=" + customerId + ", writeDate=" + writeDate + ", title=" + title + ", content=" + content
				+ ", viewCount=" + viewCount + ", replyCount=" + replyCount + ", topic=" + topic + "]";
	}

}
