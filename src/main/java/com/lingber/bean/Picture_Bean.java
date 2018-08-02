package com.lingber.bean;


import java.io.Serializable;
import java.net.URL;

import org.springframework.data.annotation.PersistenceConstructor;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年3月21日 下午9:30:36 
* 类说明 :
*/
public class Picture_Bean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String userID;
	private String picture_Name;
	private double[] location;
	private picture_content_bean content;
	private String shootingTime;
	private URL url;
	private String locationName;
	private String isRecognition;
	private String picture_Class;

	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPicture_Name() {
		return picture_Name;
	}
	public void setPicture_Name(String picture_Name) {
		this.picture_Name = picture_Name;
	}
	public double[] getLocation() {
		return location;
	}
	public void setLocation(double[] location) {
		this.location = location;
	}

	public picture_content_bean getContent() {
		return content;
	}
	public void setContent(picture_content_bean content) {
		this.content = content;
	}
	public String getShootingTime() {
		return shootingTime;
	}
	public void setShootingTime(String shootingTime) {
		this.shootingTime = shootingTime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
    public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getIsRecognition() {
		return isRecognition;
	}
	public void setIsRecognition(String isRecognition) {
		this.isRecognition = isRecognition;
	}
	
	public String getPicture_Class() {
		return picture_Class;
	}
	public void setPicture_Class(String picture_Class) {
		this.picture_Class = picture_Class;
	}
	
	
	@PersistenceConstructor
	public Picture_Bean(String userID, String picture_Name, double[] location, picture_content_bean content, String shootingTime,
			URL url, String locationName, String isRecognition, String picture_Class) {
		super();
		this.userID = userID;
		this.picture_Name = picture_Name;
		this.location = location;
		this.content = content;
		this.shootingTime = shootingTime;
		this.url = url;
		this.locationName = locationName;
		this.isRecognition = isRecognition;
		this.picture_Class = picture_Class;
	}
	
	
	
	public Picture_Bean(String userID, String picture_Name, double x, double y, picture_content_bean content, String shootingTime,URL url,
			String locationName, String isRecognition,String picture_Class) {
		super();
		this.userID = userID;
		this.picture_Name = picture_Name;
		this.location = new double[] { x, y };
		this.content = content;
		this.shootingTime = shootingTime;
		this.url = url;
		this.locationName = locationName;
		this.isRecognition = isRecognition;
		this.picture_Class = picture_Class;
	}


	
	
	




}
