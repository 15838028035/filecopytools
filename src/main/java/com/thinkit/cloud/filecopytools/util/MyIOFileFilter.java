package com.thinkit.cloud.filecopytools.util;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;

public class MyIOFileFilter implements IOFileFilter{

	/**
	 * 配置是全量，还是增量同步, all:全部, 其他增量
	 */
	private String synType;
	
	/**
	 * 文件修改时间， 默认3天前
	 */
	private int updateTime;
	
	private boolean isFilter = false;
	
	public String getSynType() {
		return synType;
	}

	public void setSynType(String synType) {
		this.synType = synType;
	}

	public int getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(int updateTime) {
		this.updateTime = updateTime;
	}
	
	public MyIOFileFilter() {
	}
	
	public MyIOFileFilter(String synType, int updateTime) {
		super();
		this.synType = synType;
		this.updateTime = updateTime;
		isFilter = true;
	}
	

	@Override
	public boolean accept(File file) {
		if(isFilter) {
			
			boolean isRun = false;
			if("all".equals(synType)) {
				isRun = true;
			}
			
			// 文件最后修改时间
			long lastModified = file.lastModified();
			long nowDate = System.currentTimeMillis();
			
			long betweenDays = (nowDate - lastModified) / (1000 * 3600 * 24);
			
			if(!"all".equalsIgnoreCase(synType) && betweenDays<updateTime) {
				isRun = true;
			}
			return isRun;
		}
		return true;
	}

	@Override
	public boolean accept(File dir, String name) {
		return true;
	}

}
