package com.lingber.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingber.bean.Msg_bean;
import com.lingber.bean.PictureWithUsername_Bean;
import com.lingber.bean.Picture_Bean;
import com.lingber.bean.User_Bean;
import com.lingber.bean.picture_content_bean;
import com.lingber.service.oss_service;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月7日 下午4:31:12 
* 类说明 : 图片管理控制器
*/

@RequestMapping("/picture") 
@Controller
public class Picture_Controller {
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private oss_service oss_service;
	
	
	//查询所有图片信息，服务器用
	@ResponseBody
    @RequestMapping("getall")
    public Msg_bean<Picture_Bean>server_getall_picture(@RequestParam(value="pn",defaultValue="1")Integer pn,String picture_Class)
    {
    	List<PictureWithUsername_Bean>pictureWithUsername_Beans = new ArrayList<>();
    	    //引入pageHelper插件
    		PageHelper.startPage(pn, 10);
    	    //startPage后紧跟的查询就是分页查询
    		Query query =new Query(Criteria.where("picture_Class").is(picture_Class));
    		List<Picture_Bean> find = mongoTemplate.find(query, Picture_Bean.class, "photo");
    		for (Picture_Bean picture_Bean : find) {
    			//查找用户名
    			Query query_username =new Query(Criteria.where("userID").is(picture_Bean.getUserID()));
    			System.out.println("1");
    			User_Bean find_userName = mongoTemplate.findOne(query_username, User_Bean.class, "mvc");
    			if (find_userName!=null) {
        			String userName=find_userName.getUserName();
        			URL userImageUrl= find_userName.getUserImage_URL();
        			PictureWithUsername_Bean pictureWithUsername_Bean = new PictureWithUsername_Bean
        					(userName, picture_Bean.getUserID(), picture_Bean.getPicture_Name(),
        					picture_Bean.getLocation(), picture_Bean.getContent(), picture_Bean.getShootingTime(),
        					picture_Bean.getUrl(), picture_Bean.getLocationName(), picture_Bean.getIsRecognition(),
        					picture_Bean.getPicture_Class(), userImageUrl);
        			System.out.println("2");
        			pictureWithUsername_Beans.add(pictureWithUsername_Bean);
				}
			}
    		System.out.println("查找成功!！");
    	    //使用PageInfo包装查询后的结果
    		//PageInfo封装了详细的分页信息包括查询出的数据，只需将pageInfo交给页面即可
    		//参数5表示连续显示5页
    		PageInfo page = new PageInfo(pictureWithUsername_Beans,10);
    		//JSONObject jsonObject = new JSONObject(page);
    		return Msg_bean.success().add("pageInfo",page);
			

    }
  
	/*根据关键字查询图片*/
	@RequestMapping("/get_by_key_with_content")
	@ResponseBody
	public Msg_bean get_by_key_with_content(String content){
		    //引入pageHelper插件
			PageHelper.startPage(1, 10);
		    //startPage后紧跟的查询就是分页查询
            Query query =new Query(Criteria.where("content.content_Class").is(content));
    		List<Picture_Bean> find = mongoTemplate.find(query, Picture_Bean.class, "photo");
		    //使用PageInfo包装查询后的结果
			//PageInfo封装了详细的分页信息包括查询出的数据，只需将pageInfo交给页面即可
			//参数5表示连续显示5页
			PageInfo page = new PageInfo(find,10);
			
    		if(find.size()!=0){
    			return Msg_bean.success().add("pageInfo", page);
    		}else{
    			Msg_bean msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
    			return msg_bean;
    		}
	}
    
	
	/*根据关键字查询图片*/
	@RequestMapping("/get_by_key_with_userName")
	@ResponseBody
	public Msg_bean get_by_key_with_userName(String userName){
		    //引入pageHelper插件
			PageHelper.startPage(1, 10);
		    //startPage后紧跟的查询就是分页查询
			List<Picture_Bean>picture_Beans = new ArrayList<>();
			List<PictureWithUsername_Bean>pictureWithUsername_Beans = new ArrayList<>();
            Query query_userID =new Query(Criteria.where("userName").is(userName));
            List<User_Bean>	user_Beans=	mongoTemplate.find(query_userID, User_Bean.class, "mvc");
            if (user_Beans.size()!=0) {
                for (User_Bean user_Bean : user_Beans) {
                	String userID= user_Bean.getUserID();
                	Criteria criteria = new Criteria();
                	criteria.andOperator(Criteria.where("userID").is(userID),Criteria.where("picture_Class").is("02"));
                	Query query_pic =new Query(criteria);
                	picture_Beans=mongoTemplate.find(query_pic, Picture_Bean.class, "photo");
    			}
				
			}

			
    		if(picture_Beans.size()!=0){
    			//转换成带userName的
    			for (Picture_Bean picture_Bean : picture_Beans) {
					PictureWithUsername_Bean pictureWithUsername_Bean  = new PictureWithUsername_Bean
					(userName, picture_Bean.getUserID(), picture_Bean.getPicture_Name(), picture_Bean.getLocation(), picture_Bean.getContent(),
							picture_Bean.getShootingTime(), picture_Bean.getUrl(), picture_Bean.getLocationName(), picture_Bean.getIsRecognition(),
							picture_Bean.getPicture_Class(), null);
    				pictureWithUsername_Beans.add(pictureWithUsername_Bean);
				}
    		    //使用PageInfo包装查询后的结果
    			//PageInfo封装了详细的分页信息包括查询出的数据，只需将pageInfo交给页面即可
    			//参数5表示连续显示5页
    			PageInfo page = new PageInfo(pictureWithUsername_Beans,10);
    			return Msg_bean.success().add("pageInfo", page);
    		}else{
    			Msg_bean msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
    			return msg_bean;
    		}
	}

	/*查询图片基本信息控制器*/
	@RequestMapping("/client_get_by_key")
	@ResponseBody
	public Msg_bean<Picture_Bean> getuser_by_key_client(String picture_name){
			System.out.println(picture_name);
            Query query =new Query(Criteria.where("picture_Name").is(picture_name));
            Picture_Bean find = mongoTemplate.findOne(query, Picture_Bean.class, "photo");
            
            //生成URL
			 URL url =oss_service.get_oss_url(picture_name);
			 find.setUrl(url);
            
            System.out.println(find.getPicture_Name());
    		List<Picture_Bean>picture_Beans = new ArrayList<>();
    		picture_Beans.add(find);
    		
    		if(picture_Beans.size()!=0){
    			Msg_bean<Picture_Bean> msg_bean = new Msg_bean("15","图片查找成功！",picture_Beans);
    			System.out.println("查找成功");
    			return msg_bean;
    		}else{
    			Msg_bean<Picture_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
    			System.out.println("查找失败");
    			return msg_bean;
    		}
	}
	
	/*根据图片名查询图片*/
	@RequestMapping("/client_get_by_picture_name")
	@ResponseBody
	public Msg_bean<PictureWithUsername_Bean> getuserWithName_by_key_client(String picture_name){
			System.out.println(picture_name);
            Query query =new Query(Criteria.where("picture_Name").is(picture_name));
            System.out.println("1");
            Picture_Bean find = mongoTemplate.findOne(query, Picture_Bean.class, "photo");
            System.out.println("2");
            if (find!=null) {
            	System.out.println("进");
             //生成URL
   			 URL url =oss_service.get_oss_url(picture_name);
   			 find.setUrl(url);
   			 String userID =find.getUserID();
               System.out.println(userID);
             System.out.println(find.getPicture_Name());
       		 List<PictureWithUsername_Bean>pictureWithUsername_Beans =new ArrayList<>();
       		
   			 Query query_withUserName =new Query(Criteria.where("userID").is(userID));
   			 User_Bean user_Bean= mongoTemplate.findOne(query_withUserName, User_Bean.class, "mvc");
   			 if (user_Bean!=null) {
   	   			 String userName=user_Bean.getUserName();
   	   			 URL userImage_URL=oss_service.get_oss_url(user_Bean.getUserImage_Name());
   	   			 System.out.println(user_Bean.getUserID());
   	   			 System.out.println(user_Bean.getUserImage_Name());
   	   			 System.out.println(userImage_URL);
/*   	   			 pictureWithUsername_Beans.add(new PictureWithUsername_Bean(userID, find.getPicture_Name(), find.getLocation(), find.getContent(),
   	   					 find.getShootingTime(), url, userName));*/
   	   			 pictureWithUsername_Beans.add(new PictureWithUsername_Bean(userName, userID, find.getPicture_Name(), find.getLocation(), find.getContent(),
   	   					 find.getShootingTime(), url,find.getLocationName(),find.getIsRecognition(),find.getPicture_Class(),userImage_URL));
			}else{
       			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("28", "图片查询失败，用户ID错误！",null);
       			System.out.println("查找失败");
       			return msg_bean;
			}

       		if(pictureWithUsername_Beans.size()!=0){
       			Msg_bean<PictureWithUsername_Bean> msg_bean = new Msg_bean("27","带用户名的图片信息查找成功！",pictureWithUsername_Beans);
       			System.out.println("查找成功");
       			return msg_bean;
       		}else{
       			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
       			System.out.println("查找失败");
       			return msg_bean;
       		}
				
		} else {
			System.out.println("出");
   			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
   			System.out.println("查找失败");
   			return msg_bean;
		}

	}
	
	
	/*根据用户ID查询图片*/
	@RequestMapping("/client_get_by_userID")
	@ResponseBody
	public Msg_bean<PictureWithUsername_Bean> getuserWithName_by_ID_client(String userId){
			System.out.println(userId);
			Criteria criteria = new Criteria();
			criteria.andOperator(Criteria.where("picture_Class").is("01"),Criteria.where("userID").is(userId));
            Query query =new Query(criteria);
            List<Picture_Bean> finds = mongoTemplate.find(query, Picture_Bean.class, "photo");
            List<PictureWithUsername_Bean>pictureWithUsername_Beans =new ArrayList<>();
            if (finds.size()!=0) {
             for (Picture_Bean find : finds) {
                 //生成URL
       			 URL url =oss_service.get_oss_url(find.getPicture_Name());
       			 find.setUrl(url);
       			 String userID =find.getUserID();
                  System.out.println("用户ID"+userID); 
                 System.out.println(find.getPicture_Name());
           		
       			 Query query_withUserName =new Query(Criteria.where("userID").is(userID));
       			 User_Bean user_Bean= mongoTemplate.findOne(query_withUserName, User_Bean.class, "mvc");
       			 if (user_Bean!=null) {
       	   			 String userName=user_Bean.getUserName();
       	   	      	URL userImage_URL=oss_service.get_oss_url(user_Bean.getUserImage_Name());
       	   			 pictureWithUsername_Beans.add(new PictureWithUsername_Bean(userName, userID, find.getPicture_Name(), find.getLocation(), find.getContent(),
       	   					 find.getShootingTime(), url,find.getLocationName(),find.getIsRecognition(),find.getPicture_Class(),userImage_URL));
    			}else{
           			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("28", "图片查询失败，用户不存在:"+userID,null);
           			System.out.println("查找失败");
           			return msg_bean;
    			}
				
			}
       		if(pictureWithUsername_Beans.size()!=0){
       			Msg_bean<PictureWithUsername_Bean> msg_bean = new Msg_bean("27","带用户名的图片信息查找成功！",pictureWithUsername_Beans);
       			System.out.println("查找成功");
       			return msg_bean;
       		}else{
       			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
       			System.out.println("查找失败");
       			return msg_bean;
       		}
				
		} else {
   			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
   			System.out.println("查找失败");
   			return msg_bean;
		}

	}
	
	
	/*查询是否识别的图片*/
	@RequestMapping("/getPicture_by_isRecognition")
	@ResponseBody
	public Msg_bean<PictureWithUsername_Bean> getPicture_by_isRecognition(String isRecognition){
			System.out.println(isRecognition);
			Criteria criteria =new Criteria();
			criteria.andOperator(Criteria.where("isRecognition").is(isRecognition),Criteria.where("picture_Class").is("01"));
            Query query =new Query(criteria);
            List<Picture_Bean> finds = mongoTemplate.find(query, Picture_Bean.class, "photo");
            List<PictureWithUsername_Bean>pictureWithUsername_Beans =new ArrayList<>();
            if (finds.size()!=0) {
             for (Picture_Bean find : finds) {
                 //生成URL
       			 URL url =oss_service.get_oss_url(find.getPicture_Name());
       			 find.setUrl(url);
       			 String userID =find.getUserID();
                 System.out.println(find.getPicture_Name());
           		
       			 Query query_withUserName =new Query(Criteria.where("userID").is(userID));
       			 User_Bean user_Bean= mongoTemplate.findOne(query_withUserName, User_Bean.class, "mvc");
       			 if (user_Bean!=null) {
       	   			 String userName=user_Bean.getUserName();
       	   			 URL userImage_URL=oss_service.get_oss_url(user_Bean.getUserImage_Name());
       	   			 pictureWithUsername_Beans.add(new PictureWithUsername_Bean(userName, userID, find.getPicture_Name(), find.getLocation(), find.getContent(),
       	   					 find.getShootingTime(), url,find.getLocationName(),find.getIsRecognition(),find.getPicture_Class(),userImage_URL));
    			}else{
           			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("28", "图片查询失败,用户不存在:"+userID,null);
           			System.out.println("查找失败");
           			return msg_bean;
    			}
				
			}
       		if(pictureWithUsername_Beans.size()!=0){
       			Msg_bean<PictureWithUsername_Bean> msg_bean = new Msg_bean("27","图片信息查找成功！",pictureWithUsername_Beans);
       			System.out.println("查找成功");
       			return msg_bean;
       		}else{
       			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
       			System.out.println("查找失败");
       			return msg_bean;
       		}
				
		} else {
   			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
   			System.out.println("查找失败");
   			return msg_bean;
		}

	}
	
	
	/*查询头像图片*/
	@RequestMapping("/getNow_userImage")
	@ResponseBody
	public Msg_bean<PictureWithUsername_Bean> getNow_userImage(String userId){
		 	   System.out.println("11:"+userId);
			
			    Query query_image_user =new Query(Criteria.where("userID").is(userId));
			    User_Bean user_Bean_find_image= mongoTemplate.findOne(query_image_user, User_Bean.class, "mvc");
			    String Image_Name = user_Bean_find_image.getUserImage_Name();
			    System.out.println(Image_Name);
			    
			    Query query_image =new Query(Criteria.where("picture_Name").is(Image_Name));
	            Picture_Bean find = mongoTemplate.findOne(query_image, Picture_Bean.class, "photo");
	            
	            List<PictureWithUsername_Bean>pictureWithUsername_Beans =new ArrayList<>();
	            if (find!=null) {
            	
            	
                 //生成URL
       			 URL url =oss_service.get_oss_url(find.getPicture_Name());
       			 find.setUrl(url);
       			 String userID =find.getUserID();
                   
                 System.out.println(find.getPicture_Name());
           		
       			 Query query_withUserName =new Query(Criteria.where("userID").is(userID));
       			 User_Bean user_Bean= mongoTemplate.findOne(query_withUserName, User_Bean.class, "mvc");
       			 if (user_Bean!=null) {
       	   			 String userName=user_Bean.getUserName();
       	   			 URL userImage_URL=oss_service.get_oss_url(user_Bean.getUserImage_Name());
       	   			 pictureWithUsername_Beans.add(new PictureWithUsername_Bean(userName, userID, find.getPicture_Name(), find.getLocation(), find.getContent(),
       	   					 find.getShootingTime(), url,find.getLocationName(),find.getIsRecognition(),find.getPicture_Class(),userImage_URL));
    			}else{
           			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("28", "图片查询失败，用户ID错误！",null);
           			System.out.println("查找失败");
           			return msg_bean;
    			}
				
			
       		if(pictureWithUsername_Beans.size()!=0){
       			Msg_bean<PictureWithUsername_Bean> msg_bean = new Msg_bean("27","带用户名的图片信息查找成功！",pictureWithUsername_Beans);
       			System.out.println("查找成功");
       			return msg_bean;
       		}else{
       			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
       			System.out.println("查找失败");
       			return msg_bean;
       		}
				
		} else {
   			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
   			System.out.println("查找失败");
   			return msg_bean;
		}

	}
	
	
	/*查询历史头像图片*/
	@RequestMapping("/getAll_userImage")
	@ResponseBody
	public Msg_bean<PictureWithUsername_Bean> getPicture_by_picClass(String userId){
			System.out.println("11:"+userId);
			Criteria criatira = new Criteria();
			criatira.andOperator(Criteria.where("picture_Class").is("02"),Criteria.where("userID").is(userId));
            Query query =new Query(criatira);
            List<Picture_Bean> finds = mongoTemplate.find(query, Picture_Bean.class, "photo");
            List<PictureWithUsername_Bean>pictureWithUsername_Beans =new ArrayList<>();
            if (finds.size()!=0) {
             for (Picture_Bean find : finds) {
                 //生成URL
       			 URL url =oss_service.get_oss_url(find.getPicture_Name());
       			 find.setUrl(url);
       			 String userID =find.getUserID();
                   
                 System.out.println(find.getPicture_Name());
           		
       			 Query query_withUserName =new Query(Criteria.where("userID").is(userID));
       			 User_Bean user_Bean= mongoTemplate.findOne(query_withUserName, User_Bean.class, "mvc");
       			 if (user_Bean!=null) {
       	   			 String userName=user_Bean.getUserName();
       	   			 URL userImage_URL=oss_service.get_oss_url(user_Bean.getUserImage_Name());
       	   			 pictureWithUsername_Beans.add(new PictureWithUsername_Bean(userName, userID, find.getPicture_Name(), find.getLocation(), find.getContent(),
       	   					 find.getShootingTime(), url,find.getLocationName(),find.getIsRecognition(),find.getPicture_Class(),userImage_URL));
    			}else{
           			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("28", "图片查询失败，用户ID错误！",null);
           			System.out.println("查找失败");
           			return msg_bean;
    			}
				
			}
       		if(pictureWithUsername_Beans.size()!=0){
       			Msg_bean<PictureWithUsername_Bean> msg_bean = new Msg_bean("27","带用户名的图片信息查找成功！",pictureWithUsername_Beans);
       			System.out.println("查找成功");
       			return msg_bean;
       		}else{
       			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
       			System.out.println("查找失败");
       			return msg_bean;
       		}
				
		} else {
   			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("17", "图片不存在，查找失败！",null);
   			System.out.println("查找失败");
   			return msg_bean;
		}

	}
	

	
	
	//服务器更新图片信息接口
    @ResponseBody
    @RequestMapping("/server_updata")
    public Msg_bean MongoJDBC_updata_server(String picture_name,String userId_update,String content_updata) {
    		System.out.println(picture_name);
    	
    		   // Update
    		Query query =new Query(Criteria.where("picture_Name").is(picture_name));
    		
    		Picture_Bean find = mongoTemplate.findOne(query, Picture_Bean.class, "photo");

    		Update update =new Update();
    		mongoTemplate.upsert(query,update.update("content", content_updata),Picture_Bean.class,"photo");
    		mongoTemplate.upsert(query,update.update("userID", userId_update),Picture_Bean.class,"photo");
    	    System.out.println("修改成功！");
			Msg_bean msg_bean =new Msg_bean("18", "图片修改成功！",null);
			return msg_bean;
	}
	
    //客户端更新图片信息接口
    @ResponseBody
    @RequestMapping("/client_updata")
    public Msg_bean MongoJDBC_updata_client(String picture_name,String content_updata) {
    		System.out.println(picture_name);
    	
    		   // Update
    		Query query =new Query(Criteria.where("picture_Name").is(picture_name));
    		
    		Picture_Bean find = mongoTemplate.findOne(query, Picture_Bean.class, "photo");

    		Update update =new Update();
    		mongoTemplate.upsert(query,update.update("content", content_updata),Picture_Bean.class,"photo");
    		
    		Query query_new =new Query(Criteria.where("picture_Name").is(picture_name));
    		
    		Picture_Bean find_new = mongoTemplate.findOne(query_new, Picture_Bean.class, "photo");
    		List<Picture_Bean>picture_Beans =new ArrayList<>();
    		picture_Beans.add(find_new);
    		
    	    System.out.println("修改成功！");
			Msg_bean msg_bean =new Msg_bean("18", "图片修改成功！",picture_Beans);
			return msg_bean;
	}
    
    //客户端更新图片信息接口
    @ResponseBody
    @RequestMapping("/set_isReconition")
    public Msg_bean set_isReconition(String picture_name,String isReconition) {
    		System.out.println(picture_name);
    		System.out.println(isReconition);
    		   // Update
    		Query query =new Query(Criteria.where("picture_Name").is(picture_name));
    		Picture_Bean find = mongoTemplate.findOne(query, Picture_Bean.class, "photo");

    		if (find!=null) {//如果picture_name不为空
				if (isReconition!=null) {
					if (isReconition.equals("false")) {//如果用户认为图片识别错误
			    		Update update =new Update();
			    		mongoTemplate.upsert(query,update.update("isRecognition", "false"),Picture_Bean.class,"photo");
					}
				}
			}else {
				Msg_bean msg_bean =new Msg_bean("18", "图片不存在！",null);
				return msg_bean;
			}

    		
    		Query query_new =new Query(Criteria.where("picture_Name").is(picture_name));
    		Picture_Bean find_new = mongoTemplate.findOne(query_new, Picture_Bean.class, "photo");
    		URL pic_url= oss_service.get_oss_url(find_new.getPicture_Name());
    		find_new.setUrl(pic_url);
    		List<Picture_Bean>picture_Beans =new ArrayList<>();
    		picture_Beans.add(find_new);
    		
    	    System.out.println("修改成功！");
			Msg_bean msg_bean =new Msg_bean("18", "图片修改成功！",picture_Beans);
			return msg_bean;
	}
    
    
    
    
	/*根据图片名删图片*/
	@RequestMapping("/del_by_key")
	@ResponseBody
	public Msg_bean delect_user_by_key(String picture_name){
		
			oss_service.del_oss(picture_name);
			
		    System.out.println(picture_name);
            Query query =new Query(Criteria.where("picture_Name").is(picture_name));
            mongoTemplate.findAndRemove(query, Picture_Bean.class, "photo");
			Msg_bean msg_bean =new Msg_bean("19", "图片删除成功！",null);
			return msg_bean;
	}
	
	
	/*删除所有改用户传的图片*/
	@RequestMapping("/del_user_all")
	@ResponseBody
	public Msg_bean delect_user_by_keys(String userId){
		System.out.println(userId);
            Query query =new Query(Criteria.where("userID").is(userId));
            mongoTemplate.findAndRemove(query, Picture_Bean.class, "photo");
		Msg_bean msg_bean =new Msg_bean("19", "图片批量删除成功！",null);
		return msg_bean;
	}
}
