package com.thinkit.cloud.filecopytools.config;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.thinkit.cloud.filecopytools.util.CopyFilesUtils;
import com.thinkit.cloud.filecopytools.util.ForEachUtils;
import com.thinkit.cloud.filecopytools.util.GLogger;
import com.thinkit.cloud.filecopytools.util.MD5Util;
import com.thinkit.cloud.filecopytools.util.MyFileUtils;
import com.thinkit.cloud.filecopytools.util.MyIOFileFilter;
import com.thinkit.cloud.filecopytools.util.MyIOFileFilter2;
import com.thinkit.cloud.filecopytools.util.NotTmpDirectoryFileFilter;

public class FileCopy {
	

	public void start(FileCopyBean fileCopyBean) {
		
		Long startTime = System.currentTimeMillis();
		
		String sourceDir = fileCopyBean.getSourceDir();
		String destDir = fileCopyBean.getDestDir();
		
		String copyFilesStr = fileCopyBean.getCopyFiles();
		
		List<String> copyFiles =Arrays.asList(copyFilesStr.split(","));
		
		String ingoredList = fileCopyBean.getIngoredList();
		
		String synType = fileCopyBean.getSynType();
		
		int updateTime = fileCopyBean.getUpdateTime();
		
		GLogger.info("===============================");
		GLogger.info("=======文件复制工具V1.0.1=======");
		GLogger.info("===============================");
		
		GLogger.info("开始进行读取文件目录了");
		   
		 GLogger.info("文件复制原目录:{0}",sourceDir);
		 GLogger.info("文件复制目标目录:{0}",destDir);
		 GLogger.info(" 需要复制的文件对象个数:{0}",copyFiles.size());
		 GLogger.info("配置忽略目录下的文件{0}",ingoredList);
		 GLogger.info(" 配置同步类型:{0}",synType);
		 GLogger.info("文件修改时间天数:{0}",String.valueOf(updateTime));
		
		File sourceDirFile = new File(sourceDir);
		
		if(sourceDirFile.isDirectory() && !sourceDirFile.exists()) {
			GLogger.info("文件复制原目录:{0}不存在",sourceDir);
			return ;
		}
		
		if(!sourceDirFile.isDirectory()) {
			GLogger.info("文件复制原目录:{0}不存在",sourceDir);
			return ;
		}
		
		File destDirFile = new File(destDir);
		
		if(destDirFile.isDirectory() && !destDirFile.exists()) {
			GLogger.info("文件复制目标目录:{0}不存在",sourceDir);
			return ;
		}
		
		if(!destDirFile.isDirectory()) {
			GLogger.info("文件复制目标目录:{0}不存在",destDirFile);
			return ;
		}
		
		List<String> errorFilePathList = new ArrayList<>();
		List<String> ingore4GFileList = new ArrayList<>();
		List<String> deleteFileList = new ArrayList<>();
		List<String> copyFileList = new ArrayList<>();
		
		copyFiles.forEach(filesubDir->{
			
			String fileDir = sourceDir + File.separator + filesubDir;
			
			File fileDirSourceFile= new File(fileDir);
			
			GLogger.info("===============================");
			GLogger.info("开始获取{0}目录下的文件列",fileDir);
			
			String destFilePathRoot = fileDir.replace(sourceDir, destDir);
			File destFileRoot = new File(destFilePathRoot);
			
			CopyFilesUtils.createDirPath(destFileRoot);
			
			List <File> listFilesSource =  (List<File>)MyFileUtils.listFiles(fileDirSourceFile, new MyIOFileFilter(synType,updateTime,ingoredList), NotTmpDirectoryFileFilter.INSTANCE);
			
			ForEachUtils.forEach(1, listFilesSource,(index, file) -> {
				try {
					
						GLogger.info("当前目录目录文件总个数:{0},开始处理判断第{1}个文件,文件路径:{2} ",String.valueOf(listFilesSource.size()),String.valueOf(index),file.getAbsoluteFile());
						
						String destFilePath = file.getAbsolutePath().replace(sourceDir, destDir);
						
						File destFile = new File(destFilePath);
						
						boolean isCopyFile = false;
						
						CopyFilesUtils.createParentPath(destFile);
						
						if(!destFile.isDirectory() && !destFile.exists()){
								isCopyFile = true;
						}
						
						if(!destFile.isDirectory() && destFile.exists()) {
							
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
						
						if(isCopyFile ) {
							GLogger.info("开始复制文件,原始目录:{0}, 目标目录:{1}",file.getAbsolutePath(), destFile.getAbsolutePath());
							copyFileList.add(file.getAbsolutePath());
							CopyFilesUtils.copyFileUsingApacheCommonsIO(file,destFile);
						}
					
				} catch (Exception e) {
					GLogger.error("复制文件出现失败, 文件路径:"+file.getAbsolutePath());
					errorFilePathList.add(file.getAbsolutePath());
				}
			});
			});
		
		Long endTime = System.currentTimeMillis();
		GLogger.info("文件复制结束了");
		GLogger.info("文件复制总共共花费时间:" +(endTime-startTime)/1000 + "秒");
		
		GLogger.info("===============================");
		GLogger.info("复制出现失败的文件信息:");
		errorFilePathList.forEach(GLogger::info);  
		
		GLogger.info("===============================");
		
		GLogger.info("忽略大于4G的文件信息:");
		
		ingore4GFileList.forEach(GLogger::info);  
		
		GLogger.info("文件复制结束了");
		
		GLogger.info("复制的文件个数:{0}", String.valueOf(copyFileList.size()));
		GLogger.info("复制的文件信息:");
		
		copyFileList.forEach(GLogger::info);  
		GLogger.info("===============================");
		
		GLogger.info("开始比对删除的文件");
		
		copyFiles.forEach(filesubDir-> {
			String fileDirDest = destDir + File.separator + filesubDir;
			File fileDirDestFile= new File(fileDirDest);
			List <File> listFilesDest =  (List<File>)MyFileUtils.listFiles(fileDirDestFile, new MyIOFileFilter2(destDir,sourceDir,ingoredList), NotTmpDirectoryFileFilter.INSTANCE);
			
			ForEachUtils.forEach(1, listFilesDest,(index, file) -> {
				try {
						deleteFileList.add(file.getAbsolutePath());
				} catch (Exception e) {
					GLogger.error("开始比对删除的文件出现失败, 文件路径:"+file.getAbsolutePath());
					errorFilePathList.add(file.getAbsolutePath());
				}
			});
		});
		
		GLogger.info("===============================");
		GLogger.info("删除的文件信息");
		
		deleteFileList.forEach(GLogger::info);  
		
		Long endTime2 = System.currentTimeMillis();
		GLogger.info("文件复制处理结束了");
		GLogger.info("文件复制总共处理时间:" +(endTime2-startTime)/1000 + "秒");
		
	}
}
