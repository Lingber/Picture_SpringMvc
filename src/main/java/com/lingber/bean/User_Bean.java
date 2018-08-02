package com.lingber.bean;

import java.io.Serializable;
import java.net.URL;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年3月21日 下午9:20:32 
* 类说明 :
*/
public class User_Bean implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String userName;
	private String passWord;
	private String userID;
	private String phone_Numb;
	private String userImage_Name;
	private URL userImage_URL;
	private String address;
	private String sex;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord){
		this.passWord = passWord;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPhone_Numb() {
		return phone_Numb;
	}
	public void setPhone_Numb(String phone_Numb) {
		this.phone_Numb = phone_Numb;
	}
	
	public String getUserImage_Name() {
		return userImage_Name;
	}
	public void setUserImage_Name(String userImage_Name) {
		this.userImage_Name = userImage_Name;
	}
	public URL getUserImage_URL() {
		return userImage_URL;
	}
	public void setUserImage_URL(URL userImage_URL) {
		this.userImage_URL = userImage_URL;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public User_Bean(String userName, String passWord, String userID, String phone_Numb, String userImage_Name,
			URL userImage_URL, String address, String sex) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.userID = userID;
		this.phone_Numb = phone_Numb;
		this.userImage_Name = userImage_Name;
		this.userImage_URL = userImage_URL;
		this.address = address;
		this.sex = sex;
	}
	public User_Bean() {
		super();
		// TODO Auto-generated constructor stub
	}

}
