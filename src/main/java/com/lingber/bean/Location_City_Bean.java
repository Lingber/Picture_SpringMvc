package com.lingber.bean;

import java.net.URL;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月22日 下午10:48:51 
* 类说明 :
*/
public class Location_City_Bean {
	
	private String province;
	private String city;
	private String county;
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}

	public Location_City_Bean(String province, String city, String county) {
		super();
		this.province = province;
		this.city = city;
		this.county = county;
	}
	public Location_City_Bean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
