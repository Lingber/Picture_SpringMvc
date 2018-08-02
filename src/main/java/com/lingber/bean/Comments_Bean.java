package com.lingber.bean;

import java.io.Serializable;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年3月21日 下午9:33:54 
* 类说明 :
*/
public class Comments_Bean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String picture_Name;
	private String comment;
	private String reviewer_ID;
	private String publishTime;

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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public Comments_Bean(String picture_Name, String comment, String reviewer_ID, String publishTime) {
		super();
		this.picture_Name = picture_Name;
		this.comment = comment;
		this.reviewer_ID = reviewer_ID;
		this.publishTime = publishTime;
	}
	public Comments_Bean() {
		super();
		// TODO Auto-generated constructor stub
	}



}
