package com.min.update.net;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.concurrent.CountDownLatch;

import com.min.update.config.UpdateConfig;
import com.min.update.operator.FileOperate;
 
/**
 * 文件下载管理类
 */
public class DownLoadManager {
    /**
     *每个线程下载的字节数
     */
    private static long UNIT_SIZE = Long.parseLong(UpdateConfig.getDownloadUnitSize());
    
    private CountDownLatch latch = null;
    
    /**
     * 下载资源
     */
    private URL url;
	/**
     * 下载资源大小
     */
    private long fileSize = 0;
	/**
     * 已下载资源大小
     */
    private long[] fileDownloaded = new long[1]; 
	/**
     *本地存储路径+文件名
     */
	private String localFile = null;
	/**
	 * 下载速率
	 */
	private double rate = 0;
	private long tempSize = 0;
 
	/**
	 * 结束标志
	 */
	
	private FinishFlag finishFlag;
    
	/**
	 * 启动多个线程下载文件  供外调用
	 * @param remoteFileUrl		文件资源地址
	 * @param localDir			本地保存路径
	 * @throws IOException
	 */
    public boolean doDownload(String remoteFileUrl, String localFile)
            throws IOException {
       
    	boolean success = false;
    	
    	this.url = new URL(remoteFileUrl);
    	
        //得到文件大小
        this.fileSize = this.getRemoteFileSize(remoteFileUrl);
        UNIT_SIZE = this.fileSize;
        
        System.out.println("FileSize = " + this.fileSize);
 
        if(this.fileSize == 0){
            return false;
        }
       
        this.localFile = localFile;
        
        finishFlag = new FinishFlag(false);
        
        //创建本地文件
        FileOperate.createFile(this.localFile);
        System.out.println("creat file: " + this.localFile);
        this.createFile(this.localFile, this.fileSize);
        //计算线程
        long threadCount = this.fileSize / UNIT_SIZE;
        this.latch = new CountDownLatch((this.fileSize % UNIT_SIZE == 0 ? (int)threadCount : (int)threadCount + 1));
        System.out.println("共启动 "
                + (this.fileSize % UNIT_SIZE == 0 ? threadCount : threadCount + 1)
                + " 个线程");
 
        //开始下载。。。
        long offset = 0;
        int id = 1;
        
        Calendar dateT1 = Calendar.getInstance();			//计算实时速率
        
        if (this.fileSize <= UNIT_SIZE) {// 如果远程文件尺寸小于等于unitSize
            DownloadThread downloadThread = new DownloadThread(this, this.latch, id++, this.fileDownloaded,
            		this.localFile, offset, this.fileSize, finishFlag);
            downloadThread.start();
        } else {//  如果远程文件尺寸大于unitSize
            for (int i = 1; i <= threadCount; i++) {
                DownloadThread downloadThread = new DownloadThread(this, this.latch, id++, this.fileDownloaded,
                		this.localFile, offset, UNIT_SIZE, finishFlag);
                downloadThread.start();
 
                offset = offset + UNIT_SIZE;
            }
            if (this.fileSize % UNIT_SIZE != 0) {// 如果不能整除，则需要再创建一个线程下载剩余字节
                DownloadThread downloadThread = new DownloadThread(this, this.latch, id++, this.fileDownloaded,
                		this.localFile, offset, this.fileSize
                                - UNIT_SIZE * threadCount, finishFlag);
                downloadThread.start();
 
            }
        }
        
        //计算速率
       // System.out.println("CalRate Start...");
		while((this.finishFlag.getFinishFlag() != true) && (this.fileDownloaded[0]) < (this.fileSize) ){
    		Calendar dateT2 = Calendar.getInstance();			//计算实时速率
            double stick = ( dateT2.getTimeInMillis() - dateT1.getTimeInMillis() ) / 1000.0;
    		   if(stick > 1){
    			   this.rate = ((this.fileDownloaded[0] - this.tempSize) / stick)/1024.0;
    			   System.out.println("rateUp(KB/s) = " + this.rate);
    			   
    			   this.tempSize = this.fileDownloaded[0];
    			   dateT1 = dateT2;
    		   }
		}

        try {//下载完成。。。
			this.latch.await();
			
			if((this.fileDownloaded[0]) < (this.fileSize)){
				success = false;
				System.out.println("Download failed!");
			}else{
				success = true;
				System.out.println("fileDownloaded is: " + this.fileDownloaded[0]);
				System.out.println("Download Finished!");
			}
			

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
        return success;
    }
 
    
    /**
     *  获取远程文件尺寸
     * @param remoteFileUrl		文件资源地址
     * @return					返回文件大小
     * @throws IOException
     */
    private long getRemoteFileSize(String remoteFileUrl) throws IOException {

        HttpURLConnection httpConnection = (HttpURLConnection) new URL(
                remoteFileUrl).openConnection();
        return httpConnection.getContentLengthLong();
    }
 
    
    /**
     * 创建指定大小的文件
     * @param fileName			本地保存 文件路径 + 文件名
     * @param fileSize			文件大小
     * @throws IOException
     */
    private void createFile(String fileName, long fileSize) throws IOException {
        File newFile = new File(fileName);
        //System.out.println("localFile: " + fileName);
        RandomAccessFile raf = new RandomAccessFile(newFile, "rw");
 
        raf.setLength(fileSize);
 
        raf.close();
    }
    
    
    /**
     * 下载资源
     */
	public URL getUrl(){
		return this.url;
	}
	/**
     *下载资源大小
     */
	public long getFileSize(){
		return this.fileSize;
	}
	/**
     * 已下载资源大小
     */
	public long getFileDownloaded(){
		return this.fileDownloaded[0];
	}
	/**
     * 下载速率
     */
	public double getRate(){
		return this.rate;
	}
 
}