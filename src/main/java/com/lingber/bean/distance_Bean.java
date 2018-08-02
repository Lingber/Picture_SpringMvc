package com.lingber.bean;

import java.net.URL;

import org.springframework.data.annotation.PersistenceConstructor;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月22日 下午10:44:30 
* 类说明 :
*/
public class distance_Bean {
	
	private String userName;
	private String userID;
	private String picture_Name;
	private double[] location;
	private picture_content_bean content;
	private String shootingTime;
	private URL url;
	private String locationName;
	private String isRecognition;
	private String picture_Class;
	private double distance;
	private URL userImage_URL;
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
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public URL getUserImage_URL() {
		return userImage_URL;
	}
	public void setUserImage_URL(URL userImage_URL) {
		this.userImage_URL = userImage_URL;
	}
	
	
	@PersistenceConstructor
	public distance_Bean(String userName, String userID, String picture_Name, double[] location,
			picture_content_bean content, String shootingTime, URL url, String locationName, String isRecognition,
			String picture_Class, double distance,URL userImage_URL) {
		super();
		this.userName = userName;
		this.userID = userID;
		this.picture_Name = picture_Name;
		this.location = location;
		this.content = content;
		this.shootingTime = shootingTime;
		this.url = url;
		this.locationName = locationName;
		this.isRecognition = isRecognition;
		this.picture_Class = picture_Class;
		this.distance = distance;
		this.userImage_URL = userImage_URL;
	}
	
	public distance_Bean(String userName, String userID, String picture_Name, double x, double y,
			picture_content_bean content, String shootingTime, URL url, String locationName, String isRecognition,
			String picture_Class, double distance,URL userImage_URL) {
		super();
		this.userName = userName;
		this.userID = userID;
		this.picture_Name = picture_Name;
		this.location = new double[] { x, y };
		this.content = content;
		this.shootingTime = shootingTime;
		this.url = url;
		this.locationName = locationName;
		this.isRecognition = isRecognition;
		this.picture_Class = picture_Class;
		this.distance = distance;
		this.userImage_URL = userImage_URL;
	}
	
	
	
	public distance_Bean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
