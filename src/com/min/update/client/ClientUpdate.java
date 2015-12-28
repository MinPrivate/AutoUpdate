package com.min.update.client;

import java.io.File;
import java.util.Date;

import com.min.update.config.UpdateIniEditor;
import com.min.update.msg.UpdateStep;
import com.min.update.operator.FileOperate;
import com.min.update.operator.RunningCheck;

public class ClientUpdate {
	
	private static String applicationUrl = UpdateIniEditor.getTargetProgramUrl();
	
	private static String currentUpdaterXml = UpdateIniEditor.getCurrentUpdateXmlPath();
	
	private static String workingPath = UpdateIniEditor.getWrokingPath();
	private static String tempPath = UpdateIniEditor.getTempPath();
	
	private static UpdateStep updateStep;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		update();
		
	}
	
	public static void update(){
		
		String tempDir = tempPath;
		String xmlPath = UpdateIniEditor.getUpdateXmlPath();
		String updateDir = tempDir + File.separator + "MadslClient";
		String workDir = workingPath;
		String remoteFile = null;
		if(tempDir.endsWith(File.separator)){
			remoteFile = tempDir + xmlPath;
		}else{
			remoteFile = tempDir + File.separator + xmlPath;
		}
		String localUpdaterXmlFile = currentUpdaterXml;
		
		setUpdateStep(UpdateStep.enumUpdateCheckLocalOk);
//		进行更新文件替换
		setUpdateStep(UpdateStep.enumUpdateCopy);
		
		System.out.println(new File(workDir).getAbsolutePath());
		boolean success = FileOperate.copyDir(updateDir, workDir);
		if(success){
			setUpdateStep(UpdateStep.enumUpdateCopyOk);
		}else{
			setUpdateStep(UpdateStep.enumUpdateCopyError);
		}
		//删除下载的更新文件
		FileOperate.deleteDir(updateDir);
		//更新updater.xml文件
		FileOperate.copyFile(remoteFile, localUpdaterXmlFile);
		FileOperate.deleteFile(remoteFile);
		
		setUpdateStep(UpdateStep.enumUpdateEnd);
		
		Date dateend = new Date();
		System.out.println("升级完成！完成时间：" + dateend);
		
		RunningCheck.runProgram(applicationUrl);

	}
	
	public static void setUpdateStep(UpdateStep updateStep) {
		ClientUpdate.updateStep = updateStep;
	}
}
