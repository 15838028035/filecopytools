package com.thinkit.cloud.filecopytools.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
 
public class CopyFilesUtils {
 
	public static void copyFileUsingFileStreams(File source, File dest)
			throws IOException {
		try (InputStream input = new FileInputStream(source);
				OutputStream	output = new FileOutputStream(dest); )  {
			
			byte[] buf = new byte[1024];
			int bytesRead;
			while ((bytesRead = input.read(buf)) > 0) {
				output.write(buf, 0, bytesRead);
			}
		}catch (Exception e ) {
			GLogger.error("文件流方式复制文件出现错误"+source.getAbsolutePath()  +" --> " + dest.getAbsolutePath() );
		}
	}
 
	public static void copyFileUsingFileChannels(File source, File dest)
			throws IOException {
		try (FileChannel inputChannel = new FileInputStream(source).getChannel();
				FileChannel outputChannel = new FileOutputStream(dest).getChannel();
				) {
			outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
		}catch (Exception e ) {
			GLogger.error("复制文件出现错误"+source.getAbsolutePath()  +" --> " + dest.getAbsolutePath() );
		}
		
	}
 
	public static void copyFileUsingJava7Files(File source, File dest)
			throws IOException {
		Files.copy(source.toPath(), dest.toPath());
	}
 
	public static void copyFileUsingApacheCommonsIO(File source, File dest)
			throws IOException {
		FileUtils.copyFile(source, dest);
	}
	
	/**
	 * NIO —— MappedByteBuffer 分割拷贝大文件
	 * @throws FileNotFoundException 
	 * @throws IOException
	 * 
	 * 
	 * 测试：拷贝4.96GB 电影：720p高清阿凡达178分钟加长收藏版.mkv 用时：4分钟0.1秒
	 * 机器配置：联想Z460 i5CPU 2.53GHz 4GB内存 500GB硬盘 1GB显存
	 * 
	 */
	public static void nioCopyFile(File sourcePathFile , File targetPathFile) throws FileNotFoundException, IOException {
		long before = System.currentTimeMillis();
		
		File files = sourcePathFile;	//源文件
		File filet = targetPathFile;	//目标文件
		
		long size = files.length(); 		// 文件总大小
		long copycount = size * 2 / Integer.MAX_VALUE; //获取读、写之和所占用虚拟内存 倍数
		int copynum = copycount >= 1 ? (int) copycount + 2 : (int) copycount + 1; // 根据倍数确认分割份数
 
		long countSize = Integer.MAX_VALUE  / copynum; 	//每块分割大小<每次读写的大小>															
		long lontemp = countSize;		//初始读、写大小
		try (
			FileChannel channels = new RandomAccessFile(files, "r").getChannel();		//得到映射读文件的通道
			FileChannel channelt = new RandomAccessFile(filet, "rw").getChannel();		//得到映射写文件的通道
				) {
	 
			long j = 0; // 每次循环累加字节的起始点
			MappedByteBuffer mbbs = null; // 声明读源文件对象
			MappedByteBuffer mbbt = null; // 声明写目标文件对象
			while (j < size) {
				mbbs = channels.map(FileChannel.MapMode.READ_ONLY, j, lontemp);		//每次读源文件都重新构造对象
				mbbt = channelt.map(FileChannel.MapMode.READ_WRITE, j, lontemp);	//每次写目标文件都重新构造对象
				for (int i = 0; i < lontemp; i++) {
					byte b = mbbs.get(i);			//从源文件读取字节
					mbbt.put(i, b);					//把字节写到目标文件中
				}
				System.gc();				//手动调用 GC		<必须的，否则出现异常>
				System.runFinalization();	//运行处于挂起终止状态的所有对象的终止方法。<必须的，否则出现异常>
				j += lontemp;				//累加每次读写的字节
				lontemp = size - j;			//获取剩余字节
				lontemp = lontemp > countSize ? countSize : lontemp;	//如果剩余字节 大于 每次分割字节 则 读取 每次分割字节 ，否则 读取剩余字节
			}
		
		}catch (Exception e ) {
			GLogger.error("Nio复制文件出现错误"+files.getAbsolutePath()  +" --> " + filet.getAbsolutePath() );
		}
		
		GLogger.info("MillTime : "
				+ (double) (System.currentTimeMillis() - before) / 1000 + "s");
	}
	
	/**
	 * 创建父级文件夹
	 * 
	 * @param file
	 *            完整路径文件名(注:不是文件夹)
	 */
	public static void createParentPath(File file) {
	    File parentFile = file.getParentFile();
	    if (null != parentFile && !parentFile.exists()) {
	        parentFile.mkdirs(); // 创建文件夹
	        createParentPath(parentFile); // 递归创建父级目录
	    }
	}
	
	/**
	 * 创建父级文件夹
	 * 
	 * @param file
	 *            完整路径文件名(注:不是文件夹)
	 */
	public static void createDirPath(File file) {
	    if (null != file  &&!file.exists() ) {
	    	file.mkdirs(); // 创建文件夹
	    }
	}
 
}