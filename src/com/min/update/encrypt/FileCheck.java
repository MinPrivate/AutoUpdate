package com.min.update.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileCheck {

	//常量定义
	private static final String HEX_CODE = "0123456789ABCDEF";
	
	protected static MessageDigest messagedigest = null;  
	static {  
		try {  
			messagedigest = MessageDigest.getInstance("MD5");  
		} catch (NoSuchAlgorithmException nsaex) {  
			System.err.println(FileCheck.class.getName()  
					+ "初始化失败，MessageDigest不支持MD5Util。");  
			nsaex.printStackTrace();  
		}  
	}  
	
	/** 
     * 生成文件的md5校验值 
     *  
     * @param file 
     * @return 
     * @throws IOException 
     */  
    public static String getFileMD5String(String file) throws IOException {  
    	File f = new File(file);
        InputStream fis;  
        fis = new FileInputStream(f);  
        byte[] buffer = new byte[1024];  
        int numRead = 0;  
        while ((numRead = fis.read(buffer)) > 0) {  
            messagedigest.update(buffer, 0, numRead);  
        }  
        fis.close();  
        return bin2hex(messagedigest.digest());  
    }  
    
    private static String bin2hex(final byte[] b) {
        if (b == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer(2 * b.length);
        for (int i = 0; i < b.length; i++) {
            int v = (256 + b[i]) % 256;
            sb.append(HEX_CODE.charAt((v / 16) & 15));
            sb.append(HEX_CODE.charAt((v % 16) & 15));
        }
        return sb.toString();
    }
}
