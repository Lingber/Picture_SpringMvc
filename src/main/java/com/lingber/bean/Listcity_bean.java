package com.lingber.bean;
/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年6月3日 下午12:38:27 
* 类说明 :
*/

import java.util.List;

public class Listcity_bean {
	private List<String>cityName;
	private List<Integer>userNumber;
	public List<String> getCityName() {
		return cityName;
	}
	public void setCityName(List<String> cityName) {
		this.cityName = cityName;
	}
	public List<Integer> getUserNumber() {
		return userNumber;
	}
	public void setUserNumber(List<Integer> userNumber) {
		this.userNumber = userNumber;
	}
	public Listcity_bean(List<String> cityName, List<Integer> userNumber) {
		super();
		this.cityName = cityName;
		this.userNumber = userNumber;
	}
	public Listcity_bean() {
		super();
		// TODO Auto-generated constructor stub
	}

	

}
