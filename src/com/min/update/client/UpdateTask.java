package com.min.update.client;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.min.update.autoupdate.UpdateOperation;

public class UpdateTask extends TimerTask{

	private  Logger log = LogManager.getLogger();
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try{
			
			//检查系统是否有新版本
			Update update = new Update();
			boolean hasUpdate = update.hasUpdate();
			log.info("是否需要版本更新: " + hasUpdate);
			//如果有软件新版本则进行升级操作
			if(hasUpdate){
				UpdateOperation updateOperation = new UpdateOperation();
				updateOperation.doUpdate();
//				this.cancel();
			}else{
				log.info("已是最新版本");
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
