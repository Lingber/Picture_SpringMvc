package com.lingber.bean;

import java.net.URL;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月8日 下午1:30:05 
* 类说明 :
*/
public class CountUser_Bean {
	
	private String userID;
	private String loginTime;
	private String outloginTime;
	private String userName;
	private URL userImage_URL;
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	public String getOutloginTime() {
		return outloginTime;
	}
	public void setOutloginTime(String outloginTime) {
		this.outloginTime = outloginTime;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public URL getUserImage_URL() {
		return userImage_URL;
	}
	public void setUserImage_URL(URL userImage_URL) {
		this.userImage_URL = userImage_URL;
	}
	public CountUser_Bean(String userID, String loginTime, String outloginTime, String userName, URL userImage_URL) {
		super();
		this.userID = userID;
		this.loginTime = loginTime;
		this.outloginTime = outloginTime;
		this.userName = userName;
		this.userImage_URL = userImage_URL;
	}
	public CountUser_Bean() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}
