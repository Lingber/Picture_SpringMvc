package com.lingber.service;

import java.math.BigDecimal;
import java.util.Random;

/** 
* @author 作者 Lingber 414393125@qq.com: 
* @version 创建时间：2018年5月7日 下午3:21:12 
* 类说明 :
*    * @Title: randomLonLat
     * @Description: 在矩形内随机生成经纬度
     * @param MinLon：小经度  MaxLon： 最大经度   MinLat：最小纬度   MaxLat：最大纬度    type：设置返回经度还是纬度
*/
public class LonLat_random {
	
	  public Double randomLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat, String type) {
		    Random random = new Random();
		    BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
		    String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
		    db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
		    String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
		   Double Dlon= Double.parseDouble(lon);
		   Double Dlat= Double.parseDouble(lat);
		    if (type.equals("Lon")) {
		      return Dlon;
		    } else {
		      return Dlat;
		    }
		  }
	  
	  
	  
	  
	  //private static double EARTH_RADIUS = 6378.137;  
	    private static double EARTH_RADIUS = 6371.393;  
	    private static double rad(double d)  
	    {  
	       return d * Math.PI / 180.0;  
	    }  
	  
	    /** 
	     * 计算两个经纬度之间的距离 
	     * @param lat1 
	     * @param lng1 
	     * @param lat2 
	     * @param lng2 
	     * @return 
	     */  
	    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)  
	    {  
	       double radLat1 = rad(lat1);  
	       double radLat2 = rad(lat2);  
	       double a = radLat1 - radLat2;      
	       double b = rad(lng1) - rad(lng2);  
	       double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +   
	        Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));  
	       s = s * EARTH_RADIUS;  
	       s = Math.round(s * 1000);  
	       return s;  
	    }  
	      
	      
	      
	    public static void main(String[] args) {  
	        System.out.println(LonLat_random.GetDistance(25.31740207248264,110.41584635416666,25.31740207248264,110.41584635416666));  
	    }  

}
