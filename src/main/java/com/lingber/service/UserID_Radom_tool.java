package com.lingber.service;
/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年4月9日 下午8:35:20 
* 类说明 :
*/

import java.util.UUID;

public class UserID_Radom_tool {
	
	public String UUID_UserID() {
		
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
			// 去掉"-"符号
		String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
		return temp;
	}
	
	public String randomFileName(){
    	UUID uuid = UUID.randomUUID();
    	String str = uuid.toString();
    		// 去掉"-"符号
    	String temp = str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
    	 return temp;
	}

}
