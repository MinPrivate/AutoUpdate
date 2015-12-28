package com.min.update.autoupdate;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.min.update.config.UpdateConfig;
import com.min.update.config.UpdateIniEditor;
import com.min.update.msg.UpdateStep;
import com.min.update.net.DownLoadManager;
import com.min.update.operator.FileOperate;
import com.min.update.operator.RunningCheck;
import com.min.update.operator.XmlFileCompare;

public class UpdateOperation {
	
	private static final int CONNECT_TRY_COUNTS = Integer.parseInt(UpdateConfig.getConnectTryCounts());
	
	private static String applicationUrl = UpdateIniEditor.getTargetProgramUrl();
	private static String updateUrl = UpdateIniEditor.getUpdateProgramUrl();

	private static String currentUpdaterXml = UpdateIniEditor.getCurrentUpdateXmlPath();
	
	private static String workingPath = UpdateIniEditor.getWrokingPath();
	private static String tempPath = UpdateIniEditor.getTempPath();
	
	private static UpdateStep updateStep;
	private static Logger log = LogManager.getLogger();
	
	public void doUpdate() {
		
		setUpdateStep(UpdateStep.enumUpdateNone);
		Date dateStart = new Date();
		log.info("开始升级！ 时间： " + dateStart);
		
		//取出updater.ini 文件中升级配置文件updater.xml的下载路径
		String webHost = UpdateIniEditor.getWebHost();
		String xmlPath = UpdateIniEditor.getUpdateXmlPath();
		log.info("updater.xml url: " + (webHost + xmlPath));
		
		String remoteFileUrl = webHost + xmlPath;
		String tempDir = tempPath;
		String updateDir = tempDir + File.separator + "MadslClient";
		String workDir = workingPath;
		String remoteFile = null;
		if(tempDir.endsWith(File.separator)){
			remoteFile = tempDir + xmlPath;
		}else{
			remoteFile = tempDir + File.separator + xmlPath;
		}
		String localUpdaterXmlFile = currentUpdaterXml;
		
		//删除之前下载的更新文件
		FileOperate.deleteDir(updateDir);
		FileOperate.deleteFile(remoteFile);
		
		setUpdateStep(UpdateStep.enumUpdateCheckRemote);
		//下载得到updater.xml
		boolean isUpdatexml = downLoadUpdateXml(remoteFileUrl, remoteFile);
		if(!isUpdatexml){
			setUpdateStep(UpdateStep.enumUpdateCheckRemoteError);
			restart();
		}
		//对比updater.xml文件 得到更新详情并下载更新
		List<String> downloadFiles = checkUpdate(remoteFile, localUpdaterXmlFile);
		
		if(downloadFiles == null){
			setUpdateStep(UpdateStep.enumUpdateCheckRemoteError);
		}else if(downloadFiles.size() == 0){
			setUpdateStep(UpdateStep.enumUpdateEnd);
			log.info("已是最新版本！");
		}else{
			setUpdateStep(UpdateStep.enumUpdateCheckRemoteOk);
			setUpdateStep(UpdateStep.enumUpdateDownload);
			boolean success = downloanUpdateFiles(downloadFiles, webHost, updateDir);
			if(success){
				setUpdateStep(UpdateStep.enumUpdateDownloadOk);
			}else{
				setUpdateStep(UpdateStep.enumUpdateDownloadError);
				restart();
			}
		}
		
		log.info(updateUrl);
		RunningCheck.runProgram(updateUrl);
		System.exit(0);
		
		
	}
	
	public void restart(){
		log.info("准备重启。。。");
		RunningCheck.runProgram(applicationUrl);
		System.exit(0);
	}
	
	public boolean downLoadUpdateXml(String remoteFileUrl, String localFile){
		boolean success = false;
		
		DownLoadManager downLoadManager = new DownLoadManager();
		for(int i = 0; i < CONNECT_TRY_COUNTS; i++){
	        try {
				success = downLoadManager.doDownload(remoteFileUrl, localFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if(success){
	        	break;
	        }
		}
		
		return success;
	}
	
	public List<String> checkUpdate(String remoteFile, String localFile){
		//将下载的最新updater.xml 与 本地 updater.xml 比较 得出需要更新的下载
		XmlFileCompare xmlFileComp = new XmlFileCompare();
		List<String> downloadFiles = xmlFileComp.getUpdateFiles(remoteFile, localFile);
		return downloadFiles;
	}
	
	public boolean downloanUpdateFiles(List<String> downloadFiles, String webHost, String localDir){

		boolean success = true;
		Iterator<String> iter = downloadFiles.iterator(); 
		while (iter.hasNext()) { 
		    String path = iter.next(); 
		    log.info("path: " + path);

		    success = false;
		    DownLoadManager downLoadManager = new DownLoadManager();
		    for(int i = 0; i < CONNECT_TRY_COUNTS; i++){
		        try {
		        	log.info("updateFile: " + webHost + path);
		        	//得到本地保存路径 + 文件名
		        	String localFile = null;
		            if(localDir.endsWith(File.separator)){
		            	localFile = localDir + path;
		            }else{
		            	localFile = localDir + File.separator + path;
		            }
		        	success = downLoadManager.doDownload(webHost + path.replace("\\", "/"), localFile);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		        if(success){
		        	break;
		        }
		    }
		    if(!success){
		    	log.info("download failed! " + webHost + path);
		    }
		    
		    
		}
		return success;
	}

	public static UpdateStep getUpdateStep() {
		return updateStep;
	}

	public static void setUpdateStep(UpdateStep updateStep) {
		UpdateOperation.updateStep = updateStep;
	}
}
