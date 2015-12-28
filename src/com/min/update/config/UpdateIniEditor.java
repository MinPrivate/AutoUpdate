package com.min.update.config;

import com.min.update.operator.IniEditor;

public class UpdateIniEditor {
	
	private static String currentUpdaterIni = UpdateConfig.getCurrentUpdaterIni();

	public static String getWebHost(){
		String val = IniEditor.getValue("UPDATEINFO", "host", currentUpdaterIni);
		return val;
	}
	
	public static String getUpdateXmlPath(){
		String val = IniEditor.getValue("UPDATEINFO", "path", currentUpdaterIni);
		return val;
	}
	
	public static String getCurrentUpdateXmlPath(){
		String val = IniEditor.getValue("APPLICATION", "current_updater_xml", currentUpdaterIni);
		return val;
	}
	
	public static String getTempPath(){
		String val = IniEditor.getValue("APPLICATION", "temp_path", currentUpdaterIni);
		return val;
	}
	
	public static String getWrokingPath(){
		String val = IniEditor.getValue("APPLICATION", "working_path", currentUpdaterIni);
		return val;
	}
	
	public static String getTargetProgramName(){
		String val = IniEditor.getValue("APPLICATION", "targetProgramName", currentUpdaterIni);
		return val;
	}
	
	public static String getTargetProgramUrl(){
		String val = IniEditor.getValue("APPLICATION", "targetProgramUrl", currentUpdaterIni);
		return val;
	}
	
	public static String getUpdateProgramUrl(){
		String val = IniEditor.getValue("APPLICATION", "updateProgramUrl", currentUpdaterIni);
		return val;
	}
	
	public static String getClientUpdateCheckUrl(){
		String val = IniEditor.getValue("APPLICATION", "clientUpdateCheckUrl", currentUpdaterIni);
		return val;
	}
	

}
