package com.gzonesoft.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpUtil {
	public static String sendGetBasic(String urlString, String parameter) throws IOException {
		URL url = new URL(urlString + "?"+ parameter);    
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
		conn.setRequestMethod("GET");
		conn.setRequestProperty("Content-Type", "application/xml");
		conn.setDoOutput(true); 
		conn.setDoInput(true); 
		conn.setUseCaches(false); 
		 
		// Connect to host  
		conn.connect();  
		 
		// System.out.println(conn.getResponseCode());
		 
		StringBuilder sb = new StringBuilder();
		InputStream in = conn.getInputStream();  
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));  
		String line = null;  
		boolean bfirst = true;
		while ((line = reader.readLine()) != null) {
			if (bfirst == true) {
				bfirst = false;
			} else {
				sb.append("\n");
			}

			sb.append(line);
		}  
		in.close();
		reader.close();  
		conn.disconnect();
		 
		return sb.toString();
	}

	public static String sendPostBasic(String urlString, String bodyData) throws IOException {
		URL url = new URL(urlString);    
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
		conn.setRequestMethod("POST");
		 
		conn.setRequestProperty("Content-Type", "application/xml");
		//conn.setRequestProperty("apiKey", apiKey);
		conn.setDoOutput(true); 
		conn.setDoInput(true); 
		conn.setUseCaches(false); 

		// Connect to host  
		conn.connect();  
		 
		//send data
		BufferedWriter bufferedwriter = null;
		if (bodyData != null && bodyData.length() > 0) {
			bufferedwriter = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream(), "utf-8"));
			bufferedwriter.write(bodyData);
			bufferedwriter.flush(); 
		}
		//System.out.println(conn.getResponseCode());
		 
		StringBuilder sb = new StringBuilder();
		InputStream in = conn.getInputStream();  
		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));  
		String line = null;  
		boolean bfirst = true;
		while ((line = reader.readLine()) != null) {
			if (bfirst == true) {
				bfirst = false;
			} else {
				sb.append("\n");
			}
			sb.append(line);
		}  
		in.close();
		reader.close();  
		if (bufferedwriter != null) bufferedwriter.close();
		conn.disconnect();
		 
		return sb.toString();
	}

	public static String sendGet(String urlString, String contentType, Map<String, Object> params) throws IOException {

        //http client 생성
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //파리미터 설정
        List<NameValuePair> paramList = convertParam(params);
        
        if(params != null){
        	urlString += "?" + URLEncodedUtils.format(paramList, "UTF-8");
        }

        //get 메서드와 URL 설정
        HttpGet httpGet = new HttpGet(urlString);
        //agent 정보 설정
        httpGet.addHeader("User-Agent", "Mozila/5.0");
        //httpGet.addHeader("Content-type", "application/xml");
        httpGet.addHeader("Content-type", contentType);
        //get 요청
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
        

        //response의 status 코드 출력
        System.out.println(httpResponse.getStatusLine().getStatusCode());
 
        String strResponse = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

        //System.out.println(strResponse);
        /*
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpResponse.getEntity().getContent()));
 
        String inputLine;
        StringBuffer response = new StringBuffer();
 
        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        
        reader.close();
        */
        httpClient.close();


		return strResponse; //response.toString();
	}
	
	public static String sendPost(String urlString, String contentType, String headerApiKey, Map<String, Object> params, String bodyData) throws IOException {
        //http client 생성
        CloseableHttpClient httpClient = HttpClients.createDefault();
        
        //pos 메서드와 URL 설정
        HttpPost httpPost = new HttpPost(urlString);
        //agent 정보 설정
        httpPost.addHeader("User-Agent", "Mozila/5.0");
        //httpPost.addHeader("Content-type", "application/xml");
        httpPost.addHeader("Content-type", contentType);
        
        if(headerApiKey != null) {
        	httpPost.addHeader("api_key", headerApiKey);
        }
        
        if(params != null){
        	List<NameValuePair> paramList = convertParam(params);
        	httpPost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
        }

        if(bodyData != null){
        	httpPost.setEntity(new StringEntity(bodyData, "UTF-8"));
        }
        
        //get 요청
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        
        //response의 status 코드 출력
        //System.out.println(httpResponse.getStatusLine().getStatusCode());
        
        String strResponse = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

        //System.out.println(strResponse);
        httpResponse.close(); //----------------------> 중복 데이터가 내려가는 듯
        
        httpClient.close();
        
        
		return strResponse;
	}
	
    private static List<NameValuePair> convertParam(Map<String, Object> params){
        List<NameValuePair> paramList = new ArrayList<NameValuePair>();
        Iterator<String> keys = params.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            paramList.add(new BasicNameValuePair(key, params.get(key).toString()));
        }
         
        return paramList;
    }
}
