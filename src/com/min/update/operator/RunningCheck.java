package com.min.update.operator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class RunningCheck {
	
//	public static void main(String[] args){
//		if(killProgram("Madsl.exe")){
//			System.out.println("kill program success!");
//		}
//	}

	public static boolean isProgramRunning(String processName){
       
		BufferedReader bufferedReader = null;
		try {
			Process proc = Runtime.getRuntime().exec("tasklist /FI \"IMAGENAME eq "
					+ processName
					+ "\"");
			bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = bufferedReader.readLine()) != null){
				//判断是否存在
				if (line.contains(processName)) {
					return true;
				}
			}
			return false;
		}
		catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally{
        	if (bufferedReader != null){
                try{
                    bufferedReader.close();
                }catch (Exception ex){
                	ex.printStackTrace();
                }
        	}
        } 
	}
	
	public static boolean killProgram(String processName){
	       
		boolean success = false;
		try {
			Process proc = Runtime.getRuntime().exec("TASKKILL /F /IM "
					+ processName
					);
			success = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return success;
	}
	
	public static boolean runProgram(String processName){
	       
		boolean success = false;
		try {
			Process proc = Runtime.getRuntime().exec("cmd.exe /c start "
					+ processName
					);
			success = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		return success;
	}
	
	public static void restart(String processName){
		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c",  "start", "java", "-jar", "MadslClient.jar");
		try {
			Process p = pb.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
	
//	public static boolean isFileOpenning(String fileName){
//		
//		 return true;
//	}
}
