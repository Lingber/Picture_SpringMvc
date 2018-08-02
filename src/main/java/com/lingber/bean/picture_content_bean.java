package com.lingber.bean;
/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月19日 上午9:49:49 
* 类说明 :
*/

import java.util.List;

public class picture_content_bean {
	
	
	private String picture_Name;	
	private List<String> picContent;
	private String content_Class;
	public String getPicture_Name() {
		return picture_Name;
	}
	public void setPicture_Name(String picture_Name) {
		this.picture_Name = picture_Name;
	}
	public List<String> getPicContent() {
		return picContent;
	}
	public void setPicContent(List<String> picContent) {
		this.picContent = picContent;
	}
	public String getContent_Class() {
		return content_Class;
	}
	public void setContent_Class(String content_Class) {
		this.content_Class = content_Class;
	}
	public picture_content_bean(String picture_Name, List<String> picContent, String content_Class) {
		super();
		this.picture_Name = picture_Name;
		this.picContent = picContent;
		this.content_Class = content_Class;
	}
	public picture_content_bean() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
}
