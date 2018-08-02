package com.lingber.controller;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.aliyuncs.exceptions.ClientException;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingber.bean.CountUser_Bean;
import com.lingber.bean.Msg_bean;
import com.lingber.bean.Picture_Bean;
import com.lingber.bean.User_Bean;
import com.lingber.service.SMS_Radom_tool;
import com.lingber.service.UserID_Radom_tool;
import com.lingber.service.User_Service;
import com.lingber.service.oss_service;
import com.mongodb.MongoClient;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;


/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年3月25日 下午4:32:24 
* 类说明 :用户管理控制器
*/

@RequestMapping("/user") 
@Controller
public class User_Controller {
	
	@Autowired
	private User_Service user_Service;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private oss_service oss_service;
	
	
	private String SMS_Code = "x";
	
	private static final Log log = LogFactory.getLog(User_Controller.class);
	 
	
	/*查询所有用户信息*/
	@RequestMapping("/getall")
	@ResponseBody
	public Msg_bean getRoughWithJson_computer(@RequestParam(value="pn",defaultValue="1")Integer pn){
		    //引入pageHelper插件
			PageHelper.startPage(pn, 10);
		    //startPage后紧跟的查询就是分页查询
    		List<User_Bean> find = mongoTemplate.findAll(User_Bean.class, "mvc");
    		System.out.println("查找成功！");
		    //使用PageInfo包装查询后的结果
			//PageInfo封装了详细的分页信息包括查询出的数据，只需将pageInfo交给页面即可
			//参数5表示连续显示5页
			PageInfo page = new PageInfo(find,10);
			//JSONObject jsonObject = new JSONObject(page);
			return Msg_bean.success().add("pageInfo",page);
	}
	
	/*查询职位基本信息控制器*/
	@RequestMapping("/get_by_key")
	@ResponseBody
	public Msg_bean getRoughWithJson_computer_by_key(String userId){
		    //引入pageHelper插件
			PageHelper.startPage(1, 10);
		    //startPage后紧跟的查询就是分页查询
            Query query =new Query(Criteria.where("userID").is(userId));
    		List<User_Bean> find = mongoTemplate.find(query, User_Bean.class, "mvc");
		    //使用PageInfo包装查询后的结果
			//PageInfo封装了详细的分页信息包括查询出的数据，只需将pageInfo交给页面即可
			//参数5表示连续显示5页
			PageInfo page = new PageInfo(find,10);
			
    		if(find.size()!=0){
    			return Msg_bean.success().add("pageInfo", page);
    		}else{
    			Msg_bean msg_bean =new Msg_bean("12", "用户不存在!",null);
    			return msg_bean;
    		}
	}
	
	/*查询职位基本信息控制器*/
	@RequestMapping("/get_by_key_withName")
	@ResponseBody
	public Msg_bean get_by_key_withName(String userName){
		    //引入pageHelper插件
			PageHelper.startPage(1, 10);
		    //startPage后紧跟的查询就是分页查询
            Query query =new Query(Criteria.where("userName").is(userName));
    		List<User_Bean> find = mongoTemplate.find(query, User_Bean.class, "mvc");
		    //使用PageInfo包装查询后的结果
			//PageInfo封装了详细的分页信息包括查询出的数据，只需将pageInfo交给页面即可
			//参数5表示连续显示5页
			PageInfo page = new PageInfo(find,10);
			
    		if(find.size()!=0){
    			return Msg_bean.success().add("pageInfo", page);
    		}else{
    			Msg_bean msg_bean =new Msg_bean("12", "用户不存在!",null);
    			return msg_bean;
    		}
	}

	/*查询职位基本信息控制器*/
	@RequestMapping("/client_get_by_key")
	@ResponseBody
	public Msg_bean<User_Bean> getuser_by_key_client(String userId){
			System.out.println(userId);
            Query query =new Query(Criteria.where("userID").is(userId));
    		User_Bean find = mongoTemplate.findOne(query, User_Bean.class, "mvc");
    		List<User_Bean>user_Beans = new ArrayList<>();
    		if(find!=null){
        		URL url= oss_service.get_oss_url(find.getUserImage_Name());
        		find.setUserImage_URL(url);
        		find.setPassWord(null);
        		user_Beans.add(find);
    			Msg_bean<User_Bean> msg_bean = new Msg_bean<>("13","用户查找成功",user_Beans);
    			System.out.println("查找成功");
    			return msg_bean;
    		}else{
    			Msg_bean<User_Bean> msg_bean = new Msg_bean<>("12","用户不存在！",null);
    			System.out.println("查找失败");
    			return msg_bean;
    		}
	}
	
	
	/*查询职位基本信息控制器*/
	@RequestMapping("/client_get_username_by_key")
	@ResponseBody
	public Msg_bean<User_Bean> getusername_by_key_client(String userId){
			System.out.println(userId);
            Query query =new Query(Criteria.where("userID").is(userId));
    		User_Bean find = mongoTemplate.findOne(query, User_Bean.class, "mvc");
    		List<User_Bean>user_Beans = new ArrayList<>();
    		user_Beans.add(find);
    		if(find!=null){
        		find.setPhone_Numb(null);
        		find.setPassWord(null);
        		URL url= oss_service.get_oss_url(find.getUserImage_Name());
        		find.setUserImage_URL(url);
    			Msg_bean<User_Bean> msg_bean = new Msg_bean<>("26","用户查找成功（返回用户名以及id）",user_Beans);
    			System.out.println("查找成功");
    			return msg_bean;
    		}else{
    			Msg_bean<User_Bean> msg_bean = new Msg_bean<>("12","用户不存在！",null);
    			System.out.println("查找失败");
    			return msg_bean;
    		}
	}
	
 
    @ResponseBody
    @RequestMapping("/server_updata")
    public Msg_bean MongoJDBC_updata_server(String userId,String userName_update,String password_updata,String phone_Numb_updata) {
    		System.out.println(userId+userName_update+password_updata+phone_Numb_updata);
    	
    		   // Update
    		Query query =new Query(Criteria.where("userID").is(userId));
    		
    		User_Bean find = mongoTemplate.findOne(query, User_Bean.class, "mvc");
    		System.out.println(find.getPhone_Numb());
    		
    		if(find.getPhone_Numb().equals(phone_Numb_updata)){
    			//如果修改后的电话号码和之前的一样
    			System.out.println("修改后的电话号码和之前的一样");
    		}else {
    			System.out.println("二");
        		Query query_phone_numb =new Query(Criteria.where("phone_Numb").is(phone_Numb_updata));
        		User_Bean find_phone_numb = mongoTemplate.findOne(query_phone_numb, User_Bean.class, "mvc");
        		System.out.println("三");
        		if (find_phone_numb!=null) {
        			//如果经过修改的电话号码依旧存在
        			Msg_bean msg_bean =new Msg_bean("03", "用户手机号已经存在!",null);
        			return msg_bean;
				}else{
					//如果不存在，则可以修改
					System.out.println("该手机号可以使用");
				}
			}
    		System.out.println("四");
    		Update update =new Update();
    		mongoTemplate.upsert(query,update.update("userName", userName_update),User_Bean.class,"mvc");
    		mongoTemplate.upsert(query,update.update("passWord", password_updata),User_Bean.class,"mvc");
    		mongoTemplate.upsert(query,update.update("phone_Numb", phone_Numb_updata),User_Bean.class,"mvc");
    	    System.out.println("修改成功！");
			Msg_bean msg_bean =new Msg_bean("14", "用户修改成功！",null);
			return msg_bean;
	}
	
	/*查询职位基本信息控制器*/
	@RequestMapping("/del_by_key")
	@ResponseBody
	public Msg_bean delect_user_by_key(String userId){
		    System.out.println(userId);
            Query query =new Query(Criteria.where("userID").is(userId));
            mongoTemplate.findAndRemove(query, User_Bean.class, "mvc");
			Msg_bean msg_bean =new Msg_bean("16", "用户删除成功！",null);
			return msg_bean;
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
	
	
	
    @ResponseBody
    @RequestMapping("/userInfo_updata")
    public Msg_bean userInfo_updata(HttpServletRequest request,String userId,String userName_update,String password_updata,
    		String phone_Numb_updata,String address_update,String sex_update)
    		throws ClientException, IllegalStateException, IOException{
    	    System.out.println("1");
    		// Update
    		Query query =new Query(Criteria.where("userID").is(userId));
    		
    		User_Bean find = mongoTemplate.findOne(query, User_Bean.class, "mvc");
    		if (find==null) {
    			Msg_bean msg_bean =new Msg_bean("12", "用户不存在！",null);
    			return msg_bean;
			}
    		if(find.getPhone_Numb().equals(phone_Numb_updata)){
    			//如果修改后的电话号码和之前的一样
    			System.out.println("修改后的电话号码和之前的一样");
    		}else {
    			System.out.println("二");
        		Query query_phone_numb =new Query(Criteria.where("phone_Numb").is(phone_Numb_updata));
        		User_Bean find_phone_numb = mongoTemplate.findOne(query_phone_numb, User_Bean.class, "mvc");
        		System.out.println("三");
        		if (find_phone_numb!=null) {
        			//如果经过修改的电话号码依旧存在
        			Msg_bean msg_bean =new Msg_bean("03", "用户手机号已经存在!",null);
        			return msg_bean;
				}else{
					//如果不存在，则可以修改
					System.out.println("该手机号可以使用");
				}
			}
    		
    		 URL userImage_URL=null;
    		 String Pic_name=null;
    		
    		//图片上传类
    		   long  startTime=System.currentTimeMillis();

    		   List<Picture_Bean> picture_Beans = new ArrayList();
    	        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
    	       CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
    	               request.getSession().getServletContext());
    	       //检查form中是否有enctype="multipart/form-data"
    	       if(multipartResolver.isMultipart(request))
    	       {
    	    	  
    	           //将request变成多部分request
    	           MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
    	          //获取multiRequest 中所有的文件名
    	           Iterator iter=multiRequest.getFileNames();
    	           while(iter.hasNext())
    	           {
    	               //一次遍历所有文件
    	               MultipartFile file=multiRequest.getFile(iter.next().toString());
    	               if(file!=null)
    	               {	
    	            	   // 获取上传的文件保存的路径  
    	            	   String path =  request.getSession().getServletContext().getRealPath("upload_buffer");
    	            	   //获取文件名称
    	            	   String Oldfliename=file.getOriginalFilename();
    	            	   //获取后缀
    	            	   String prefix=Oldfliename.substring(Oldfliename.lastIndexOf(".")+1);
    	            	   
    	            	   //检测是否上传图片
    	            	   System.out.println("后缀:"+prefix);
    	            	   if (prefix.equals("")) {
    						System.out.println("为空");
    						break;
    					   }
    	            	   if (!(prefix.equals("jpg"))) {
    	            			Msg_bean<Picture_Bean> msg_bean =new Msg_bean("36", "图片格式错误！要求.jpg格式",null);
    	            			return msg_bean;
    	            	   }
    	            	   
    	    		       
    	            	   //生成新的名字
    	    		       UserID_Radom_tool userID_Radom_tool =new UserID_Radom_tool();
    	    		       
    	            	   String uuidfilename = userID_Radom_tool.randomFileName();

    	                   //检测文件名称  
    	                   while(true){  
    	        		       //新建查询
    	        		       Query query_pic =new Query(Criteria.where("picture_Name").is(uuidfilename + "." + prefix));
    	        		       Picture_Bean picture_Bean_findResult =mongoTemplate.findOne(query_pic, Picture_Bean.class, "photo");

    	                       if (picture_Bean_findResult == null) {  
    	                           break;  
    	                       }  
    	                       //否则，重新生成文件名称  
    	                       uuidfilename = userID_Radom_tool.randomFileName();  
    	                   } 

    	            	   //修改后完整的文件名称
    	            	   String NewFileName = uuidfilename + "." + prefix;
    	            	   //完整的路径
    	            	   String completepath = path +"\\"+ NewFileName;
    	            	   System.out.println(completepath);
    	    		       //重命名
    	    		       file.transferTo(new File(completepath));
    	    		       //将图片存入云数据库中
    	            	   oss_service.uploadFile_to_oss(completepath, NewFileName);
    	    		      
    	    		       //从session中获取用户名
    	    		       //本地获取上传时间
    	    		       //获取客户端传来的经纬度
    	    		       String userid = userId;
    	    		       System.out.println("强行转:"+userid);
    	    		       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    	    		       String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
    	    		       
    	                   //生成URL
    	    	   			URL url =oss_service.get_oss_url(NewFileName);
    	    	   			userImage_URL=url;
    	    	   			
    	    		       /*Picture_Bean picture_Bean= new Picture_Bean(userid, NewFileName, null, null, null, date, url, null, null, "02");*/
    	    	   			Picture_Bean picture_Bean= new Picture_Bean(userid, NewFileName, 0, 0, null, date, url, null, null, "02");

    	    	   			
    	    		        mongoTemplate.insert(picture_Bean, "photo");
    	    		        
    	    		        
    	    	    		Query query_user =new Query(Criteria.where("userID").is(userid));
    	    	    		User_Bean find_user = mongoTemplate.findOne(query_user, User_Bean.class, "mvc");
    	    	    		String userName=find_user.getUserName();
    			    		Update update =new Update();
    			    		mongoTemplate.upsert(query_user,update.update("userName", userName),Picture_Bean.class,"mvc");
    			    		mongoTemplate.upsert(query_user,update.update("userImage_URL", url),Picture_Bean.class,"mvc");
    			    		mongoTemplate.upsert(query_user,update.update("userImage_Name", NewFileName),Picture_Bean.class,"mvc");
    	    		       
    	    		       System.out.println("上传成功！");
    	    		       File file_del =new File(completepath);
    	    		       //从服务器的缓存中删除文件
    	    		       file_del.delete();
    	               }
    	           }
    	       }

    	       
    		
    		/*判断是否为空，为空*/
        		System.out.println("四");
        		Update update =new Update();
        		if (userName_update!=null) {
        		mongoTemplate.upsert(query,update.update("userName", userName_update),User_Bean.class,"mvc");
        		}
        		if (password_updata!=null) {
        		mongoTemplate.upsert(query,update.update("passWord", password_updata),User_Bean.class,"mvc");	
        		}
        		if (phone_Numb_updata!=null) {
        		mongoTemplate.upsert(query,update.update("phone_Numb", phone_Numb_updata),User_Bean.class,"mvc");	
        		}
        		if (address_update!=null) {
        		mongoTemplate.upsert(query,update.update("address", address_update),User_Bean.class,"mvc");
        		}
        		if (sex_update!=null) {
        		mongoTemplate.upsert(query,update.update("sex", sex_update),User_Bean.class,"mvc");
        		}
        		if (userImage_URL!=null) {
        		
        	    mongoTemplate.upsert(query,update.update("userImage_URL", userImage_URL),User_Bean.class,"mvc");
				}
        	    System.out.println("修改成功！");
        	    
        		Query query_new =new Query(Criteria.where("userID").is(userId));
        		User_Bean find_new = mongoTemplate.findOne(query_new, User_Bean.class, "mvc");
        		//设置url
        		System.out.println("图片名1："+find_new.getUserImage_Name());
        		URL url_pic=oss_service.get_oss_url(find_new.getUserImage_Name());
        		
        		find_new.setUserImage_URL(url_pic);
        		
        		List<User_Bean>user_Beans =new ArrayList<>();
        		user_Beans.add(find_new);
        	    
        		
        		
     	        long  endTime=System.currentTimeMillis();
     	        System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");
        		
    			Msg_bean msg_bean =new Msg_bean("14", "用户信息修改成功！",user_Beans);
    			return msg_bean;
	
    			
     } 
	
	
	
	
	
    @ResponseBody
    @RequestMapping("/client_updata")
    public Msg_bean client_user_updata(String userId,String userName_update,String password_updata,String phone_Numb_updata,String Code)
    		throws ClientException{
    	
    		// Update
    		Query query =new Query(Criteria.where("userID").is(userId));
    		
    		User_Bean find = mongoTemplate.findOne(query, User_Bean.class, "mvc");
    		
    		System.out.println(find.getPhone_Numb());
    		
    		if(find.getPhone_Numb().equals(phone_Numb_updata)){
    			//如果修改后的电话号码和之前的一样
    			System.out.println("修改后的电话号码和之前的一样");
    		}else {
    			System.out.println("二");
        		Query query_phone_numb =new Query(Criteria.where("phone_Numb").is(phone_Numb_updata));
        		User_Bean find_phone_numb = mongoTemplate.findOne(query_phone_numb, User_Bean.class, "mvc");
        		System.out.println("三");
        		if (find_phone_numb!=null) {
        			//如果经过修改的电话号码依旧存在
        			Msg_bean msg_bean =new Msg_bean("03", "用户手机号已经存在!",null);
        			return msg_bean;
				}else{
					//如果不存在，则可以修改
					System.out.println("该手机号可以使用");
				}
			}
    		
    		/*短信验证过程*/
    		if (this.SMS_Code.equals(Code)) {
        		System.out.println("四");
        		Update update =new Update();
        		mongoTemplate.upsert(query,update.update("userName", userName_update),User_Bean.class,"mvc");
        		mongoTemplate.upsert(query,update.update("passWord", password_updata),User_Bean.class,"mvc");
        		mongoTemplate.upsert(query,update.update("phone_Numb", phone_Numb_updata),User_Bean.class,"mvc");
        	    System.out.println("修改成功！");
        	    
        		Query query_new =new Query(Criteria.where("userID").is(userId));
        		User_Bean find_new = mongoTemplate.findOne(query, User_Bean.class, "mvc");
        		List<User_Bean>user_Beans =new ArrayList<>();
        		user_Beans.add(find_new);
        	    
    			Msg_bean msg_bean =new Msg_bean("14", "用户修改成功！",user_Beans);
    			return msg_bean;
				
			} else {
    			Msg_bean msg_bean =new Msg_bean("05", "注册失败！验证码不符！",null);
    			return msg_bean;
			}
    		/*短信验证过程结束*/
	}
    
    
    @ResponseBody
    @RequestMapping("/client_updata_get_code")
    public Msg_bean client_user_updata_get_code(String phone_Numb)
    		throws ClientException{
    	
    		// Update
    		Query query =new Query(Criteria.where("phone_Numb").is(phone_Numb));
    		
    		User_Bean find = mongoTemplate.findOne(query, User_Bean.class, "mvc");
    		
    		System.out.println(find.getPhone_Numb());
    		
    		if(find.getPhone_Numb().equals(phone_Numb)){
    			//如果修改后的电话号码和之前的一样
    			System.out.println("修改后的电话号码和之前的一样");
    		}else {
    			System.out.println("二");
        		Query query_phone_numb =new Query(Criteria.where("phone_Numb").is(phone_Numb));
        		User_Bean find_phone_numb = mongoTemplate.findOne(query_phone_numb, User_Bean.class, "mvc");
        		System.out.println("三");
        		if (find_phone_numb!=null) {
        			//如果经过修改的电话号码依旧存在
        			Msg_bean msg_bean =new Msg_bean("03", "用户手机号已经存在!",null);
        			return msg_bean;
				}else{
					//如果不存在，则可以修改
					System.out.println("该手机号可以使用");
				}
			}
    		
    		/*短信验证过程*/
	   		 SMS_Radom_tool sms_Radom_tool =new SMS_Radom_tool();
	   		 String code=sms_Radom_tool.Radom_SMS();
	   		 //将验证码记录一下
	   		 this.SMS_Code=code;
	   		 System.out.println(code);
    		
    		String return_code=sms_Radom_tool.Creat_SMS(phone_Numb,code);

    		if (return_code.equals("OK")) {
    			Msg_bean msg_bean =new Msg_bean("01", "短信发送成功！",null);
    			return msg_bean;
			} else {
    			Msg_bean msg_bean =new Msg_bean("02", return_code, null);
    			return msg_bean;
			}
    		/*短信验证过程结束*/


	}
    
    
	/*查询职位基本信息控制器*/
	@RequestMapping("/count_user_online")
	@ResponseBody
	public Msg_bean count_user_online(){
		
			
            List<CountUser_Bean> countUser_Beans= mongoTemplate.findAll(CountUser_Bean.class, "count");
            List<CountUser_Bean> countUser_Beans_return=new ArrayList<>();
            for (CountUser_Bean countUser_Bean : countUser_Beans) {
				if (countUser_Bean.getOutloginTime()==null) {
					countUser_Beans_return.add(countUser_Bean);
				}
			}
            
			Msg_bean msg_bean =new Msg_bean("25", "实时在线用户返回成功！",countUser_Beans_return);
			return msg_bean;
	}
	
    
    


}
