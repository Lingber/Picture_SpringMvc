package com.lingber.bean;

import java.net.URL;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年6月5日 下午1:12:49 
* 类说明 :
*/
public class UserChat_withAll_bean {
	private String receiverID;
	private String senderID;
	private String chatContent;
	private URL picURL;
	private String chatClass;
	private String chatTime;
	private String senderName;
	private String receiverName;
	private URL senderURL;
	private URL receiverURL;
	private String picture_Name;
	public String getReceiverID() {
		return receiverID;
	}
	public void setReceiverID(String receiverID) {
		this.receiverID = receiverID;
	}
	public String getSenderID() {
		return senderID;
	}
	public void setSenderID(String senderID) {
		this.senderID = senderID;
	}
	public String getChatContent() {
		return chatContent;
	}
	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}
	public URL getPicURL() {
		return picURL;
	}
	public void setPicURL(URL picURL) {
		this.picURL = picURL;
	}
	public String getChatClass() {
		return chatClass;
	}
	public void setChatClass(String chatClass) {
		this.chatClass = chatClass;
	}
	public String getChatTime() {
		return chatTime;
	}
	public void setChatTime(String chatTime) {
		this.chatTime = chatTime;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public URL getSenderURL() {
		return senderURL;
	}
	public void setSenderURL(URL senderURL) {
		this.senderURL = senderURL;
	}
	public URL getReceiverURL() {
		return receiverURL;
	}
	public void setReceiverURL(URL receiverURL) {
		this.receiverURL = receiverURL;
	}
	public String getPicture_Name() {
		return picture_Name;
	}
	public void setPicture_Name(String picture_Name) {
		this.picture_Name = picture_Name;
	}
	public UserChat_withAll_bean(String receiverID, String senderID, String chatContent, URL picURL, String chatClass,
			String chatTime, String senderName, String receiverName, URL senderURL, URL receiverURL,
			String picture_Name) {
		super();
		this.receiverID = receiverID;
		this.senderID = senderID;
		this.chatContent = chatContent;
		this.picURL = picURL;
		this.chatClass = chatClass;
		this.chatTime = chatTime;
		this.senderName = senderName;
		this.receiverName = receiverName;
		this.senderURL = senderURL;
		this.receiverURL = receiverURL;
		this.picture_Name = picture_Name;
	}
	public UserChat_withAll_bean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
