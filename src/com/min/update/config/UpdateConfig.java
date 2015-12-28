package com.min.update.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class UpdateConfig {

	/* property file identifier */
	private static String CONFIG_FILE = "com/min/update/config/updateConfig";
	//所使用的ResourceBundle
	private static ResourceBundle bundle;
	
	//静态初始化段，用于加载属性文件
	static {
		try {
			bundle = ResourceBundle.getBundle(CONFIG_FILE);
		} catch (MissingResourceException e) {
			e.printStackTrace();
		}
	}

	//静态私有方法，用于从属性文件中取得属性值
	private static String getValue(String key) {
		return bundle.getString(key);
	}

	//获取网络连接尝试次数
	public static String getConnectTryCounts() {
		String val = getValue("connectTryCounts");
		return val;
	}
	
	//获取网络下载buffer大小
	public static String getDownloadBufferSize() {
		String val = getValue("download_buffer_size");
		return val;
	}

	//获取网络下载线程下载数据单元大小
	public static String getDownloadUnitSize() {
		String val = getValue("download_unit_size");
		return val;
	}
	
	//获得当前升级配置 ini 文件
	public static String getCurrentUpdaterIni(){
		String val = getValue("current_updater_ini");
		return val;
	}
}
