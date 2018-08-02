package com.lingber.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingber.bean.CountUser_Bean;
import com.lingber.bean.Msg_bean;
import com.lingber.bean.User_Bean;
import com.lingber.service.oss_service;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年4月9日 下午8:55:02 
* 类说明 :登录控制器接口
*/
@RequestMapping("/login") 
@Controller
public class login_controller {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private oss_service oss_service;
	
	//登录验证接口
    @ResponseBody
    @RequestMapping("/loginCheckUp") 
	public Msg_bean  login_CheckUp(String phonenumbLogin,String Loginpassword,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
    	System.out.println(phonenumbLogin);
    	//生成查找请求
		Query query_phone =new Query(Criteria.where("phone_Numb").is(phonenumbLogin));
		//利用mongoTemplatey运行查找
		List<User_Bean> find_phones = mongoTemplate.find(query_phone, User_Bean.class, "mvc");
		if (find_phones.size()!=0) {
					for(User_Bean find_phone : find_phones){
						System.out.println(find_phone.getPhone_Numb());
						System.out.println(find_phone.getUserName());
								if (find_phone.getPassWord().equals(Loginpassword)) {//如果密码正确
										System.err.println("密码正确");
										find_phone.setUserImage_URL(oss_service.get_oss_url(find_phone.getUserImage_Name()));
										//加入登录时间
										Query query_count =new Query(Criteria.where("userID").is(find_phone.getUserID()));
										CountUser_Bean countUser_Bean_find= mongoTemplate.findOne(query_count, CountUser_Bean.class,"count");
										//生成时间
					    		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
								        String login_date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
								        //检测到登录的时候，将登录信息插入数据库
										if (countUser_Bean_find!=null) {
											countUser_Bean_find.setLoginTime(login_date);
											countUser_Bean_find.setOutloginTime(null);
											countUser_Bean_find.setUserName(find_phone.getUserName());
											countUser_Bean_find.setUserImage_URL(oss_service.get_oss_url(find_phone.getUserImage_Name()));
										}else {
							        		URL url= oss_service.get_oss_url(find_phone.getUserImage_Name());
											CountUser_Bean countUser_Bean =new CountUser_Bean(find_phone.getUserID(), login_date, null,find_phone.getUserName(),url);
											mongoTemplate.insert(countUser_Bean, "count");
										}
								}else {
									Msg_bean msg_bean =new Msg_bean("08", "登录失败！密码错误！",null);
									return msg_bean;
								}
								break;
				      }
				
				System.out.println("登录成功");
				Msg_bean<User_Bean>msg_bean =new Msg_bean<>("06", "登录成功！",find_phones);
				return msg_bean;
		}else {
			Msg_bean<User_Bean>msg_bean =new Msg_bean("07", "登录失败！账号不存在！",null);
			return msg_bean;
		}
	}
   
	//退出登录
    @ResponseBody
    @RequestMapping("/out_login") 
	public Msg_bean  out_login(String phonenumbLogin) throws ServletException, IOException{
    	System.out.println(phonenumbLogin);
		Query query_phone =new Query(Criteria.where("phone_Numb").is(phonenumbLogin));
		List<User_Bean> find_phones = mongoTemplate.find(query_phone, User_Bean.class, "mvc");
		if (find_phones.size()!=0) {
					for(User_Bean find_phone : find_phones){
						//将登录信息改成NULL
						Query query_count =new Query(Criteria.where("userID").is(find_phone.getUserID()));
						CountUser_Bean countUser_Bean= mongoTemplate.findOne(query_count, CountUser_Bean.class,"count");
						//生成时间
	    		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	    		        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
						countUser_Bean.setOutloginTime(date);
								break;
				      }
				System.out.println("注销成功");
				Msg_bean<User_Bean>msg_bean =new Msg_bean<>("10", "注销成功！",null);
				return msg_bean;
		}else {
			Msg_bean<User_Bean>msg_bean =new Msg_bean("07", "账号不存在！",null);
			return msg_bean;
		}
	}
    
    
    @ResponseBody
    @RequestMapping("/sessionCheckUp") 
	public Msg_bean  session_CheckUp(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
    	//获取客户端保存的session,若没有过期，则可以提取出信息，如果过期，则要求重新登录。
    	HttpSession httpSession = request.getSession(false);
    	Cookie[] cookies = request.getCookies();
        if (null==cookies) {  
	            System.out.println("没有cookie=========");
				Msg_bean msg_bean =new Msg_bean("08", "登录超时！请重新登录！",null);
				return msg_bean;
        } else {  
        		if (httpSession.getAttribute("userID")!=null) {
    	    		for(Cookie cookie : cookies){ //选定所有
    		    		cookie.setMaxAge(43200*60);// 重新设置时间 
    		    		cookie.setPath("/Picture_SpringMvc");
    		    		response.addCookie(cookie); 
    		    		}
				}else {
					Msg_bean msg_bean =new Msg_bean("08", "登录超时！请重新登录！",null);
					return msg_bean;
				}
				Msg_bean msg_bean =new Msg_bean("09", "重新激活！",null);
				return msg_bean;
        }  
	}
    
    @ResponseBody
    @RequestMapping("/sessionDelect") 
	public Msg_bean  del_session(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
    	//获取客户端保存的session,若没有过期，则可以提取出信息，如果过期，则要求重新登录。
    	HttpSession httpSession = request.getSession(false);
    	Cookie[] cookies = request.getCookies();
        if (null==cookies) {  
	            System.out.println("没有cookie=========");
				Msg_bean msg_bean =new Msg_bean("08", "登录超时！请重新登录！",null);
				return msg_bean;
        } else {
	    		if (httpSession!=null) {//如果session尚未过期（cookie还存在）
	            	System.out.println("读取userid:"+httpSession.getAttribute("userID"));
	    		}
	    		for(Cookie cookie : cookies){ //选定所有
                    cookie.setValue(null);  
                    cookie.setMaxAge(0);// 立即销毁cookie
                    cookie.setPath("/Picture_SpringMvc"); 
                    response.addCookie(cookie);
	    		}
				Msg_bean msg_bean =new Msg_bean("10", "注销成功！销毁会话！",null);
				return msg_bean;
        }  
	}
    
    @ResponseBody
    @RequestMapping("/server_loginCheckUp") 
	public Msg_bean  server_login_CheckUp(String phonenumbLogin,String passWord,HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		Query query_phone =new Query(Criteria.where("phone_Numb").is(phonenumbLogin));
		User_Bean find_phone = mongoTemplate.findOne(query_phone, User_Bean.class, "mvc");
		if (find_phone!=null) {
				if (find_phone.getPassWord().equals(passWord)) {//如果密码正确
					//取出userID
					String userid=find_phone.getUserID();
					//创建session
			    	HttpSession httpSession = request.getSession(); 
			        // 向session 保存一个数据  
			        httpSession.setAttribute("userID", userid); 
			        System.out.println("已将userid保存进session:"+userid);
			        Cookie cookie = new Cookie("JSESSIONID", httpSession.getId());
			        cookie.setMaxAge(43200*60);// 设置为30天  
			        cookie.setPath("/Picture_SpringMvc");  
			        System.out.println("已添加===============");  
			        response.addCookie(cookie);
			        
				}else {
					Msg_bean msg_bean =new Msg_bean("08", "登录失败！密码错误！",null);
					return msg_bean;
				}
				Msg_bean msg_bean =new Msg_bean("06", "登录成功！",null);
				return msg_bean;
		}else {
			Msg_bean msg_bean =new Msg_bean("07", "登录失败！账号不存在！",null);
			return msg_bean;
		}
	}
    
    
    
    

}
