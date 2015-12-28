package com.min.update.operator;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileOperate {

	/** 
	 *  根据路径删除指定的目录或文件
	 *@param sPath  要删除的目录或文件 
	 *@return 删除成功返回 true，否则返回 false。 
	 */  
	public static boolean DeleteFolder(String sPath) {  
		boolean success = false;  
		File file = new File(sPath);  
	    // 判断目录或文件是否存在  
		if (!file.exists()) {  // 不存在返回 false  
			return success;  
		} else {  
			// 判断是否为文件  
			if (file.isFile()) {  // 为文件时调用删除文件方法  
				return deleteFile(sPath);  
			} else {  // 为目录时调用删除目录方法  
	            return deleteDir(sPath);  
	        }  
	    }  
	}  
	
	/** 
	 * 删除单个文件 
	 * @param   sPath    被删除文件的文件名 
	 * @return 单个文件删除成功返回true，否则返回false 
	 */  
	public static boolean deleteFile(String sPath) {  
	    boolean success = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        success = true;  
	    }  
	    return success;  
	}
	
	/** 
	 * 删除目录（文件夹）以及目录下的文件 
	 * @param   sPath 被删除目录的文件路径 
	 * @return  目录删除成功返回true，否则返回false 
	 */  
	public static boolean deleteDir(String sPath) {  
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!sPath.endsWith(File.separator)) {  
	        sPath = sPath + File.separator;  
	    }  
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return false;  
	    }  
	    boolean success = true;  
	    //删除文件夹下的所有文件(包括子目录)  
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	        	success = deleteFile(files[i].getAbsolutePath());  
	            if (!success) break;  
	        } //删除子目录  
	        else {  
	        	success = deleteDir(files[i].getAbsolutePath());  
	            if (!success) break;  
	        }  
	    }  
	    if (!success) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	}  
	
	
	/**
	 * 创建单个文件
	 * @param filePath 文件名
	 * @return 成功返回true 否则返回 false
	 */
	public static boolean createFile(String filePath) {
		File file = new File(filePath);
		// 判断文件是否存在
		if (file.exists()) {
			return false;
		}
		// 判断文件是否为目录
		if (file.isDirectory()) {
			return false;
		}
		// 判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			// 如果目标文件所在的文件夹不存在，则创建父文件夹
			// 判断创建目录是否成功
			if (!file.getParentFile().mkdirs()) {
				return false;
			}
		}
		try {
			// 创建目标文件
			if (file.createNewFile()) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	
	/**
	 * 创建目录
	 * @param destDirName 目录名
	 * @return 成功返回true 否则返回 false
	 */
	public static boolean createDir(String destDirName) {
		// 结尾是否以"/"结束
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		File dir = new File(destDirName);
		// 判断目录是否存在
		if (dir.exists()) {
			return false;
		}
		// 创建目标目录
		if (dir.mkdirs()) {
			return true;
		} else {
			return false;
		}
	}
	
	
	/**
	 * 复制文件 
	 * @param sourceFile 源文件名
	 * @param targetFile 目标文件名
	 * @return 成功返回true 否则返回 false
	 */
	public static boolean copyFile(String sourceFile, String targetFile){
		File source = new File(sourceFile);
		File target = new File(targetFile);
		return copyFile(source, target);
	}
	
	/**
	 * 复制文件 
	 * @param sourceFile 源文件
	 * @param targetFile 目标文件
	 * @return 成功返回true 否则返回 false
	 */
	public static boolean copyFile(File sourceFile, File targetFile){ 
		
		try{
			// 新建文件输入流并对它进行缓冲   
			FileInputStream input = new FileInputStream(sourceFile);  
			BufferedInputStream inBuff = new BufferedInputStream(input);  
			
			// 新建文件输出流并对它进行缓冲   
			FileOutputStream output = new FileOutputStream(targetFile);  
			BufferedOutputStream outBuff=new BufferedOutputStream(output);  
		          
			// 缓冲数组   
			byte[] b = new byte[1024 * 5];  
			int len;  
			while ((len = inBuff.read(b)) != -1) {  
				outBuff.write(b, 0, len);  
			}  
			// 刷新此缓冲的输出流   
			outBuff.flush();  
			
			//关闭流   
			inBuff.close();  
			outBuff.close();  
			output.close();  
			input.close();
			return true;
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}  
	
	/**
	 * 复制文件夹及子文件夹和文件
	 * @param sourceDir 源文件夹名
	 * @param targetDir 目标文件夹名
	 * @return 成功返回true 否则返回 false
	 */
	public static boolean copyDir(String sourceDir, String targetDir){  
		boolean success = true;
		// 新建目标目录  
		if(!(new File(targetDir).exists())){
			(new File(targetDir)).mkdirs();
		}
		// 获取源文件夹当前下的文件或目录   
		File[] file = (new File(sourceDir)).listFiles();
		
		if(file == null){
			return true;
		}
		
		for (int i = 0; i < file.length; i++) {  
			if (file[i].isFile()) {  
				// 源文件   
				File sourceFile = file[i];  
				// 目标文件   
				File targetFile = new File(new File(targetDir).getAbsolutePath()  
												+ File.separator + file[i].getName());
							
				if(targetFile.exists()){
					
					targetFile.delete();
				}
				success = copyFile(sourceFile,targetFile);  
				if (!success) break; 
			}  
			if (file[i].isDirectory()) {  
				// 准备复制的源文件夹   
				String sdir = sourceDir + File.separator + file[i].getName();  
				// 准备复制的目标文件夹   
				String tdir = targetDir + File.separator + file[i].getName();  
				success = copyDir(sdir, tdir);
				if (!success) break; 
			}  
		}
		return success; 
	}  
}
