package com.lingber.bean;
/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月26日 下午9:46:53 
* 类说明 :
*/
public class conten_bean {
	
	private double confidence;	
	private String value;
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public conten_bean(double confidence, String value) {
		super();
		this.confidence = confidence;
		this.value = value;
	}
	public conten_bean() {
		super();
		// TODO Auto-generated constructor stub
	}
}
