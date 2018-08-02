package com.lingber.service;

import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.crypto.spec.SecretKeySpec;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lingber.bean.conten_bean;

import sun.misc.BASE64Encoder;
import javax.crypto.Mac;


/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月29日 下午9:04:36 
* 类说明 :
*/
public class picture_server {

    /*
     * 计算MD5+BASE64
     */
    public static String MD5Base64(String s) {
        if (s == null)
            return null;
        String encodeStr = "";
        byte[] utfBytes = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(utfBytes);
            byte[] md5Bytes = mdTemp.digest();
            BASE64Encoder b64Encoder = new BASE64Encoder();
            encodeStr = b64Encoder.encode(md5Bytes);
        } catch (Exception e) {
            throw new Error("Failed to generate MD5 : " + e.getMessage());
        }
        return encodeStr;
    }
    /*
     * 计算 HMAC-SHA1
     */
    public static String HMACSha1(String data, String key) {
        String result;
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes());
            result = (new BASE64Encoder()).encode(rawHmac);
        } catch (Exception e) {
            throw new Error("Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }
    /*
     * 等同于javaScript中的 new Date().toUTCString();
     */
    public static String toGMTString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.UK);
        df.setTimeZone(new java.util.SimpleTimeZone(0, "GMT"));
        return df.format(date);
    }
    /*
     * 发送POST请求
     */
    public static String sendPost(String url, String body, String ak_id, String ak_secret) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        int statusCode = 200;
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "POST";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + bodyMd5 + "\n" + content_type + "\n" + date + "\n"
                    + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", accept);
            conn.setRequestProperty("content-type", content_type);
            conn.setRequestProperty("date", date);
            conn.setRequestProperty("Authorization", authHeader);
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(body);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection)conn).getResponseCode();
            if(statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection)conn).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: "+ statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }
    /*
     * GET请求
     */
    public static String sendGet(String url, String ak_id, String ak_secret) throws Exception {
        String result = "";
        BufferedReader in = null;
        int statusCode = 200;
        try {
            URL realUrl = new URL(url);
            /*
             * http header 参数
             */
            String method = "GET";
            String accept = "application/json";
            String content_type = "application/json";
            String path = realUrl.getFile();
            String date = toGMTString(new Date());
            // 1.对body做MD5+BASE64加密
            // String bodyMd5 = MD5Base64(body);
            String stringToSign = method + "\n" + accept + "\n" + "" + "\n" + content_type + "\n" + date + "\n" + path;
            // 2.计算 HMAC-SHA1
            String signature = HMACSha1(stringToSign, ak_secret);
            // 3.得到 authorization header
            String authHeader = "Dataplus " + ak_id + ":" + signature;
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", accept);
            connection.setRequestProperty("content-type", content_type);
            connection.setRequestProperty("date", date);
            connection.setRequestProperty("Authorization", authHeader);
            connection.setRequestProperty("Connection", "keep-alive");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            statusCode = ((HttpURLConnection)connection).getResponseCode();
            if(statusCode != 200) {
                in = new BufferedReader(new InputStreamReader(((HttpURLConnection)connection).getErrorStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (statusCode != 200) {
            throw new IOException("\nHttp StatusCode: "+ statusCode + "\nErrorMessage: " + result);
        }
        return result;
    }
    public static void main(String[] args) throws Exception {
        // 发送POST请求示例
        String ak_id = "#"; //用户ak
        String ak_secret = "#"; // 用户ak_secret
        String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/image/tag";
        String body = "{\"type\": \"0\", \"image_url\":\"#填入自己的\"}";   
        /*System.out.println("response body:" + sendPost(url, body, ak_id, ak_secret));*/
        picture_server picture_server = new picture_server();
        List<conten_bean>list= picture_server.test(sendPost(url, body, ak_id, ak_secret));
        conten_bean conten_bean=list.get(0);
        String value= conten_bean.getValue();
        System.out.println(value);
        
        // 发送GET请求
/*        String ak_id1 = "NMV.............5jv"; //用户ak
        String ak_secret1 = "Fgs...............3zu"; // 用户ak_secret
        String url1 = "https://shujuapi.aliyun.com/org_code/service_code/api_name?param1=xxx&param2=xxx";
        System.out.println("response body:" + sendGet(url1, ak_id1, ak_secret1));*/
    }
    
    
    
    
    public List<conten_bean> getcontent(URL url_input) {
    	String image_url = url_input.toString();
    	List<conten_bean>result= new ArrayList<>();
        // 发送POST请求示例
        String ak_id = "#"; //用户ak
        String ak_secret = "#"; // 用户ak_secret
        String url = "https://dtplus-cn-shanghai.data.aliyuncs.com/image/tag";
        String body = "{\"type\": \"0\", \"image_url\":\""+image_url+"\"}";   
        picture_server picture_server = new picture_server();
        String msg = null;
		try {
			msg = picture_server.sendPost(url, body, ak_id, ak_secret);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("识别出错！");
			e.printStackTrace();
		}
/*        List<conten_bean>list= picture_server.test(sendPost(url, body, ak_id, ak_secret));*/
      	 
      	 if (msg!=null) {
          	 JSONObject json = JSONObject.parseObject(msg);  
           	/* System.out.println(JSONObject.toJSONString(json, true)); */ 
           	 JSONArray array = json.getJSONArray("tags");
           	 result = JSON.parseArray(array.toJSONString(), conten_bean.class);
		}

   		return result;
   	}
    
    
    
    
    
    public List<conten_bean> test(String msg) {
    	 /*String image_url = url_input.toString();*/
    	 List<conten_bean>result= new ArrayList<>();
    	 JSONObject json = JSONObject.parseObject(msg);  
    	/* System.out.println(JSONObject.toJSONString(json, true)); */ 
    	 JSONArray array = json.getJSONArray("tags");
    	 result = JSON.parseArray(array.toJSONString(), conten_bean.class);
		return result;
	}
    

	
}
