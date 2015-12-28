package com.min.update.operator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class XmlFileCompare {
	
	//需要更新下载的文件的  文件下载地址 及 文件MD5
	private List<String> fileDowns;
	
	public XmlFileCompare(){
		fileDowns = new ArrayList<String>();
	}
	
	public List<String> getUpdateFiles(String remoteXmlFile, String localXmlFile){
		File rtf = new File(remoteXmlFile);
		File lcf = new File(localXmlFile);
		return getUpdateFiles(rtf, lcf);
	}
	
	public List<String> getUpdateFiles(File remoteXmlFile, File localXmlFile){
		SAXBuilder remoteBuilder = new SAXBuilder(); 
		SAXBuilder localBuilder = new SAXBuilder();  
		//得到Document  
		Document remoteDoc = null;
		Document localDoc = null;
		try {
			remoteDoc = remoteBuilder.build(remoteXmlFile);
			localDoc = localBuilder.build(localXmlFile);
					
			Element remoteRoot = remoteDoc.getRootElement();
			Element localRoot = localDoc.getRootElement();
			
			String remoteAppver = remoteRoot.getChildText("appver");
			String localAppver = localRoot.getChildText("appver");

			
			if(remoteAppver.equals(localAppver)){
				return this.fileDowns;
			}
            
			Element remoteFiles = remoteRoot.getChild("files");
			Element localFiles = localRoot.getChild("files");
			
			
			List<Element> remoteFilesNode = remoteFiles.getChildren("file");
			List<Element> localFilesNode = localFiles.getChildren("file");
			
			Element remoteEt = null;
			Element localEt = null;
			for(int i = 0; i < remoteFilesNode.size(); i++){
				boolean isFind = false;
				remoteEt = remoteFilesNode.get(i);
            	String remoteVer = remoteEt.getChildText("ver");
            	String remotePath = remoteEt.getChildText("path");
            	
            	for(int j = 0; j < localFilesNode.size(); j++){
            		localEt = localFilesNode.get(j);
            		String localVer = localEt.getChildText("ver");
            		String localPath = localEt.getChildText("path");
            		
            		if(remotePath.equals(localPath)){
            			isFind = true;
            			if(!remoteVer.equals(localVer)){
            				fileDowns.add(remotePath);
            				break;
            			}
            		}
            	}
            	if(!isFind){
            		fileDowns.add(remotePath);
            	}
            }
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return this.fileDowns;
	}
}
