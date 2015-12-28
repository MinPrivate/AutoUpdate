package com.min.update.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import com.min.update.config.UpdateConfig;
 
/**
 * 负责文件下载的类
 */
public class DownloadThread extends Thread {
	
	private static final int BUFFER_SIZE = Integer.parseInt(UpdateConfig.getDownloadBufferSize());
    /**
     *待下载的文件
     */
    private URL url = null;
    
    /**
     * 待下载的文件线程id
     */
    private int id = 1;
 
    /**
     *本地文件名
     */
    private String fileName = null;
 
    /**
     * 偏移量
     */
    private long offset = 0;
 
    /**
     * 分配给本线程的下载字节数
     */
    private long length = 0;

	private long[] fileDownloaded;
	
	private final CountDownLatch latch;
	
	private FinishFlag finishFlag;
	
 
	/**
     * @param url 下载文件地址
     * @param fileName 另存文件名
     * @param offset 本线程下载偏移量
     * @param length 本线程下载长度
     * 
     * */
    public DownloadThread(DownLoadManager manager, CountDownLatch latch, int id, long[] fileDownloaded, String file, long offset, long length, FinishFlag finishFlag) {
        this.url = manager.getUrl();
        this.latch = latch;
        this.id = id;
        this.fileDownloaded = fileDownloaded;
        this.fileName = file;
        this.offset = offset;
        this.length = length;
        this.finishFlag = finishFlag;
        System.out.println("线程: " + this.id + " 开始下载。。。");
        System.out.println("线程: " + this.id + " 偏移量=" + offset + ";字节数=" + length);
    }
 
    public void run() {
        try {
            HttpURLConnection httpConnection = (HttpURLConnection) 
                    this.url.openConnection();
 
            httpConnection.setRequestMethod("GET");
            httpConnection.setRequestProperty("RANGE", "bytes=" + this.offset
                    + "-" + (this.offset + this.length - 1));
           
            //System.out.println("线程: " + this.id + " RANGE bytes=" + this.offset + "-" + (this.offset + this.length - 1));
            
            BufferedInputStream bis = new BufferedInputStream(httpConnection
                    .getInputStream());
 
            byte[] buff = new byte[BUFFER_SIZE];
            int bytesRead;
            File newFile = new File(fileName);
            RandomAccessFile raf = new RandomAccessFile(newFile, "rw");
           
           
            while ((bytesRead = bis.read(buff, 0, buff.length)) != -1) {
                raf.seek(this.offset);
                raf.write(buff, 0, bytesRead);
                this.fileDownloaded[0] += bytesRead;
                this.offset = this.offset + bytesRead;
            }
           
            raf.close();
            bis.close();
            this.latch.countDown();
            System.out.println("线程: " + this.id + " 下载完成！");
            
            this.finishFlag.setFinishFlag(true);
            
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
            this.finishFlag.setFinishFlag(true);
            this.latch.countDown();
            System.out.println("download thread failed! finisheFlag: " + finishFlag.getFinishFlag());
            
        }
        
 
    }
 
}

