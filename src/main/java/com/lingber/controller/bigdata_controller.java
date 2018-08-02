package com.lingber.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lingber.bean.CountUser_Bean;
import com.lingber.bean.Listcity_bean;
import com.lingber.bean.Msg_bean;
import com.lingber.bean.PictureWithUsername_Bean;
import com.lingber.bean.Picture_Bean;
import com.lingber.bean.User_Bean;
import com.lingber.bean.count_city_bean;
import com.lingber.bean.picture_content_bean;
import com.lingber.bean.wordcount_bean;
import com.lingber.service.oss_service;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月29日 下午5:47:45 
* 类说明 :
*/

@RequestMapping("/count") 
@Controller
public class bigdata_controller {
	
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private oss_service oss_service;
	
    @ResponseBody
    @RequestMapping("wordcount")
    public List<wordcount_bean> wordcount() {
    	Query query_conten =new Query(Criteria.where("picture_Class").is("01"));//只查找需要识别的图
    	System.out.println("1");
    	List<Picture_Bean> picture_Bean_contentfinds=mongoTemplate.find(query_conten, Picture_Bean.class, "photo");
    	System.out.println("2");
    	if (picture_Bean_contentfinds.size()!=0) {
    	for (Picture_Bean picture_Bean : picture_Bean_contentfinds) {
    		System.out.println("进入循环");
			picture_content_bean picture_content_bean = picture_Bean.getContent();
			String contentClass=picture_content_bean.getContent_Class();
			System.out.println(contentClass);

	    	System.out.println("3");
		    		Query query_word= new Query(Criteria.where("className").is(contentClass));
		    		wordcount_bean wordcount_bean_find=mongoTemplate.findOne(query_word, wordcount_bean.class, "word");
			    	if (wordcount_bean_find==null) {
			    			System.out.println("4");
			    			wordcount_bean wordcount_bean = new wordcount_bean(contentClass, 1);
				    		mongoTemplate.insert(wordcount_bean, "word");
					}else {
						System.out.println("5");
						Update update =new Update();
			    		mongoTemplate.upsert(query_word,update.update("confidence", wordcount_bean_find.getConfidence()+1),wordcount_bean.class,"word");
					}

			}
    	}else {
		System.out.println("没有需要识别的图片");
    	}
    	
    	//完成词频统计以后，执行查询wordcount操作
		List<wordcount_bean> wordcount_bean_finds=mongoTemplate.findAll(wordcount_bean.class, "word");
		for (wordcount_bean wordcount_bean : wordcount_bean_finds) {
			System.out.println(wordcount_bean.getClassName()+wordcount_bean.getConfidence());
		}
		mongoTemplate.dropCollection("word");
		return wordcount_bean_finds;
	}
	
	
	
/*    @ResponseBody
    @RequestMapping("wordcount")
    public List<wordcount_bean> wordcount() {
    	Query query_conten =new Query(Criteria.where("picture_Class").is("01"));//只查找需要识别的图
    	System.out.println("1");
    	List<Picture_Bean> picture_Bean_contentfinds=mongoTemplate.find(query_conten, Picture_Bean.class, "photo");
    	System.out.println("2");
    	if (picture_Bean_contentfinds.size()!=0) {
    	for (Picture_Bean picture_Bean : picture_Bean_contentfinds) {
    		System.out.println("进入循环");
			picture_content_bean picture_content_bean = picture_Bean.getContent();
			String contentClass=picture_content_bean.getContent_Class();
			System.out.println(contentClass);
			//聚合统计
	    	Aggregation aggregation3 =
	    	        Aggregation.newAggregation(
	    	                Aggregation.match(Criteria.where("picture_Class").is("01")),
	    	                Aggregation.match(Criteria.where("content.content_Class").is(contentClass)),
	    	                Aggregation.group("content_Class").count().as("confidence"));
	    	List<wordcount_bean> outputTypeCount3 =
	    	        mongoTemplate.aggregate(aggregation3, "photo", wordcount_bean.class).getMappedResults();
	    	System.out.println("3");
		    	for (wordcount_bean wordcount_bean : outputTypeCount3) {
		    		Query query_word= new Query(Criteria.where("className").is(contentClass));
		    		wordcount_bean wordcount_bean_find=mongoTemplate.findOne(query_word, wordcount_bean.class, "word");
		    		if (wordcount_bean_find==null) {//如果之前没有查询过此类的词频
		    			System.out.println("4");
		    			wordcount_bean.setClassName(contentClass);
		    			System.out.println(wordcount_bean.getClassName()+wordcount_bean.getConfidence());
			    		mongoTemplate.insert(wordcount_bean, "word");
					}
		    	 }
			}
    	}else {
		System.out.println("没有需要识别的图片");
    	}
    	
    	//完成词频统计以后，执行查询wordcount操作
		List<wordcount_bean> wordcount_bean_finds=mongoTemplate.findAll(wordcount_bean.class, "word");
		for (wordcount_bean wordcount_bean : wordcount_bean_finds) {
			System.out.println(wordcount_bean.getClassName()+wordcount_bean.getConfidence());
		}
		return wordcount_bean_finds;
	}*/
	
    
	@ResponseBody
    @RequestMapping("countAllUser")
    public Listcity_bean test(){
		List<User_Bean> find = mongoTemplate.findAll(User_Bean.class, "mvc");
		List<count_city_bean>city_beans = new ArrayList<>();
		List<String>list_cityName = new ArrayList<>();
		List<Integer>list_userNumber=new ArrayList<>();
		if(find.size()!=0){
			for (User_Bean user_Bean : find) {
				if (user_Bean.getAddress()!=null) {
					 System.out.println("进入用户总查询循环");
					 String userAdress=user_Bean.getAddress();
				     String[] arr = userAdress.split("-"); 
				     System.out.println(arr);
				     String cityName= arr[1];
				     System.out.println(cityName);
				     
		    		 Query query_city= new Query(Criteria.where("cityName").is(cityName));
		    		 count_city_bean count_city_bean_find =mongoTemplate.findOne(query_city, count_city_bean.class, "city");
		    		 if (count_city_bean_find==null) {
							count_city_bean count_city_bean = new count_city_bean(cityName, 1);
							mongoTemplate.insert(count_city_bean, "city");
					}else {
						//如果之前有查询到相同的城市，则+1
						System.out.println("1");
				    		Update update =new Update();
				    		mongoTemplate.upsert(query_city,update.update("userNumber", count_city_bean_find.getUserNumber()+1),count_city_bean.class,"city");
					}

				}
			}
	 		    //再次查询city表
	 		List<count_city_bean> count_city_finals=mongoTemplate.findAll(count_city_bean.class, "city");
	 		for (count_city_bean count_city_bean_final : count_city_finals) {
	 			System.out.println("2");
		    		list_cityName.add(count_city_bean_final.getCityName());
		    		list_userNumber.add(count_city_bean_final.getUserNumber());
		    		System.out.println("3");
			}
			
			mongoTemplate.dropCollection("city");
			System.out.println("5");
			Listcity_bean listcity_bean =new Listcity_bean(list_cityName, list_userNumber);
			return listcity_bean;
		}else{

			return null;
		}
    }

	@ResponseBody
    @RequestMapping("online")
    public Listcity_bean online(){
		//查询所有在线用户
        List<CountUser_Bean> find= mongoTemplate.findAll(CountUser_Bean.class, "count");
        List<CountUser_Bean> countUser_Beans_return=new ArrayList<>();
		List<count_city_bean>city_beans = new ArrayList<>();
		List<String>list_cityName = new ArrayList<>();
		List<Integer>list_userNumber=new ArrayList<>();
		if (find.size()!=0) {
	        for (CountUser_Bean countUser_Bean : find) {
				if (countUser_Bean.getOutloginTime()==null) {
					System.out.println(countUser_Bean.getUserID());
							//根据用户ID查询用户Adress
			    		 Query query_userID= new Query(Criteria.where("userID").is(countUser_Bean.getUserID()));
			    		 User_Bean userAdress_find =mongoTemplate.findOne(query_userID, User_Bean.class, "mvc");
							if (userAdress_find!=null) {
								 System.out.println("进入用户总查询循环");
								 String userAdress=userAdress_find.getAddress();
							     String[] arr = userAdress.split("-"); 
							     System.out.println(arr);
							     String cityName= arr[1];
							     System.out.println(cityName);
							     
					    		 Query query_city= new Query(Criteria.where("cityName").is(cityName));
					    		 count_city_bean count_city_bean_find =mongoTemplate.findOne(query_city, count_city_bean.class, "city");
					    		 if (count_city_bean_find==null) {
										count_city_bean count_city_bean = new count_city_bean(cityName, 1);
										mongoTemplate.insert(count_city_bean, "city");
								}else {
									//如果之前有查询到相同的城市，则+1
									System.out.println("1");
							    		Update update =new Update();
							    		mongoTemplate.upsert(query_city,update.update("userNumber", count_city_bean_find.getUserNumber()+1),count_city_bean.class,"city");
								}

							}
				}
			}
				 		    //再次查询city表
				 		List<count_city_bean> count_city_finals=mongoTemplate.findAll(count_city_bean.class, "city");
				 		for (count_city_bean count_city_bean_final : count_city_finals) {
				 			System.out.println("2");
					    		list_cityName.add(count_city_bean_final.getCityName());
					    		list_userNumber.add(count_city_bean_final.getUserNumber());
					    		System.out.println("3");
						}
						
						mongoTemplate.dropCollection("city");
						System.out.println("5");
						Listcity_bean listcity_bean =new Listcity_bean(list_cityName, list_userNumber);
						return listcity_bean;
		}else {
			return null;
		}
    }
	
	
}
