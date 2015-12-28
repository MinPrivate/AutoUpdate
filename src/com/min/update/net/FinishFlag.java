package com.min.update.net;

public class FinishFlag {
	private  boolean isFinish = false;
	
	public FinishFlag(boolean b){
		this.isFinish = b;
	}
	
	public void setFinishFlag(boolean b){
		this.isFinish = b;
	}
	
	public boolean getFinishFlag(){
		return this.isFinish;
	}
	
}
