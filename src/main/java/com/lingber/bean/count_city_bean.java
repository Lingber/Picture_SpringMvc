package com.lingber.bean;
/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年6月3日 上午10:51:31 
* 类说明 :
*/
public class count_city_bean {
	
	private String cityName;	
	private int userNumber;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public int getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(int userNumber) {
		this.userNumber = userNumber;
	}
	public count_city_bean(String cityName, int userNumber) {
		super();
		this.cityName = cityName;
		this.userNumber = userNumber;
	}
	public count_city_bean() {
		super();
		// TODO Auto-generated constructor stub
	}

}
