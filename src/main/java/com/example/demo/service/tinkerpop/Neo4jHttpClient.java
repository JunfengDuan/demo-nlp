package com.example.demo.service.tinkerpop;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import sun.misc.BASE64Encoder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author liwei 2017-08-17
 *
 */
@Component
public class Neo4jHttpClient {

    private static Logger logger = LoggerFactory.getLogger(Neo4jHttpClient.class);
    @Value("${neo4jIp:localhost:7474}")
    private String neo4jIp;
    @Value("${neo4jUsername:neo4j}")
    private String neo4jUsername;
    @Value("${neo4jPassword:Neo4j}")
    private String neo4jPassword;

    private CloseableHttpClient httpClient;
    private RequestConfig requestConfig;
    private String gremlinUrl;
    private String basic;

    @PostConstruct
    private void initNeo4jClient(){

        gremlinUrl = "http://"+ neo4jIp + "/tp/gremlin/execute?script=";
        String enc = neo4jUsername +":"+ neo4jPassword;
        basic = new BASE64Encoder().encode(enc.getBytes());

        //配置超时时间
        requestConfig = RequestConfig.custom().
                setConnectTimeout(10000)//设置连接超时时间
                .setConnectionRequestTimeout(10000)// 设置请求超时时间
                .setSocketTimeout(10000)
                .setRedirectsEnabled(true)//默认允许自动重定向
                .build();


    }
	
	public Object execute(String script) {
        String uri = "";
		try {
			uri = gremlinUrl + URLEncoder.encode(script,"UTF-8");
			logger.debug("GremlinUri is {}",uri);
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

        httpClient = HttpClients.createDefault();
	    HttpGet httpRequest = new HttpGet(uri);
        Object result = new Object();
	    
		try {
		    //设置超时时间
		    httpRequest.setConfig(requestConfig);
        	httpRequest.addHeader("Authorization", "Basic " + basic);

        	HttpResponse httpResponse = httpClient.execute(httpRequest);
            if(httpResponse != null){
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                if ( statusCode == 200) {
                    String strResult = EntityUtils.toString(httpResponse.getEntity());
                    JSONObject jsonResult = JSONObject.parseObject(strResult);
                    result = jsonResult.get("results");

                }else{
                    result = new JSONArray();
                }

            }

            return result;
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
