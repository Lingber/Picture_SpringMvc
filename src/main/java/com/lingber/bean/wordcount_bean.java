package com.lingber.bean;
/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年6月1日 下午8:05:12 
* 类说明 :
*/
public class wordcount_bean {
	
	private String className;
	private double confidence;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	public wordcount_bean(String className, double confidence) {
		super();
		this.className = className;
		this.confidence = confidence;
	}
	public wordcount_bean() {
		super();
		// TODO Auto-generated constructor stub
	}	
	
	
	
}
