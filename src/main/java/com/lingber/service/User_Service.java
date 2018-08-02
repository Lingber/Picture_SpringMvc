package com.lingber.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lingber.bean.User_Bean;
import com.lingber.dao.User_dao;
import com.lingber.dao.impl.User_daoImpl;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年3月25日 下午4:27:14 
* 类说明 :
*/
@Service("userservice")
public class User_Service {

	@Autowired
	private User_daoImpl user_daoImpl;
	
	public List<User_Bean> showAllUserInfo() {
		
		return user_daoImpl.showAllUserInfo();
	}
	
	
}
