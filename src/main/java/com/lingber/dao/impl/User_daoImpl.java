package com.lingber.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.lingber.bean.User_Bean;
import com.lingber.dao.User_dao;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年3月25日 下午8:06:25 
* 类说明 :
*/
@Repository("userdaoimpl")
public class User_daoImpl implements User_dao {

	/*@Autowired*/
	 User_dao user_dao;
	
	@Override
	public List<User_Bean> showAllUserInfo() {
		return user_dao.showAllUserInfo();
	}

}
