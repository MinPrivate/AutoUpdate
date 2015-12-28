package com.min.update.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.min.update.config.UpdateIniEditor;
import com.min.update.net.HttpRequest;
import com.min.update.net.HttpRequest.HttpResult;

public class Update { 
	
	private static String clientUpdateCheckUrl = UpdateIniEditor.getClientUpdateCheckUrl();
	private static String currentUpdateXml = UpdateIniEditor.getCurrentUpdateXmlPath();	
	private static Logger log = LogManager.getLogger();

	public boolean hasUpdate(){
		boolean update = false;
		
		String urlPath = clientUpdateCheckUrl;
		String currentVer = this.getCurrentVer();
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("appver",  currentVer));
		
		HttpRequest hr = new HttpRequest();
		HttpResult result = null;
		result = hr.post(urlPath, params);
		
		if(result == null){
			return update;
		}
		
		if(result.code == 200){
			String resultText = result.textResult.trim();
			if(resultText.equals("update")){
				update = true;
			}
		}
		
		return update;
	}
	
	private String getCurrentVer(){
		String currentVer = null;
		
		SAXBuilder builder = new SAXBuilder();  
		//得到Document  
		Document doc = null;
		try {
			doc = builder.build(currentUpdateXml);
			Element root = doc.getRootElement();
			
			currentVer = root.getChildText("appver");
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return currentVer;
	}
	
	public String getDescription(){
		String description = "";
		
		SAXBuilder builder = new SAXBuilder();  
		//得到Document  
		Document doc = null;
		try {
			doc = builder.build(currentUpdateXml);
			Element root = doc.getRootElement();
			Element des = root.getChild("descriptions");
			List<Element> dess = des.getChildren("description");
			Element e = null;
			for(int i = 0; i < dess.size(); i++){
				e = dess.get(i);
				description += e.getTextTrim() + "\r\n";
			}
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return description;
	}
}
