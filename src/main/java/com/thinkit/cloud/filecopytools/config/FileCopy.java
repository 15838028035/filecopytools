package com.thinkit.cloud.filecopytools.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.thinkit.cloud.filecopytools.util.CopyFilesUtils;
import com.thinkit.cloud.filecopytools.util.MD5Util;
import com.thinkit.cloud.filecopytools.util.MyFileUtils;
import com.thinkit.cloud.filecopytools.util.MyIOFileFilter;
import com.thinkit.cloud.filecopytools.util.NotTmpDirectoryFileFilter;
import com.thinkit.cloud.filecopytools.util.SpringContextUtil;

@Component
public class FileCopy {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	public void start() {
		
		Long startTime = System.currentTimeMillis();
		
		FileCopyBean fileCopyBean = (FileCopyBean) SpringContextUtil.getBean("fileCopyBean") ;
		
		String sourceDir = fileCopyBean.getSourceDir();
		String destDir = fileCopyBean.getDestDir();
		
		List<String> copyFiles = fileCopyBean.getCopyFiles();
		
		String ingoredList = fileCopyBean.getIngoredList();
		
		String synType = fileCopyBean.getSynType();
		
		int updateTime = fileCopyBean.getUpdateTime();
		
		 logger.info("开始进行读取文件目录了");
		   
		logger.info("文件复制原目录:{}",sourceDir);
		logger.info("文件复制目标目录:{}",destDir);
		logger.info(" 需要复制的文件对象个数:{}",copyFiles.size());
		logger.info("配置忽略目录下的文件{}",ingoredList);
		logger.info(" 配置同步类型:{}",synType);
		logger.info("文件修改时间{}",updateTime);
		
		
		File sourceDirFile = new File(sourceDir);
		
		if(sourceDirFile.isDirectory() && !sourceDirFile.exists()) {
			logger.info("文件复制原目录:{}不存在",sourceDir);
			return ;
		}
		
		if(!sourceDirFile.isDirectory()) {
			logger.info("文件复制原目录:{}不存在",sourceDir);
			return ;
		}
		
		File destDirFile = new File(destDir);
		
		if(destDirFile.isDirectory() && !destDirFile.exists()) {
			logger.info("文件复制目标目录:{}不存在",sourceDir);
			return ;
		}
		
		if(!destDirFile.isDirectory()) {
			logger.info("文件复制目标目录:{}不存在",destDirFile);
			return ;
		}
		
		List<String> errorFilePathList = new ArrayList<>();
		List<String> ingore4GFileList = new ArrayList<>();
		List<String> deleteFileList = new ArrayList<>();
		List<String> copyFileList = new ArrayList<>();
		
		copyFiles.forEach(filesubDir->{
			
			String fileDir = sourceDir + File.separator + filesubDir;
			
			File fileDirSourceFile= new File(fileDir);
			
			logger.info("开始获取{}目录下的文件列",fileDir);
			
			List <File> listFilesSource =  (List<File>)MyFileUtils.listFiles(fileDirSourceFile, new MyIOFileFilter(), NotTmpDirectoryFileFilter.INSTANCE);
			
			
			listFilesSource.forEach(file->{
				try {
					
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
					
					if(isRun) {
						
						 logger.info("开始处理判断文件{}",file.getAbsoluteFile());
						
						String destFilePath = file.getAbsolutePath().replace(sourceDir, destDir);
						
						File destFile = new File(destFilePath);
						
						boolean isCopyFile = false;
						
						CopyFilesUtils.createParentPath(destFile);
						
						if(!destFile.isDirectory() && !destFile.exists()){
								isCopyFile = true;
						}
						
						// 判断是否是忽略的文件
						
						String []ingoredListArray = ingoredList.split(",");
						
						boolean isIgnoreDir = false;
						for(String str:ingoredListArray) {
							
							if(file.getAbsolutePath().contains(str)) {
								isIgnoreDir = true;
							}
						}
						
						if(!destFile.isDirectory() && destFile.exists() && !isIgnoreDir) {
							
							long fileSizeLong = file.length();
							
							// 大于4G文件， 直接复制，不进行校验
							
							long longSize = 4000000000L;
							
							if(fileSizeLong>=longSize) {
								isCopyFile = false;
								ingore4GFileList.add(file.getAbsolutePath());
							}else {
								String fileMd5 = MD5Util.getFileMD5String(file);
								String destFileMd5 =MD5Util.getFileMD5String(destFile);
								
								if(!fileMd5.equalsIgnoreCase(destFileMd5)) {
									isCopyFile = true;
								}
							}
							
						}
						
						if(isCopyFile && !isIgnoreDir) {
							logger.info("开始复制文件,原始目录:{}, 目标目录:{}",file.getAbsolutePath(), destFile.getAbsolutePath());
							copyFileList.add(file.getAbsolutePath());
							CopyFilesUtils.copyFileUsingApacheCommonsIO(file,destFile);
						}
						
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("复制文件出现失败, 文件路径:{}",file.getAbsolutePath());
					errorFilePathList.add(file.getAbsolutePath());
				}
			});
				
		});
		
		Long endTime = System.currentTimeMillis();
		logger.info("文件复制结束了");
		logger.info("文件复制总共共花费时间:" +(endTime-startTime)/1000 + "秒");
		
		logger.info("复制出现失败的文件信息:");
		
		errorFilePathList.forEach(str-> {
			logger.info(str);
		});
		
		logger.info("忽略大于4G的文件信息:");
		
		ingore4GFileList.forEach(str-> {
			logger.info(str);
		});
		
		logger.info("文件复制结束了");
		
		logger.info("复制的文件个数:{}", copyFileList.size());
		logger.info("复制的文件信息:");
		
		copyFileList.forEach(str-> {
			logger.info(str);
		});
		
		logger.info("开始比对删除的文件");
		
		copyFiles.forEach(filesubDir-> {
			String fileDirDest = destDir + File.separator + filesubDir;
			File fileDirDestFile= new File(fileDirDest);
			List <File> listFilesDest =  (List<File>)MyFileUtils.listFiles(fileDirDestFile, new MyIOFileFilter(), NotTmpDirectoryFileFilter.INSTANCE);
			
			listFilesDest.forEach(file-> {
				try {
					String sourceFilePath = file.getAbsolutePath().replace(destDir, sourceDir);
					File sourceFile = new File(sourceFilePath);
					
					if( !sourceFile.exists()) {
						deleteFileList.add(file.getAbsolutePath());
					}
					
				} catch (Exception e) {
					e.printStackTrace();
					errorFilePathList.add(file.getAbsolutePath());
				}
			});
			
		});
		
		logger.info("删除的文件信息");
		deleteFileList.forEach(str-> {
			logger.info(str);
		});
		
		Long endTime2 = System.currentTimeMillis();
		logger.info("文件复制处理结束了");
		logger.info("文件复制总共处理时间:" +(endTime2-startTime)/1000 + "秒");
		
	}
}
