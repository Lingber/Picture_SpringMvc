package com.lingber.bean;

import java.net.URL;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月12日 下午2:54:46 
* 类说明 :
*/
public class CommentsWithUserName_Bean {
	
	
	private String picture_Name;
	private String comment;
	private String reviewer_ID;
	private String publishTime;
	private String userName;
	private String userID;
	private URL userImage_URL;
	public String getPicture_Name() {
		return picture_Name;
	}
	public void setPicture_Name(String picture_Name) {
		this.picture_Name = picture_Name;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getReviewer_ID() {
		return reviewer_ID;
	}
	public void setReviewer_ID(String reviewer_ID) {
		this.reviewer_ID = reviewer_ID;
	}
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public URL getUserImage_URL() {
		return userImage_URL;
	}
	public void setUserImage_URL(URL userImage_URL) {
		this.userImage_URL = userImage_URL;
	}
	public CommentsWithUserName_Bean(String picture_Name, String comment, String reviewer_ID, String publishTime,
			String userName, String userID, URL userImage_URL) {
		super();
		this.picture_Name = picture_Name;
		this.comment = comment;
		this.reviewer_ID = reviewer_ID;
		this.publishTime = publishTime;
		this.userName = userName;
		this.userID = userID;
		this.userImage_URL = userImage_URL;
	}
	public CommentsWithUserName_Bean() {
		super();
		// TODO Auto-generated constructor stub
	}



}
