package com.lingber.controller;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.jasper.tagplugins.jstl.core.Url;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.lingber.bean.Msg_bean;
import com.lingber.bean.PictureWithUsername_Bean;
import com.lingber.bean.Picture_Bean;
import com.lingber.bean.User_Bean;
import com.lingber.bean.distance_Bean;
import com.lingber.bean.picture_content_bean;
import com.lingber.service.LonLat_random;
import com.lingber.service.oss_service;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;


/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年4月11日 下午2:37:05 
* 类说明 :位置查询控制器
*/
@RequestMapping("/gpos") 
@Controller
public class GeoSpatial_Queries_controller {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private oss_service oss_service;
	
	@ResponseBody
    @RequestMapping("queries")
    public Msg_bean<Picture_Bean> springUploadwithTest(HttpServletResponse response,Double longitude,Double latitude,Double radii) throws IllegalStateException, IOException
    {
	   long  startTime=System.currentTimeMillis();
	   //位置查询开始
	   System.out.println("longitude："+longitude+"latitude："+latitude+"radii："+radii);
	   Circle circle = new Circle(longitude,latitude,radii);
	   
	   /*Circle circle = new Circle(-73.99171,40.738868,100000);*/
	   List<Picture_Bean> picture_finds =
	   mongoTemplate.find(new Query(Criteria.where("location").withinSphere(circle)), Picture_Bean.class,"photo");
	   
	   if (picture_finds!=null) {
			   for (Picture_Bean picture_find : picture_finds) {
				 String pic_name=picture_find.getPicture_Name();
				 double[] pic_location=picture_find.getLocation();
				 System.out.println(pic_name);
				 System.out.println(pic_location);

				 URL url =oss_service.get_oss_url(pic_name);
				 picture_find.setUrl(url);
			}
	   }else {
		   
	        Msg_bean<Picture_Bean>msg_bean = new Msg_bean<>("24", "图片查询失败，位置信息错误！", null);
	        return msg_bean; 
	   }
       long  endTime=System.currentTimeMillis();
       System.out.println("位置查询方法的运行时间："+String.valueOf(endTime-startTime)+"ms");

        Msg_bean<Picture_Bean>msg_bean = new Msg_bean<>("15", "图片查找成功！", picture_finds);
        
        return msg_bean; 
//		return picture_finds; 
    }
    
    
	@ResponseBody
    @RequestMapping("queries_with_name")
    public Msg_bean<PictureWithUsername_Bean> getpicture_with_name(HttpServletResponse response,Double longitude,Double latitude,Double radii) throws IllegalStateException, IOException
    {
	   long  startTime=System.currentTimeMillis();
	   //位置查询开始
	   System.out.println("longitude："+longitude+"latitude："+latitude+"radii："+radii);
	   Circle circle = new Circle(longitude,latitude,radii);
	   /*Circle circle = new Circle(-73.99171,40.738868,100000);*/
	   List<Picture_Bean> picture_finds =
	   mongoTemplate.find(new Query(Criteria.where("location").withinSphere(circle)), Picture_Bean.class,"photo");
	   //新建图片返回对象（with_name）
	   List<PictureWithUsername_Bean>pictureWithUsername_Beans =new ArrayList<>();
	   
	   if (picture_finds!=null) {
			   for (Picture_Bean picture_find : picture_finds) {
				 String pic_name=picture_find.getPicture_Name();
				 double[] pic_location=picture_find.getLocation();
				 String userID=picture_find.getUserID();
				 picture_content_bean content = picture_find.getContent();
				 String shootingTime = picture_find.getShootingTime();
				 String locationName = picture_find.getLocationName();
				 String isRecognition = picture_find.getIsRecognition();
				 String pic_Class = picture_find.getPicture_Class();
				 URL url =oss_service.get_oss_url(pic_name);
				 picture_find.setUrl(url);
				 
				 System.out.println(pic_name);
				 System.out.println(pic_location);
				 System.out.println(userID);

				 if (userID!=null) {
					 Query query =new Query(Criteria.where("userID").is(userID));
					 User_Bean user_Bean= mongoTemplate.findOne(query, User_Bean.class, "mvc");
					 if (user_Bean!=null) {
						 String userName=user_Bean.getUserName();
						 URL userImage_URL=oss_service.get_oss_url(user_Bean.getUserImage_Name());
						 System.out.println(userName);
						 pictureWithUsername_Beans.add(new PictureWithUsername_Bean(userName,userID, pic_name, pic_location, content, shootingTime, url, locationName,isRecognition,pic_Class,userImage_URL));
					}
				}else {
			        Msg_bean<PictureWithUsername_Bean>msg_bean = new Msg_bean<>("28", "图片查询失败，位置信息错误！", null);
			        return msg_bean; 
				}
				 
			}
	   }else {
		   
	        Msg_bean<PictureWithUsername_Bean>msg_bean = new Msg_bean<>("24", "图片查询失败，位置信息错误！", null);
	        return msg_bean; 
	   }
       long  endTime=System.currentTimeMillis();
       System.out.println("位置查询方法的运行时间："+String.valueOf(endTime-startTime)+"ms");

        Msg_bean<PictureWithUsername_Bean>msg_bean = new Msg_bean<>("27", "带用户名的图片信息查找成功！", pictureWithUsername_Beans);
        
        return msg_bean; 
//		return picture_finds; 
    }
    
    //返回带有具体距离的图片 
	@ResponseBody
    @RequestMapping("queries_with_distance")
    public Msg_bean<distance_Bean> getpicture_with_distance(HttpServletResponse response,Double longitude,Double latitude,Double radii,int picture_number) throws IllegalStateException, IOException
    {
	   long  startTime=System.currentTimeMillis();
	   //位置查询开始
	   System.out.println("longitude："+longitude+"latitude："+latitude+"radii："+radii);
	   Circle circle = new Circle(longitude,latitude,radii);
	   /*Circle circle = new Circle(-73.99171,40.738868,100000);*/
	   Criteria criteria = new Criteria();
	   criteria.andOperator(Criteria.where("location").withinSphere(circle),Criteria.where("picture_Class").is("01"));
	   Query query_pic = new Query(criteria);
	   List<Picture_Bean> picture_finds =
	   mongoTemplate.find(query_pic, Picture_Bean.class,"photo");
	   //新建图片返回对象（with_name）
	   List<distance_Bean>distance_Beans =new ArrayList<>();
	   LonLat_random lat_random = new LonLat_random();
	   
	   if (picture_finds!=null) {
		   	   int i=0;
			   for (Picture_Bean picture_find : picture_finds) {
				 String pic_name=picture_find.getPicture_Name();
				 double[] pic_location=picture_find.getLocation();
				 String userID=picture_find.getUserID();
				 picture_content_bean content = picture_find.getContent();
				 String shootingTime = picture_find.getShootingTime();
				 String locationName = picture_find.getLocationName();
				 String isRecognition = picture_find.getIsRecognition();
				 String pic_Class = picture_find.getPicture_Class();
				 URL url =oss_service.get_oss_url(pic_name);
				 picture_find.setUrl(url);
				 
				 System.out.println(pic_name);
				 System.out.println(pic_location);
				 System.out.println(userID);

				 if (userID!=null) {
					 Query query =new Query(Criteria.where("userID").is(userID));
					 User_Bean user_Bean= mongoTemplate.findOne(query, User_Bean.class, "mvc");
					 if (user_Bean!=null) {
						 String userName=user_Bean.getUserName();
						 URL userImage_URL=oss_service.get_oss_url(user_Bean.getUserImage_Name());
						 System.out.println("1111"+userName);
						 System.out.println(picture_find.getLocation()[1]+picture_find.getLocation()[0]);
						 double distance =lat_random.GetDistance(latitude, longitude, picture_find.getLocation()[1], picture_find.getLocation()[0]);
						 distance_Beans.add(new distance_Bean(userName,userID, pic_name, pic_location, content, shootingTime, url, locationName,isRecognition,pic_Class, distance,userImage_URL));
						 if (picture_number!=0) {
							 i++;
							 if (i>=picture_number) {
									break;
								}
						}

						 
					}
				}else {
			        Msg_bean<distance_Bean>msg_bean = new Msg_bean<>("28", "图片查询失败，位置信息错误！", null);
			        return msg_bean; 
				}
				 
			}
	   }else {
		   
	        Msg_bean<distance_Bean>msg_bean = new Msg_bean<>("24", "图片查询失败，位置信息错误！", null);
	        return msg_bean; 
	   }
       long  endTime=System.currentTimeMillis();
       System.out.println("位置查询方法的运行时间："+String.valueOf(endTime-startTime)+"ms");

        Msg_bean<distance_Bean>msg_bean = new Msg_bean<>("39", "带用户名的图片信息查找成功！", distance_Beans);
        
        return msg_bean; 
//		return picture_finds; 
    }
    
    
    
  	@ResponseBody
      @RequestMapping("getpicture_with_hundres")
      public Msg_bean<PictureWithUsername_Bean> getpicture_with_hundres() throws IllegalStateException, IOException
      {	
    	  int i=0;
    	  Query query =new Query(Criteria.where("picture_Class").is("01"));//只查找需要识别的图
  		  List<Picture_Bean> find = mongoTemplate.find(query, Picture_Bean.class, "photo");
  		  Collections.reverse(find); // 倒序排列 
  		  List<PictureWithUsername_Bean>pictureWithUsername_Beans =new ArrayList<>();
  		  for (Picture_Bean picture_Bean : find) {
  			  System.out.println(i);
  			  i++;
  			  if (i==100) {
				break;
			}
  			  if (picture_Bean!=null) {
  	  			  Query query_user= new Query(Criteria.where("userID").is(picture_Bean.getUserID()));
  	  			  User_Bean user_Bean = mongoTemplate.findOne(query_user, User_Bean.class, "mvc");
  	  			  if (user_Bean!=null) {
  	  			 	  URL url= oss_service.get_oss_url(picture_Bean.getPicture_Name());
  	  			 	  URL userUrl = oss_service.get_oss_url(user_Bean.getUserImage_Name());
  	  				  PictureWithUsername_Bean pictureWithUsername_Bean  =new PictureWithUsername_Bean(user_Bean.getUserName(), picture_Bean.getUserID(),
  	  						picture_Bean.getPicture_Name(), picture_Bean.getLocation(), picture_Bean.getContent(),picture_Bean.getShootingTime(), url,
  	  						picture_Bean.getLocationName(), picture_Bean.getIsRecognition(), picture_Bean.getPicture_Class(), userUrl);
  	  				pictureWithUsername_Beans.add(pictureWithUsername_Bean);
				}

				
			}
		}
  		 
    	  
          Msg_bean<PictureWithUsername_Bean>msg_bean = new Msg_bean<>("40", "最新100张图片查找成功功！", pictureWithUsername_Beans);
          
          return msg_bean; 
      }
	

}
