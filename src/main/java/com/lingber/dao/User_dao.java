package com.lingber.dao;
/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年3月25日 下午4:22:57 
* 类说明 :
*/

import java.util.List;

import org.springframework.stereotype.Repository;

import com.lingber.bean.User_Bean;

/*@Repository("userdao")*/
public interface User_dao {
	 List<User_Bean>showAllUserInfo();
}
