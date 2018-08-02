package com.lingber.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lingber.bean.picture_content_bean;





/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月18日 下午12:59:23 
* 类说明 :
*/
public class Client_Service {
	
	public List<String> sentAndRec_data(URL URL){
		
        Socket socket = null;
        /*int port=8080;*/
        int port=46051;
        List<String> result = new ArrayList<>();
           try {                    
               socket = new Socket("103.46.128.47", port);
               // 发送关闭命令
               String url =URL.toString();
               OutputStream socketOut = socket.getOutputStream();
               socketOut.write(url.getBytes());
               System.out.println("写完！");
               // 接收服务器的反馈
               BufferedReader br = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
               String msg = null;
               msg = br.readLine();
               if (msg!=null) {
            	   //转成JAVA数组
            	    result=JSON.parseArray(msg, String.class);
			   }
            	   /*放入识别对象操作*/
               socket.close();
           	} catch (IOException e) {                    
               e.printStackTrace();
           	}
           
           System.out.println("连接即将关闭！");
           return result;
	}
	
	public static void main(String[] args){
		
        Socket socket = null;
       // int port=8080;
          int port=46051;
       
           try {                    
        	   //http://103.46.128.47:41883/
               socket = new Socket("103.46.128.47", port);
               // 发送关闭命令
               String url ="https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=2567413480,4071038408&fm=200&gp=0.jpg/r/n";
               //String url ="https://lingber.oss-cn-shenzhen.aliyuncs.com/fish.jpg?Expires=1527596005&OSSAccessKeyId=TMP.AQFMQ9P1hmuIQU1QToQW4iRuunlkyWS-_D6oVXjpLb_wEiPTcXxngy5xzDwTAAAwLAIUdKOjEj6bqE_bChp9b8yNoLv13NYCFGoCXablHg0bac_CqFo7fZNmva5Y&Signature=uU5l5q7C7h40%2FdlIlvC3eG6BuXM%3D";
               OutputStream socketOut = socket.getOutputStream();
               socketOut.write(url.getBytes());
               System.out.println("写完！");
               // 接收服务器的反馈
               BufferedReader br = new BufferedReader(
               new InputStreamReader(socket.getInputStream()));
               String msg = null;
               while ((msg = br.readLine()) != null){
            	   List<String> result=JSON.parseArray(msg, String.class);
            	   System.out.println(result);
            	  String class_result= result.get(result.size()-1);
            	   System.out.println(class_result);
               }
               socket.close();
           	} catch (IOException e) {                    
               e.printStackTrace();
           	}
           
           System.out.println("连接即将关闭！");
           /*socket.close();*/
	}

}
