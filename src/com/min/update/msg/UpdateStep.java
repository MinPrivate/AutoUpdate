package com.min.update.msg;

public enum UpdateStep {

	/**
	 * 升级未开始 0
	 */
	enumUpdateNone(0),				//升级未开始
	/**
	 * 检查更新文件前 1
	 */
	enumUpdateCheckRemote(1),		//检查更新文件前
	/**
	 * 检查更新文件出错 2
	 */
	enumUpdateCheckRemoteError(2),	//检查更新文件出错
	/**
	 * 检查更新文件成功后 3
	 */
	enumUpdateCheckRemoteOk(3),		//检查更新文件成功后
	/**
	 * 下载文件前 4
	 */
	enumUpdateDownload(4),			//下载文件前
	/**
	 * 下载文件出错 5
	 */
	enumUpdateDownloadError(5),		//下载文件出错
	/**
	 * 下载文件成功后 6
	 */
	enumUpdateDownloadOk(6),		//下载文件成功后
	/**
	 * 检查本地文件前  7
	 */
	enumUpdateCheckLocal(7),		//检查本地文件前
	/**
	 * 检查本地文件出错 8
	 */
	enumUpdateCheckLocalError(8),	//检查本地文件出错
	/**
	 * 检查本地文件成功后 9
	 */
	enumUpdateCheckLocalOk(9),		//检查本地文件成功后
	/**
	 * 从临时目录拷贝文件前 10
	 */
	enumUpdateCopy(10),				//从临时目录拷贝文件前
	/**
	 * 从临时目录拷贝文件出错 11 
	 */
	enumUpdateCopyError(11),		//从临时目录拷贝文件出错
	/**
	 * 从临时目录拷贝文件成功后 12
	 */
	enumUpdateCopyOk(12),			//从临时目录拷贝文件成功后
	/**
	 * 升级结束 13
	 */
	enumUpdateEnd(13);				//升级结束
	
	private int nCode;
	
	private UpdateStep(int _nCode){
		this.nCode = _nCode;
	}
	
	@Override
    public String toString() {
        return String.valueOf ( this.nCode );
    }
}
