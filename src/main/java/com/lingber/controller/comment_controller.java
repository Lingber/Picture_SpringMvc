package com.lingber.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingber.bean.CommentsWithUserName_Bean;
import com.lingber.bean.Comments_Bean;
import com.lingber.bean.Msg_bean;
import com.lingber.bean.PictureWithUsername_Bean;
import com.lingber.bean.Picture_Bean;
import com.lingber.bean.User_Bean;
import com.lingber.service.UserID_Radom_tool;
import com.lingber.service.oss_service;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月3日 下午9:42:43 
* 类说明 :
*/
@RequestMapping("/comment") 
@Controller
public class comment_controller {
	
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private oss_service oss_service;
	
	
	@ResponseBody
    @RequestMapping("/findAll_comment_withusername")
	public Msg_bean<CommentsWithUserName_Bean> find_all(@RequestParam(value="pn",defaultValue="1")Integer pn){
		List<CommentsWithUserName_Bean>commentsWithUserName_Beans = new ArrayList<>();
	    //引入pageHelper插件
		PageHelper.startPage(pn, 10);
	    //startPage后紧跟的查询就是分页查询
		List<Comments_Bean> find = mongoTemplate.findAll(Comments_Bean.class, "comment");
		if (find.size()!=0) {
			for (Comments_Bean comments_Bean : find) {
				System.out.println("1");
				Query query_username =new Query(Criteria.where("userID").is(comments_Bean.getReviewer_ID()));
				User_Bean find_userName = mongoTemplate.findOne(query_username, User_Bean.class, "mvc");
				System.out.println("2");
				if (find_userName!=null) {
	    			String userName=find_userName.getUserName();
	    			CommentsWithUserName_Bean commentsWithUserName_Bean = new CommentsWithUserName_Bean(comments_Bean.getPicture_Name(),
	    					comments_Bean.getComment(), comments_Bean.getReviewer_ID(), comments_Bean.getPublishTime(), userName, null, null);
	    			System.out.println("3");
	    			commentsWithUserName_Beans.add(commentsWithUserName_Bean);
				}
			}
		}
		System.out.println("查找成功！");
	    //使用PageInfo包装查询后的结果
		//PageInfo封装了详细的分页信息包括查询出的数据，只需将pageInfo交给页面即可
		//参数5表示连续显示5页
		PageInfo page = new PageInfo(commentsWithUserName_Beans,10);
		//JSONObject jsonObject = new JSONObject(page);
		return Msg_bean.success().add("pageInfo",page);
	}
	
	
	@ResponseBody
    @RequestMapping("/find_comment_withusername")
	public Msg_bean<CommentsWithUserName_Bean> find_comment_withusername(String userName){
		List<CommentsWithUserName_Bean>commentsWithUserName_Beans = new ArrayList<>();
	    //引入pageHelper插件
		PageHelper.startPage(1, 10);
	    //startPage后紧跟的查询就是分页查询
		Query query_userName = new Query(Criteria.where("userName").is(userName));
		List<User_Bean>user_Beans = mongoTemplate.find(query_userName, User_Bean.class, "mvc");
		if (user_Beans.size()!=0) {
			for (User_Bean user_Bean : user_Beans) {
				String userID = user_Bean.getUserID();
				Query query_comment = new Query(Criteria.where("reviewer_ID").is(userID));
				List<Comments_Bean>comments_Beans=mongoTemplate.find(query_comment, Comments_Bean.class, "comment");
					if (comments_Beans.size()!=0) {
						for (Comments_Bean comments_Bean : comments_Beans) {
							CommentsWithUserName_Bean commentsWithUserName_Bean = new CommentsWithUserName_Bean(comments_Bean.getPicture_Name(),
									comments_Bean.getComment(), comments_Bean.getReviewer_ID(), comments_Bean.getPublishTime(), userName, comments_Bean.getReviewer_ID(),
									null);
							commentsWithUserName_Beans.add(commentsWithUserName_Bean);
						}
					}
			}
		}
		System.out.println("查找成功！");
	    //使用PageInfo包装查询后的结果
		//PageInfo封装了详细的分页信息包括查询出的数据，只需将pageInfo交给页面即可
		//参数5表示连续显示5页
		PageInfo page = new PageInfo(commentsWithUserName_Beans,10);
		//JSONObject jsonObject = new JSONObject(page);
		return Msg_bean.success().add("pageInfo",page);
	}
	
	
	@ResponseBody
    @RequestMapping("/submit_comment")
	public Msg_bean<Comments_Bean> comment_submit(String userId,String picture_Name,String comment) {
		
				//新建评论
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
				Comments_Bean comments_Bean =new Comments_Bean(picture_Name, comment, userId, date);
				//查询是否有这张图片
				Query query = new Query(Criteria.where("picture_Name").is(picture_Name));
				Picture_Bean picture_Bean = mongoTemplate.findOne(query, Picture_Bean.class,"photo");
				
				//查询是否有这张图片
				Query query_id = new Query(Criteria.where("userID").is(userId));
				User_Bean user_Bean = mongoTemplate.findOne(query_id, User_Bean.class,"mvc");
				
				if (picture_Bean!=null) {
						if (user_Bean!=null) {
							
							//插入评论
							mongoTemplate.insert(comments_Bean, "comment");
							
							System.out.println("评论插入成功！");
							
							List<Comments_Bean>comments_Beans =new ArrayList<>();
							comments_Beans.add(comments_Bean);
							
							Msg_bean<Comments_Bean>msg_bean =new Msg_bean<>("20", "评论发表成功！",comments_Beans);
							return msg_bean;
							
						} else {
							Msg_bean msg_bean =new Msg_bean("12", "用户不存在！",null);
							return msg_bean;
						}
				} else {
					Msg_bean msg_bean =new Msg_bean("17", "图片不存在，重新填入图片信息！",null);
					return msg_bean;
				}
				

	}
	
	@ResponseBody
    @RequestMapping("/find_comment")
	public Msg_bean<Comments_Bean> find_submit(String picture_Name) {
        Query query =new Query(Criteria.where("picture_Name").is(picture_Name));
		List<Comments_Bean> find = mongoTemplate.find(query, Comments_Bean.class, "comment");
		System.out.println("评论"+picture_Name);
		if (find.size()!=0) {
			Msg_bean<Comments_Bean>msg_bean =new Msg_bean<>("21", "评论查找成功！",find);
			return msg_bean;
		} else {
			Msg_bean<Comments_Bean>msg_bean =new Msg_bean<>("23", "评论信息不存在！",null);
			return msg_bean;
		}

	}
	
	
	@ResponseBody
    @RequestMapping("/find_comment_withUsername")
	public Msg_bean<CommentsWithUserName_Bean> find_submit_withUserName(String picture_Name) {
        Query query =new Query(Criteria.where("picture_Name").is(picture_Name));
		List<Comments_Bean> find = mongoTemplate.find(query, Comments_Bean.class, "comment");
		List<CommentsWithUserName_Bean>commentsWithUserName_Beans =new ArrayList<>();
		
		if (find.size()!=0) {
			for (Comments_Bean comments_Bean : find) {
				String userid=comments_Bean.getReviewer_ID();
				 Query query_id =new Query(Criteria.where("userID").is(userid));
				 User_Bean user_Bean= mongoTemplate.findOne(query_id, User_Bean.class,"mvc");
				 if (user_Bean!=null) {
					String userName=user_Bean.getUserName();
					String userID =userid;
					
					URL userURL= oss_service.get_oss_url(user_Bean.getUserImage_Name());
					/*URL userURL=user_Bean.getUserImage_URL();*/
					commentsWithUserName_Beans.add(new CommentsWithUserName_Bean(comments_Bean.getPicture_Name(), comments_Bean.getComment(),
							comments_Bean.getReviewer_ID(), comments_Bean.getPublishTime(), userName, userid, userURL));
				} else {
					Msg_bean<CommentsWithUserName_Bean>msg_bean =new Msg_bean<>("29", "用户不存在导致评论查询失败！",null);
					return msg_bean;
				}
				
			}
			
			Msg_bean<CommentsWithUserName_Bean>msg_bean =new Msg_bean<>("30", "评论查找成功！（带userName）",commentsWithUserName_Beans);
			return msg_bean;
		} else {
			Msg_bean<CommentsWithUserName_Bean>msg_bean =new Msg_bean<>("23", "评论信息不存在！",null);
			return msg_bean;
		}

	}
	
	
/*	
	@ResponseBody
	@RequestMapping("/del_by_key")
	public Msg_bean del_submit(HttpServletRequest request,HttpServletResponse response) {

	    System.out.println(userId);
        Query query =new Query(Criteria.where("userID").is(userId));
        mongoTemplate.findAndRemove(query, User_Bean.class, "mvc");
		Msg_bean msg_bean =new Msg_bean("16", "用户删除成功！",null);
		return msg_bean;
	}*/
	
	
	
	/*查询职位基本信息控制器*/
	@RequestMapping("/del_by_key")
	@ResponseBody
	public Msg_bean delect_user_by_key(String reviewer_ID,String picture_Name,String publishTime){
		    System.out.println(reviewer_ID+picture_Name+publishTime);
			Comments_Bean comments_Bean =new Comments_Bean(picture_Name, null, reviewer_ID, publishTime);
		
            Query query =new Query(Criteria.byExample(comments_Bean));
            if (query!=null) {
            	mongoTemplate.findAndRemove(query, Comments_Bean.class, "comment");
    			Msg_bean msg_bean =new Msg_bean("22", "评论删除成功！",null);
    			return msg_bean;
			} else {
    			Msg_bean msg_bean =new Msg_bean("23", "评论信息不存在！",null);
    			return msg_bean;
			}
	}
	
	
	/*查询职位基本信息控制器*/
	@RequestMapping("/del_by_keys")
	@ResponseBody
	public Msg_bean delect_user_by_keys(String userIds){
		System.out.println(userIds);
	     String[] arr = userIds.split("-"); 
		for(String userId : arr){
			System.err.println(userId);
            Query query =new Query(Criteria.where("userID").is(userId));
            mongoTemplate.findAndRemove(query, User_Bean.class, "mvc");
		}
		Msg_bean msg_bean =new Msg_bean("16", "用户删除成功！",null);
		return msg_bean;
	}
	
	

}
