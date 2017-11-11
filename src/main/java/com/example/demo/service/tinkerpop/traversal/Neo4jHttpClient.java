package com.example.demo.service.tinkerpop.traversal;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author liwei 2017-08-17
 *
 */
public class Neo4jHttpClient {
	private final static String NEO4J_USER = "neo4j";
	private final static String NEO4J_PASSWORD = "Neo4j";
	private final static String NEO4J_IP = "192.168.1.151";
	private final static String NEO4J_PORT = "7474";
	
	public static Object execute(String script) {
		String user = System.getProperty("neo4j_user", NEO4J_USER);
		String password = System.getProperty("neo4j_password", NEO4J_PASSWORD);
		String port = System.getProperty("neo4j_ServerPort", NEO4J_PORT);
		String ip = System.getProperty("neo4j_ServerIP", NEO4J_IP);
		
		String enc = user+":"+password;
		String basic = new BASE64Encoder().encode(enc.getBytes());
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		//配置超时时间
        RequestConfig requestConfig = RequestConfig.custom().
                setConnectTimeout(10000)//设置连接超时时间
                .setConnectionRequestTimeout(10000)// 设置请求超时时间
                .setSocketTimeout(10000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .build();
        
        String uri = "";
		try {
			uri = "http://"+ ip + ":" + port + "/tp/gremlin/execute?script=g."+URLEncoder.encode(script,"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
	    HttpGet httpRequest = new HttpGet(uri);
	    
		try {
		    //设置超时时间
		    httpRequest.setConfig(requestConfig);
        	httpRequest.addHeader("Authorization", "Basic " + basic);

        	HttpResponse httpResponse = httpClient.execute(httpRequest);
            String strResult = "";
            if(httpResponse != null){
               // System.out.println(httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                    JSONObject jsonResult = JSONObject.parseObject(strResult);
                    HashMap map = new HashMap(jsonResult);
                    
                    return map.get("results");
                    
                } else if (httpResponse.getStatusLine().getStatusCode() == 400) {
                	return "[]";
                    //strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                } else if (httpResponse.getStatusLine().getStatusCode() == 500) {
                    strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                } else {
                    strResult = "Error Response: " + httpResponse.getStatusLine().toString();
                } 
            }else{
            	
            }
            //System.out.println(strResult);
            
            return strResult.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(httpClient != null){
                    httpClient.close(); //释放资源
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
		return null;
	}
}
