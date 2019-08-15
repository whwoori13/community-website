package com.woori.community.entity;

public class Customer {

	private int num;
	private String id;
	private String pw;
	private String email;
	private String address;
	private String joinDate;
	private int newAlert;
	
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getJoinDate() {
		return joinDate;
	}
	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}
	public int getNewAlert() {
		return newAlert;
	}
	public void setNewAlert(int newAlert) {
		this.newAlert = newAlert;
	}
	
	
}
