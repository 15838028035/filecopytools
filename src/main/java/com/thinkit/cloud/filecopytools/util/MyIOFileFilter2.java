package com.thinkit.cloud.filecopytools.util;

import java.io.File;

import org.apache.commons.io.filefilter.IOFileFilter;

public class MyIOFileFilter2 implements IOFileFilter{

	/**
	 * 文件复制目录dist
	 */
	private String destDir;
	
	/**
	 * 文件复制目的目录source
	 */
	private String sourceDir;
	
	/**
	 *  配置忽略目录下的文件
	 */
	private String ingoredList;
	
	public MyIOFileFilter2(String destDir, String sourceDir, String ingoredList) {
		super();
		this.destDir = destDir;
		this.sourceDir = sourceDir;
		this.ingoredList = ingoredList;
	}

	@Override
	public boolean accept(File file) {
		// 判断是否是忽略的文件
		String []ingoredListArray = ingoredList.split(",");
		
		boolean isIgnoreDir = false;
		for(String str:ingoredListArray) {
			if(file.getAbsolutePath().contains(str)) {
				isIgnoreDir = true;
			}
		}
		
		if(isIgnoreDir) {
			return false;
		}
		
		String sourceFilePath = file.getAbsolutePath().replace(destDir, sourceDir);
		File sourceFile = new File(sourceFilePath);
		
		if( !sourceFile.exists()) {
			return true;
		}
		
		return false;
	}

	@Override
	public boolean accept(File dir, String name) {
		return true;
	}

}
