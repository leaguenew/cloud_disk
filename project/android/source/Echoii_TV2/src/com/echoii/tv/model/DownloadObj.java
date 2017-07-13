package com.echoii.tv.model;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>下载文件时，更新界面需要的信息封装类</p>
 *
 */
public class DownloadObj {
	
	private String name ; 
	private float progress;
	private int currentIndex;
	private int fileCount;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public float getProgress() {
		return progress;
	}
	
	public void setProgress(float progress) {
		this.progress = progress;
	}
	
	public int getCurrentIndex() {
		return currentIndex;
	}
	
	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}
	
	public int getFileCount() {
		return fileCount;
	}
	
	public void setFileCount(int fileCount) {
		this.fileCount = fileCount;
	}
	

}
