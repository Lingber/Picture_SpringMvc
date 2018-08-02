package com.lingber.service;


import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lingber.bean.User_Bean;


/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月29日 下午4:00:19 
* 类说明 :
*/
public class test_Json {
	
	public static void main(String[] args) {
		
		User_Bean user_Bean1 = new User_Bean("Lingber", "123", "123456", "150", "123.jpg", null, "桂林市", "男");
		User_Bean user_Bean2 = new User_Bean("Lingber", "123", "123456", "150", "123.jpg", null, "桂林市", "女");
		User_Bean user_Bean3 = new User_Bean("Lingber", "123", "123456", "150", "123.jpg", null, "桂林市", "男？");
		List<User_Bean>user_Beans = new ArrayList<>();
		user_Beans.add(user_Bean1);
/*		user_Beans.add(user_Bean2);
		user_Beans.add(user_Bean3);*/
		String userStr = JSON.toJSONString(user_Beans); 
		System.out.println(userStr);
/*        List<User_Bean> userList = JSON.parseArray(userStr, User_Bean.class);  
        userList.stream().forEach(System.err::println);*/  
		
		 /*String json = '["\u8428\u6469\u8036\uff0cSamoyede", "\u767d\u72fc\uff0c\u5317\u6781\u72fc", "\u5e93\u74e6\u5179", "\u5927\u6bd4\u5229\u725b\u65af\u5c71\u8109", "\u7231\u65af\u57fa\u6469\u72d7\uff0c\u54c8\u58eb\u5947", "\u72d7"]';*/
	}

}
