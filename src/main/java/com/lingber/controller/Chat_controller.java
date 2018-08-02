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

import org.apache.commons.lang.ObjectUtils.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingber.bean.Comments_Bean;
import com.lingber.bean.Msg_bean;
import com.lingber.bean.Picture_Bean;
import com.lingber.bean.UserChat_Bean;
import com.lingber.bean.UserChat_withAllName_bean;
import com.lingber.bean.UserChat_withAll_bean;
import com.lingber.bean.User_Bean;
import com.lingber.bean.picture_content_bean;
import com.lingber.service.UserID_Radom_tool;
import com.lingber.service.oss_service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月22日 下午3:12:06 
* 类说明 :
*/

@RequestMapping("/chat") 
@Controller
public class Chat_controller {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private oss_service oss_service;
	
	
	
	/*查询职位基本信息控制器*/
	@RequestMapping("/get_chat_byUserName")
	@ResponseBody
	public Msg_bean get_chat_byUserName(String userName){
		    //引入pageHelper插件
			PageHelper.startPage(1, 10);
		    //startPage后紧跟的查询就是分页查询
            Query query =new Query(Criteria.where("senderName").is(userName));
    		List<UserChat_Bean> find = mongoTemplate.find(query, UserChat_Bean.class, "chat");
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
	
	
	
	
	//插入私信信息
	@ResponseBody
    @RequestMapping("/insert_Messge")
	public Msg_bean<UserChat_Bean> insertMessge(HttpServletRequest request,String receiverID,String senderID,String chatContent) throws IllegalStateException, IOException {
		
				//新建评论
		        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		        String chat_date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
		        URL userImage_URL=null;
		        String picture_Name = null;
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
	    	            			Msg_bean<UserChat_Bean> msg_bean =new Msg_bean("36", "图片格式错误！要求.jpg格式",null);
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
	    	    		       String userid = receiverID;
	    	    		       System.out.println("强行转:"+userid);
	    	    		       
	    	    		       String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
	    	    		       
	    	                   //生成URL
	    	    	   			URL url =oss_service.get_oss_url(NewFileName);
	    	    	   			userImage_URL=url;
	    	    	   			picture_Name = NewFileName;
	    	    	   			
	    	    		       /*Picture_Bean picture_Bean= new Picture_Bean(userid, NewFileName, null, null, null, date, url, null, null, "02");*/
	    	    	   			Picture_Bean picture_Bean= new Picture_Bean(userid, NewFileName, 0, 0, null, date, url, null, null, "03");
	    	    	   				//如果用户之前没有设置过头像
	    		    		    picture_Beans.add(picture_Bean);
	    		    		    mongoTemplate.insert(picture_Bean, "photo");

	    	    		       
	    	    		       System.out.println("上传成功！");
	    	    		       File file_del =new File(completepath);
	    	    		       //从服务器的缓存中删除文件
	    	    		       file_del.delete();
	    	               }
	    	           }
	    	       }else{
	    	    	   System.out.println("没有上传图片！");
	    	       }
	    	       
	    	       
    	    	   Query query_sender= new Query(Criteria.where("userID").is(senderID));
    	    	   User_Bean user_Bean_sender = mongoTemplate.findOne(query_sender, User_Bean.class, "mvc");
    	    	   String senderName=user_Bean_sender.getUserName();
    	    	   URL URL= oss_service.get_oss_url(user_Bean_sender.getUserImage_Name());
    	    	   /*URL URL=user_Bean_sender.getUserImage_URL();*/
	    	       List<UserChat_Bean>userChat_Beans =new ArrayList<>();
	    	       if (userImage_URL!=null) {
	    	    	   //消息型数据
	    	    	   UserChat_Bean userChat_Bean =new UserChat_Bean(receiverID, senderID, chatContent, userImage_URL, "05", chat_date, senderName, URL, picture_Name);
	    	    	   mongoTemplate.insert(userChat_Bean, "chat");
	    	    	   userChat_Beans.add(userChat_Bean);
				} else {
					   //字符型数据
					   UserChat_Bean userChat_Bean =new UserChat_Bean(receiverID, senderID, chatContent, userImage_URL, "04", chat_date, senderName, URL, picture_Name);
					   mongoTemplate.insert(userChat_Bean, "chat");
					   userChat_Beans.add(userChat_Bean);
				}
	    		       Msg_bean msg_bean =new Msg_bean("33", "私信信息插入成功！",userChat_Beans);
	    			   return msg_bean;
	}
	
	
	
	@ResponseBody
    @RequestMapping("/find_Messge_byUserID")
	public Msg_bean<UserChat_Bean> findMessge(String receiverID,String senderID,String chatClass)  {
			System.out.println(chatClass);
			List<UserChat_withAll_bean>userChat_withAll_beans = new ArrayList<>();
			if (!(chatClass.equals("04")||chatClass.equals("05")||chatClass.equals("06"))) {
	    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！参数错误",null);
	    	    return msg_bean;
			}
		if (chatClass.equals("06")) {//如果想要全部查询
			
			if ((receiverID!=null)&&(senderID!=null)) {
			//如果两个参数都有
		    Criteria criteria = new Criteria();
		    criteria.andOperator(Criteria.where("senderID").is(senderID),Criteria.where("receiverID").is(receiverID));
		    Query query = new Query(criteria);
			List<UserChat_Bean> finds = mongoTemplate.find(query, UserChat_Bean.class, "chat");	
			
				if (finds.size()!=0) {
					for (UserChat_Bean userChat_Bean : finds) {
						URL senderURL= get_userImageURL(userChat_Bean.getSenderID());
						userChat_Bean.setSenderURL(senderURL);
						if (userChat_Bean.getPicture_Name()!=null) {
							URL pic_URL= get_pictureURL(userChat_Bean.getPicture_Name());
							userChat_Bean.setPicURL(pic_URL);
						}
						//添加reciveName
						Query query_receiverName=new Query(Criteria.where("userID").is(userChat_Bean.getReceiverID()));
						User_Bean user_Bean = mongoTemplate.findOne(query_receiverName, User_Bean.class, "mvc");
						if (user_Bean==null) {
				    	    Msg_bean msg_bean =new Msg_bean("37", "私信信息查找失败!收件人用户不存在",null);
				    	    return msg_bean;
						}
						String receiverName= user_Bean.getUserName();
						URL receiverURL= get_userImageURL(userChat_Bean.getReceiverID());
						UserChat_withAll_bean userChat_withAll_bean = new UserChat_withAll_bean(userChat_Bean.getReceiverID(),
								userChat_Bean.getSenderID(), userChat_Bean.getChatContent(), userChat_Bean.getPicURL(), userChat_Bean.getChatClass(),
								userChat_Bean.getChatTime(), userChat_Bean.getSenderName(), receiverName, senderURL,
								receiverURL, userChat_Bean.getPicture_Name());
						userChat_withAll_beans.add(userChat_withAll_bean);
						//添加reciveName结束
						
					}
		    	    Msg_bean msg_bean =new Msg_bean("34", "私信信息查找成功！",userChat_withAll_beans);
		    	    return msg_bean;
				} else {
		    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！信息不存在",null);
		    	    return msg_bean;
				}
			} else {
					
				if (receiverID!=null) {
					Query query =new Query(Criteria.where("receiverID").is(receiverID));
					List<UserChat_Bean> finds = mongoTemplate.find(query, UserChat_Bean.class, "chat");	
						if (finds.size()!=0) {
							for (UserChat_Bean userChat_Bean : finds) {
								URL senderURL= get_userImageURL(userChat_Bean.getSenderID());
								userChat_Bean.setSenderURL(senderURL);
								if (userChat_Bean.getPicture_Name()!=null) {
									URL pic_URL= get_pictureURL(userChat_Bean.getPicture_Name());
									userChat_Bean.setPicURL(pic_URL);
								}
								//添加reciveName
								Query query_receiverName=new Query(Criteria.where("userID").is(userChat_Bean.getReceiverID()));
								User_Bean user_Bean = mongoTemplate.findOne(query_receiverName, User_Bean.class, "mvc");
								if (user_Bean==null) {
						    	    Msg_bean msg_bean =new Msg_bean("37", "私信信息查找失败!收件人用户不存在",null);
						    	    return msg_bean;
								}
								String receiverName= user_Bean.getUserName();
								URL receiverURL= get_userImageURL(userChat_Bean.getReceiverID());
								UserChat_withAll_bean userChat_withAll_bean = new UserChat_withAll_bean(userChat_Bean.getReceiverID(),
										userChat_Bean.getSenderID(), userChat_Bean.getChatContent(), userChat_Bean.getPicURL(), userChat_Bean.getChatClass(),
										userChat_Bean.getChatTime(), userChat_Bean.getSenderName(), receiverName, senderURL,
										receiverURL, userChat_Bean.getPicture_Name());
								userChat_withAll_beans.add(userChat_withAll_bean);
								//添加reciveName结束
								
							}
				    	    Msg_bean msg_bean =new Msg_bean("34", "私信信息查找成功！",userChat_withAll_beans);
				    	    return msg_bean;
						} else {
				    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！信息不存在",null);
				    	    return msg_bean;
						}
				}else {
					if (senderID!=null) {
						Query query =new Query(Criteria.where("senderID").is(senderID));
						List<UserChat_Bean> finds = mongoTemplate.find(query, UserChat_Bean.class, "chat");	
							if (finds.size()!=0) {
								for (UserChat_Bean userChat_Bean : finds) {
									URL senderURL= get_userImageURL(userChat_Bean.getSenderID());
									userChat_Bean.setSenderURL(senderURL);
									if (userChat_Bean.getPicture_Name()!=null) {
										URL pic_URL= get_pictureURL(userChat_Bean.getPicture_Name());
										userChat_Bean.setPicURL(pic_URL);
									}
									//添加reciveName
									Query query_receiverName=new Query(Criteria.where("userID").is(userChat_Bean.getReceiverID()));
									User_Bean user_Bean = mongoTemplate.findOne(query_receiverName, User_Bean.class, "mvc");
									if (user_Bean==null) {
							    	    Msg_bean msg_bean =new Msg_bean("37", "私信信息查找失败!收件人用户不存在",null);
							    	    return msg_bean;
									}
									String receiverName= user_Bean.getUserName();
									URL receiverURL= get_userImageURL(userChat_Bean.getReceiverID());
									UserChat_withAll_bean userChat_withAll_bean = new UserChat_withAll_bean(userChat_Bean.getReceiverID(),
											userChat_Bean.getSenderID(), userChat_Bean.getChatContent(), userChat_Bean.getPicURL(), userChat_Bean.getChatClass(),
											userChat_Bean.getChatTime(), userChat_Bean.getSenderName(), receiverName, senderURL,
											receiverURL, userChat_Bean.getPicture_Name());
									userChat_withAll_beans.add(userChat_withAll_bean);
									//添加reciveName结束
								}
					    	    Msg_bean msg_bean =new Msg_bean("34", "私信信息查找成功！",userChat_withAll_beans);
					    	    return msg_bean;
							} else {
					    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！信息不存在",null);
					    	    return msg_bean;
							}
					}else {
			    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！信息不存在",null);
			    	    return msg_bean;
					}
				} 
			}
			
		} else {
		//判断查询哪种类型的数据
			if ((receiverID!=null)&&(senderID!=null)) {
			//如果两个参数都有
			Criteria criteria = new Criteria();
			criteria.andOperator(Criteria.where("senderID").is(senderID),Criteria.where("receiverID").is(receiverID),Criteria.where("chatClass").is(chatClass)); 
			Query query = new  Query(criteria);
			/*Query query =new Query(Criteria.where("senderID").is(senderID).where("receiverID").is(receiverID).where("chatClass").is(chatClass));*/
			List<UserChat_Bean> finds = mongoTemplate.find(query, UserChat_Bean.class, "chat");

				if (finds.size()!=0) {
					for (UserChat_Bean userChat_Bean : finds) {
						URL senderURL= get_userImageURL(userChat_Bean.getSenderID());
						userChat_Bean.setSenderURL(senderURL);
						
						if (userChat_Bean.getPicture_Name()!=null) {
							URL pic_URL= get_pictureURL(userChat_Bean.getPicture_Name());
							userChat_Bean.setPicURL(pic_URL);
						}
						//添加reciveName
						Query query_receiverName=new Query(Criteria.where("userID").is(userChat_Bean.getReceiverID()));
						User_Bean user_Bean = mongoTemplate.findOne(query_receiverName, User_Bean.class, "mvc");
						if (user_Bean==null) {
				    	    Msg_bean msg_bean =new Msg_bean("37", "私信信息查找失败!收件人用户不存在",null);
				    	    return msg_bean;
						}
						String receiverName= user_Bean.getUserName();
						URL receiverURL= get_userImageURL(userChat_Bean.getReceiverID());
						UserChat_withAll_bean userChat_withAll_bean = new UserChat_withAll_bean(userChat_Bean.getReceiverID(),
								userChat_Bean.getSenderID(), userChat_Bean.getChatContent(), userChat_Bean.getPicURL(), userChat_Bean.getChatClass(),
								userChat_Bean.getChatTime(), userChat_Bean.getSenderName(), receiverName, senderURL,
								receiverURL, userChat_Bean.getPicture_Name());
						userChat_withAll_beans.add(userChat_withAll_bean);
						//添加reciveName结束
					}
		    	    Msg_bean msg_bean =new Msg_bean("34", "私信信息查找成功！",userChat_withAll_beans);
		    	    return msg_bean;
				} else {
		    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！信息不存在",null);
		    	    return msg_bean;
				}
			} else {
					
				if (receiverID!=null) {
					Criteria criteria = new Criteria();
					criteria.andOperator(Criteria.where("receiverID").is(receiverID),Criteria.where("chatClass").is(chatClass));
					Query query = new  Query(criteria);
					/*Query query =new Query(Criteria.where("receiverID").is(receiverID).where("chatClass").is(chatClass));*/
					List<UserChat_Bean> finds = mongoTemplate.find(query, UserChat_Bean.class, "chat");
					
						if (finds.size()!=0) {
							for (UserChat_Bean userChat_Bean : finds) {
								URL senderURL= get_userImageURL(userChat_Bean.getSenderID());
								userChat_Bean.setSenderURL(senderURL);
								if (userChat_Bean.getPicture_Name()!=null) {
									URL pic_URL= get_pictureURL(userChat_Bean.getPicture_Name());
									userChat_Bean.setPicURL(pic_URL);
								}
								//添加reciveName
								Query query_receiverName=new Query(Criteria.where("userID").is(userChat_Bean.getReceiverID()));
								User_Bean user_Bean = mongoTemplate.findOne(query_receiverName, User_Bean.class, "mvc");
								if (user_Bean==null) {
						    	    Msg_bean msg_bean =new Msg_bean("37", "私信信息查找失败!收件人用户不存在",null);
						    	    return msg_bean;
								}
								String receiverName= user_Bean.getUserName();
								URL receiverURL= get_userImageURL(userChat_Bean.getReceiverID());
								UserChat_withAll_bean userChat_withAll_bean = new UserChat_withAll_bean(userChat_Bean.getReceiverID(),
										userChat_Bean.getSenderID(), userChat_Bean.getChatContent(), userChat_Bean.getPicURL(), userChat_Bean.getChatClass(),
										userChat_Bean.getChatTime(), userChat_Bean.getSenderName(), receiverName, senderURL,
										receiverURL, userChat_Bean.getPicture_Name());
								userChat_withAll_beans.add(userChat_withAll_bean);
								//添加reciveName结束
								
							}
				    	    Msg_bean msg_bean =new Msg_bean("34", "私信信息查找成功！",userChat_withAll_beans);
				    	    return msg_bean;
						} else {
				    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！信息不存在",null);
				    	    return msg_bean;
						}
				}else {
					if (senderID!=null) {
						Criteria criteria = new Criteria();
						criteria.andOperator(Criteria.where("senderID").is(senderID),Criteria.where("chatClass").is(chatClass));
						Query query = new  Query(criteria);
						/*Query query =new Query(Criteria.where("senderID").is(senderID).where("chatClass").is(chatClass));*/
						List<UserChat_Bean> finds = mongoTemplate.find(query, UserChat_Bean.class, "chat");	
							if (finds.size()!=0) {
								for (UserChat_Bean userChat_Bean : finds) {
									URL senderURL= get_userImageURL(userChat_Bean.getSenderID());
									userChat_Bean.setSenderURL(senderURL);
									if (userChat_Bean.getPicture_Name()!=null) {
										URL pic_URL= get_pictureURL(userChat_Bean.getPicture_Name());
										userChat_Bean.setPicURL(pic_URL);
									}
									//添加reciveName
									Query query_receiverName=new Query(Criteria.where("userID").is(userChat_Bean.getReceiverID()));
									User_Bean user_Bean = mongoTemplate.findOne(query_receiverName, User_Bean.class, "mvc");
									if (user_Bean==null) {
							    	    Msg_bean msg_bean =new Msg_bean("37", "私信信息查找失败!收件人用户不存在",null);
							    	    return msg_bean;
									}
									String receiverName= user_Bean.getUserName();
									URL receiverURL= get_userImageURL(userChat_Bean.getReceiverID());
									UserChat_withAll_bean userChat_withAll_bean = new UserChat_withAll_bean(userChat_Bean.getReceiverID(),
											userChat_Bean.getSenderID(), userChat_Bean.getChatContent(), userChat_Bean.getPicURL(), userChat_Bean.getChatClass(),
											userChat_Bean.getChatTime(), userChat_Bean.getSenderName(), receiverName, senderURL,
											receiverURL, userChat_Bean.getPicture_Name());
									userChat_withAll_beans.add(userChat_withAll_bean);
									//添加reciveName结束
									
								}
					    	    Msg_bean msg_bean =new Msg_bean("34", "私信信息查找成功！",userChat_withAll_beans);
					    	    return msg_bean;
							} else {
					    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！信息不存在",null);
					    	    return msg_bean;
							}
					}else {
			    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！信息不存在",null);
			    	    return msg_bean;
					}
				} 
			}
		}
	}
	
	@ResponseBody
    @RequestMapping("/find_Messge_WithAll")
	public Msg_bean<UserChat_Bean> find_Messge_WithAll(String receiverID,String senderID,String chatClass)  {
			System.out.println(chatClass);
			if (!(chatClass.equals("04")||chatClass.equals("05")||chatClass.equals("06"))) {
	    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！参数错误",null);
	    	    return msg_bean;
			}
			
			//如果两个参数都有
		    Criteria criteria = new Criteria();
		    criteria.andOperator(Criteria.where("senderID").is(senderID),Criteria.where("receiverID").is(receiverID),Criteria.where("chatClass").is(chatClass));
		    Query query = new Query(criteria);
			List<UserChat_Bean> finds = mongoTemplate.find(query, UserChat_Bean.class, "chat");	
			List<UserChat_withAll_bean>userChat_withAll_beans = new ArrayList<>();
				if (finds.size()!=0) {
					for (UserChat_Bean userChat_Bean : finds) {
						System.out.println("userChat_Bean："+userChat_Bean.getReceiverID());
						System.out.println("userChat_Bean："+userChat_Bean.getSenderID());
						URL senderURL= get_userImageURL(userChat_Bean.getSenderID());
						userChat_Bean.setSenderURL(senderURL);
						System.out.println("1");
						Query query_receiverName=new Query(Criteria.where("userID").is(userChat_Bean.getReceiverID()));
						User_Bean user_Bean = mongoTemplate.findOne(query_receiverName, User_Bean.class, "mvc");
						if (user_Bean==null) {
				    	    Msg_bean msg_bean =new Msg_bean("37", "私信信息查找失败!收件人用户不存在",null);
				    	    return msg_bean;
						}
						String receiverName= user_Bean.getUserName();
						URL receiverURL= get_userImageURL(userChat_Bean.getReceiverID());
						if (userChat_Bean.getPicture_Name()!=null) {
							URL pic_URL= get_pictureURL(userChat_Bean.getPicture_Name());
							userChat_Bean.setPicURL(pic_URL);
						}
						
						UserChat_withAll_bean userChat_withAll_bean = new UserChat_withAll_bean(userChat_Bean.getReceiverID(),
								userChat_Bean.getSenderID(), userChat_Bean.getChatContent(), userChat_Bean.getPicURL(), userChat_Bean.getChatClass(),
								userChat_Bean.getChatTime(), userChat_Bean.getSenderName(), receiverName, senderURL,
								receiverURL, userChat_Bean.getPicture_Name());
						
						
						userChat_withAll_beans.add(userChat_withAll_bean);
					}
		    	    Msg_bean msg_bean =new Msg_bean("34", "私信信息查找成功！",userChat_withAll_beans);
		    	    return msg_bean;
				} else {
		    	    Msg_bean msg_bean =new Msg_bean("35", "私信信息查找失败！信息不存在",null);
		    	    return msg_bean;
				}

	}
	
	
	@ResponseBody
    @RequestMapping("/findAll_Messge")
	public Msg_bean<UserChat_Bean> findAll_Messge(@RequestParam(value="pn",defaultValue="1")Integer pn)  {
		
	    //引入pageHelper插件
		PageHelper.startPage(pn, 10);
		List<UserChat_withAllName_bean>userChat_withAllName_beans = new ArrayList<>();
		
		List<UserChat_Bean> userChat_Beans =mongoTemplate.findAll(UserChat_Bean.class, "chat");
		for (UserChat_Bean userChat_Bean : userChat_Beans) {
			Query query_Receiver= new Query(Criteria.where("userID").is(userChat_Bean.getReceiverID()));
			User_Bean user_Bean_findrec=mongoTemplate.findOne(query_Receiver, User_Bean.class, "mvc");
			if (user_Bean_findrec!=null) {
				URL pic_URL= get_pictureURL(userChat_Bean.getPicture_Name());
				userChat_Bean.setPicURL(pic_URL);
				String ReceiverName	= user_Bean_findrec.getUserName();
				UserChat_withAllName_bean userChat_withAllName_bean = new UserChat_withAllName_bean
				(userChat_Bean.getReceiverID(), userChat_Bean.getSenderID(), userChat_Bean.getChatContent(),
				pic_URL, userChat_Bean.getChatClass(), userChat_Bean.getChatTime(),
				userChat_Bean.getSenderName(), ReceiverName, userChat_Bean.getSenderURL(), userChat_Bean.getPicture_Name());
				userChat_withAllName_beans.add(userChat_withAllName_bean);
			}
		}
	    //使用PageInfo包装查询后的结果
		//PageInfo封装了详细的分页信息包括查询出的数据，只需将pageInfo交给页面即可
		//参数5表示连续显示5页
		PageInfo page = new PageInfo(userChat_withAllName_beans,10);
		//JSONObject jsonObject = new JSONObject(page);
		return Msg_bean.success().add("pageInfo",page);
	}
	
	
	@ResponseBody
    @RequestMapping("/del_Messge")
	public Msg_bean<UserChat_Bean> del_Messge(String receiverID,String senderID,String chatTime)  {
			System.out.println(chatTime);
			//如果两个参数都有
		    Criteria criteria = new Criteria();
		    criteria.andOperator(Criteria.where("senderID").is(senderID),Criteria.where("receiverID").is(receiverID),Criteria.where("chatTime").is(chatTime));
		    Query query = new Query(criteria);
		    mongoTemplate.findAndRemove(query, UserChat_Bean.class, "chat");
    	    Msg_bean msg_bean =new Msg_bean("34", "私信信息删除成功！",null);
    	    return msg_bean;
	}
	
	
	
	
	public URL get_userImageURL(String userID) {
		Query query_URL= new Query(Criteria.where("userID").is(userID));
		User_Bean user_Bean_URL = mongoTemplate.findOne(query_URL, User_Bean.class, "mvc");
		if (user_Bean_URL!=null) {
			URL url = oss_service.get_oss_url(user_Bean_URL.getUserImage_Name());
			return url;
		}else {
			return null;
		}
	}
	
	
	public URL get_pictureURL(String picture_Name) {
		Query query_URL= new Query(Criteria.where("picture_Name").is(picture_Name));
		Picture_Bean picture_Bean_URL = mongoTemplate.findOne(query_URL, Picture_Bean.class, "photo");
		if (picture_Bean_URL!=null) {
			URL url = oss_service.get_oss_url(picture_Bean_URL.getPicture_Name());
			return url;
		}else {
			return null;
		}
	}
	
	
}
