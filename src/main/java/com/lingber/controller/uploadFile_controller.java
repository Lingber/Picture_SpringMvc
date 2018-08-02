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
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.lingber.bean.Msg_bean;
import com.lingber.bean.PictureWithUsername_Bean;
import com.lingber.bean.Picture_Bean;
import com.lingber.bean.User_Bean;
import com.lingber.bean.conten_bean;
import com.lingber.bean.picture_content_bean;
import com.lingber.service.Client_Service;
import com.lingber.service.LonLat_random;
import com.lingber.service.UserID_Radom_tool;
import com.lingber.service.oss_service;
import com.lingber.service.picture_server;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年4月10日 下午6:27:44 
* 类说明 :图片操作控制器
*/
@RequestMapping("/upload") 
@Controller
public class uploadFile_controller {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private oss_service oss_service;
	

	//在服务器端上传图片的接口
	@ResponseBody
    @RequestMapping("server_upload_picture_to_oss")
    public Msg_bean<Picture_Bean>  server_upload_picture_to_oss(HttpServletRequest request,String userId) throws IllegalStateException, IOException
    {
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
        		       Query query =new Query(Criteria.where("picture_Name").is(uuidfilename + "." + prefix));
        		       Picture_Bean picture_Bean_findResult =mongoTemplate.findOne(query, Picture_Bean.class, "photo");
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
    		       
    		       //随机生成经纬度
    		       LonLat_random random =new LonLat_random();
    		       Double lon= random.randomLonLat(-70.99171, -78.99171, 40.738868, 45.738868, "Lon");
    		       Double lat= random.randomLonLat(-70.99171, -78.99171, 40.738868, 45.738868, "Lat");
    		       
    		       String locationName="广西省-桂林市-灵川县";
    		       
                   //生成URL
      			   URL url =oss_service.get_oss_url(NewFileName);

     		       /*先模拟一些种类*/
      			 	
      			   /*图像识别2_0*/
/*      			   List<String>listContents=new ArrayList<>();
    	   		   picture_server picture_server = new picture_server();
    	   		   
    	   		   List<conten_bean>result=picture_server.getcontent(url);
    	   		   
    	   	   	   conten_bean conten_bean =  result.get(0);
    	   	   	   String contentClass = conten_bean.getValue();
	     	   		for (int i = 0; i < result.size(); i++) {
	     	   			listContents.add(result.get(i).getValue());
					}*/
      			   /*图像识别2_0结束*/
      			   
      			 	
      			 	/*图像识别1_0*/
     	   		   List<String>listContents=new ArrayList<>();
     	   		   List<String>result = new ArrayList<>();
     	   		   
     	   		   Client_Service client_Service = new Client_Service();
     	   		   result = client_Service.sentAndRec_data(url);
	     	   		for (int i = 0; i < result.size()-1; i++) {
	     	   			listContents.add(result.get(i));
					}
	     	   	   String contentClass = result.get(result.size()-1);
     	   		   System.out.println(contentClass);
     	   	    	/*图像识别结束*/
     		       picture_content_bean picture_content_bean =new picture_content_bean(NewFileName, listContents, contentClass);
     	   		   
     		       Picture_Bean picture_Bean= new Picture_Bean(userid, NewFileName, lon, lat, picture_content_bean, date, url, locationName, "ture", "01");

    		       picture_Beans.add(picture_Bean);
    		       mongoTemplate.insert(picture_Bean, "photo");
    		       
    		       System.out.println("上传成功！");
    		       File file_del =new File(completepath);
    		       //从服务器的缓存中删除文件
    		       file_del.delete();
               }
           }
       }else {
		System.out.println();
		Msg_bean<Picture_Bean> msg_bean =new Msg_bean("12", "图片未上传！",null);
		return msg_bean;
       	}
       long  endTime=System.currentTimeMillis();
       System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");
       
        
       
		Msg_bean<Picture_Bean> msg_bean =new Msg_bean("11", "图片上传成功！",picture_Beans);
		return msg_bean;
       
    }
	
    
    //客户端上传图片的接口
	@ResponseBody
    @RequestMapping("/client_upload_picture_to_oss")
    public Msg_bean<PictureWithUsername_Bean>  client_upload_picture_to_oss(HttpServletRequest request,Double longitude,Double latitude,String userId,String locationName) throws IllegalStateException, IOException
    {
	   long  startTime=System.currentTimeMillis();
	   
	   List<PictureWithUsername_Bean> pictureWithUsername_Beans = new ArrayList();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
       CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
               request.getSession().getServletContext());
	       
	     if ((longitude==null&&latitude==null)) {
   			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("38", "参数未输入",null);
   			return msg_bean;
		}
       
       
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
        			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("37", "未检测到图片！",null);
        			return msg_bean;
				   }
            	   if (!(prefix.equals("jpg"))) {
            			Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("36", "图片格式错误！要求.jpg格式",null);
            			return msg_bean;
            	   }
            	   
            	   //生成新的名字
    		       UserID_Radom_tool userID_Radom_tool =new UserID_Radom_tool();
    		       
            	   String uuidfilename = userID_Radom_tool.randomFileName();

                   //检测文件名称  
                   while(true){  
        		       //新建查询
        		       Query query =new Query(Criteria.where("picture_Name").is(uuidfilename + "." + prefix));
        		       Picture_Bean picture_Bean_findResult =mongoTemplate.findOne(query, Picture_Bean.class, "photo");

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
    		       System.out.println("1");
                   //生成URL
    	   			URL url =oss_service.get_oss_url(NewFileName);
    	   		 System.out.println("2");
    		       /*先模拟一些种类*/
    	   		 
    			   /*图像识别2_0*/
/*    			    List<String>listContents=new ArrayList<>();
    			    picture_server picture_server = new picture_server();
  	   		   
  	   		   		List<conten_bean>result=picture_server.getcontent(url);
  	   		   
  	   		   		conten_bean conten_bean =  result.get(0);
  	   	   	   		String contentClass = conten_bean.getValue();
	     	   		for (int i = 0; i < result.size(); i++) {
	     	   			listContents.add(result.get(i).getValue());
					}*/
    			   /*图像识别2_0结束*/
    	   		 
    	   		 
    	   		 
    	   		 
   			 	/*图像识别*/
	   	   		   List<String>listContents=new ArrayList<>();
	   	   		   List<String>result = new ArrayList<>();
	   	   		   
	   	   		   Client_Service client_Service = new Client_Service();
	   	   		   result = client_Service.sentAndRec_data(url);
		     	   		for (int i = 0; i < result.size()-1; i++) {
		     	   			listContents.add(result.get(i));
						}
		     	   	   String contentClass = result.get(result.size()-1);
	   	   		   System.out.println(contentClass);
   	   	    	/*图像识别结束*/
    		       picture_content_bean picture_content_bean =new picture_content_bean(NewFileName, listContents, contentClass);
    		       System.out.println("3");
   	    		   Query query_user =new Query(Criteria.where("userID").is(userid));
   	    		   User_Bean find_user = mongoTemplate.findOne(query_user, User_Bean.class, "mvc");
   	    		   System.out.println("4");
   	    		   
    		       URL UserImage_URL = oss_service.get_oss_url(find_user.getUserImage_Name());
    		       String userName= find_user.getUserName();
    		       System.out.println("5");
    		       
/*    		       double longitude1=70.1111111;
    		       double longitude2=70.1111111;*/

    		       
    		       Picture_Bean picture_Bean= new Picture_Bean(userid, NewFileName, longitude, latitude, picture_content_bean, date, url, locationName, "ture", "01");
    		       /*Picture_Bean picture_Bean= new Picture_Bean(userid, NewFileName, longitude1, longitude2, picture_content_bean, date, url, locationName, "ture", "01");*/
    		       System.out.println("5.5");
    		       PictureWithUsername_Bean  pictureWithUsername_Bean= new PictureWithUsername_Bean(userName,userid, NewFileName, longitude, latitude, picture_content_bean, date, url, locationName, "ture", "01",UserImage_URL);
    		       /*PictureWithUsername_Bean  pictureWithUsername_Bean= new PictureWithUsername_Bean(userName,userid, NewFileName, longitude1, longitude1, picture_content_bean, date, url, locationName, "ture", "01",UserImage_URL);*/
    		       System.out.println("6");
    		       mongoTemplate.insert(picture_Bean, "photo");
    		       pictureWithUsername_Beans.add(pictureWithUsername_Bean);
    		       /*预留给图像处理部分的部分*/
    		       System.out.println("7");
    		       
    		       System.out.println("上传成功！");
    		       File file_del =new File(completepath);
    		       //从服务器的缓存中删除文件
    		       file_del.delete();
               }
           }
       }
       long  endTime=System.currentTimeMillis();
       System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");
       
        
       
		Msg_bean<PictureWithUsername_Bean> msg_bean =new Msg_bean("11", "图片上传成功！",pictureWithUsername_Beans);
		return msg_bean;
       
    }
    
    
	@ResponseBody
    @RequestMapping("client_upload_userImage_to_oss")
    public Msg_bean<User_Bean>  client_upload_userImage_to_oss(HttpServletRequest request,String userId) throws IllegalStateException, IOException
    {
	   long  startTime=System.currentTimeMillis();

	   List<User_Bean>user_Beans =new ArrayList<>();
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
        			Msg_bean<User_Bean> msg_bean =new Msg_bean("37", "未检测到图片！",null);
        			return msg_bean;
				   }
            	   if (!(prefix.equals("jpg"))) {
            			Msg_bean<User_Bean> msg_bean =new Msg_bean("36", "图片格式错误！要求.jpg格式",null);
            			return msg_bean;
            	   }
            	   
            	   //生成新的名字
    		       UserID_Radom_tool userID_Radom_tool =new UserID_Radom_tool();
    		       
            	   String uuidfilename = userID_Radom_tool.randomFileName();

                   //检测文件名称  
                   while(true){  
        		       //新建查询
        		       Query query =new Query(Criteria.where("picture_Name").is(uuidfilename + "." + prefix));
        		       Picture_Bean picture_Bean_findResult =mongoTemplate.findOne(query, Picture_Bean.class, "photo");

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
    		       System.out.println("强行转个屁:"+userid);
    		       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    		       String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
    		       System.out.println("生成URL");
                   //生成URL
    	   			URL url =oss_service.get_oss_url(NewFileName);
    		       
    	   			Picture_Bean picture_Bean= new Picture_Bean(userid, NewFileName, 0, 0, null, date, url, null, null, "02");

    	   			
    		        mongoTemplate.insert(picture_Bean, "photo");
    		        
    		        
    	    		Query query_user =new Query(Criteria.where("userID").is(userid));
    	    		User_Bean find_user = mongoTemplate.findOne(query_user, User_Bean.class, "mvc");
    	    		String userName=find_user.getUserName();
		    		Update update =new Update();
		    		mongoTemplate.upsert(query_user,update.update("userName", userName),Picture_Bean.class,"mvc");
		    		mongoTemplate.upsert(query_user,update.update("userImage_URL", url),Picture_Bean.class,"mvc");
		    		mongoTemplate.upsert(query_user,update.update("userImage_Name", NewFileName),Picture_Bean.class,"mvc");
		    		
		    		User_Bean user_Bean =mongoTemplate.findOne(query_user, User_Bean.class, "mvc");
		    		user_Beans.add(user_Bean);
		    		
/*	    		        

    		       /*预留给图像处理部分的部分*/
    		       
    		       System.out.println("上传成功！");
    		       File file_del =new File(completepath);
    		       //从服务器的缓存中删除文件
    		       file_del.delete();
               }
           }
       }
       long  endTime=System.currentTimeMillis();
       System.out.println("方法三的运行时间："+String.valueOf(endTime-startTime)+"ms");
       
        
       
		Msg_bean<User_Bean> msg_bean =new Msg_bean("31", "头像上传成功！",user_Beans);
		return msg_bean;
       
    }
    
    

	
}
