package com.thinkit.cloud.filecopytools.config;

import java.util.List;

/**
 * 文件复制对象
 *
 */
public class FileCopyBean {

	/**
	 * 文件复制原目录
	 */
	private String sourceDir;
	
	/**
	 * 文件复制目标目录
	 */
	private String destDir;
	
	/**
	 * 需要复制的文件对象
	 */
	private String  copyFiles;
	
	/**
	 *  配置忽略目录下的文件
	 */
	private String ingoredList;
	
	/**
	 * 配置是全量，还是增量同步, all:全部, 其他增量
	 */
	private String synType;
	
	/**
	 * 文件修改时间， 默认3天前
	 */
	private int updateTime;

	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	public String getDestDir() {
		return destDir;
	}

	public void setDestDir(String destDir) {
		this.destDir = destDir;
	}

	public String getCopyFiles() {
		return copyFiles;
	}

	public void setCopyFiles(String copyFiles) {
		this.copyFiles = copyFiles;
	}

	public String getIngoredList() {
		return ingoredList;
	}

	public void setIngoredList(String ingoredList) {
		this.ingoredList = ingoredList;
	}

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
	
}
