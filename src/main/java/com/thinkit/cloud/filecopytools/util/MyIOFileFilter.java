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
	
	/**
	 *  配置忽略目录下的文件
	 */
	private String ingoredList;
	
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
	
	public MyIOFileFilter(String synType, int updateTime, String ingoredList) {
		super();
		this.synType = synType;
		this.updateTime = updateTime;
		this.ingoredList = ingoredList;
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
			
			// 判断是否是忽略的文件
			
			String []ingoredListArray = ingoredList.split(",");
			
			boolean isIgnoreDir = false;
			for(String str:ingoredListArray) {
				if(file.getAbsolutePath().contains(str)) {
					isIgnoreDir = true;
				}
			}
			
			return isRun && !isIgnoreDir;
		}
		return true;
	}

	@Override
	public boolean accept(File dir, String name) {
		return true;
	}

}
