package com.min.update.net;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpRequest {

	private Logger log = LogManager.getLogger();
	public HttpResult get(String url) {
		
		HttpResult httpResult = null;
		HttpClient httpClient = new DefaultHttpClient();
		for(int i = 0; i < 5; i++){
			try{
				HttpGet httpGet = new HttpGet(url);
				HttpResponse response = httpClient.execute(httpGet);
				int code = response.getStatusLine().getStatusCode();
				String encoding = parseCharset(response);
				String result = EntityUtils.toString(response.getEntity(), encoding);
				httpResult = new HttpResult(code, result);
				break;
			}catch(Exception e){
				e.printStackTrace();
				log.error(e.getMessage());
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		httpClient.getConnectionManager().shutdown();
		return httpResult;
	}

	public HttpResult get(String url, List<NameValuePair> params){
		url += "?";
		for (NameValuePair param : params) {
			url += param.getName() + "=" + param.getValue() + "&";
		}
		url = url.substring(0, url.length() - 1);
		return get(url);
	}

	@SuppressWarnings("deprecation")
	public HttpResult post(String url, List<NameValuePair> params){
		HttpResult httpResult = null;
		HttpClient httpClient = new DefaultHttpClient();
		for(int i = 0; i < 5; i++){
			try{
				HttpPost httpPost = new HttpPost(url);
				HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
				httpPost.setEntity(entity);
				
				HttpResponse httpResp = httpClient.execute(httpPost);
				int statusCode = httpResp.getStatusLine().getStatusCode();
				String encoding = parseCharset(httpResp);
				String result = EntityUtils.toString(httpResp.getEntity(), encoding);
				httpResult = new HttpResult(statusCode, result);
				break;
			}catch(Exception e){
				e.printStackTrace();
				log.error(e.getMessage());
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		httpClient.getConnectionManager().shutdown();
		return httpResult;
	}

	private String parseCharset(HttpResponse httpResp) {
		String charset = "UTF-8";
		Header[] headers = httpResp.getAllHeaders();
		for (Header header : headers) {
			if (header.getName().equalsIgnoreCase("Content-Encoding")) {
				charset = header.getValue();
				break;
			}
		}
		return charset;
	}

	public class HttpResult {
		public int code;
		public String textResult;

		public HttpResult(int code, String result) {
			this.code = code;
			this.textResult = result;
		}
	}
}
