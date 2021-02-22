package com.thinkit.cloud.filecopytools.util;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5Util {
	
    /**
     * 默认的密码字符串组合，apache校验下载的文件的正确性用的就是默认的这个组合
     */
    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static MessageDigest messageDigest = null;
    static {
    try {
        messageDigest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
        System.err.println(MD5Util.class.getName() + "初始化失败，MessageDigest不支持MD5Util.");
    }
    }
    
    /**
     * 计算文件的MD5
     * 
     * @param fileName
     *            文件的绝对路径
     * @return
     * @throws IOException
     */
    public String getFileMD5String(String fileName) throws IOException {
    File file = new File(fileName);
    return getFileMD5String(file);
    }
    

    /**
     * 计算文件的MD5，重载方法
     * 
     * @param file
     *            文件对象
     * @return
     * @throws IOException
     */
    public static String getFileMD5String(File file) throws IOException {
    if (!file.exists() || !file.isFile()) {
        return "";
    }
    
    String filePath = file.getAbsolutePath();
    
    try (  FileInputStream in = new FileInputStream(file);) {
	    byte[] buffer = new byte[1024 * 1024 * 10];
	    int len = 0;
	    while ((len = in.read(buffer)) > 0) {
	        messageDigest.update(buffer, 0, len);
	    }
    }catch(Exception e) {
    	 GLogger.error("获取文件md5出现错误，文件路径:"+filePath);
    }
    return bufferToHex(filePath,messageDigest.digest());
    }
    

    public  static  String bufferToHex(String filePath,byte bytes[]) {
    return bufferToHex(filePath,bytes, 0, bytes.length);
    }

    private static String bufferToHex(String filePath,byte bytes[], int m, int n) {
    StringBuffer stringbuffer = new StringBuffer(2 * n);
    int k = m + n;
    for (int l = m; l < k; l++) {
        appendHexPair(bytes[l], stringbuffer);
    }
    GLogger.info("文件路径:{0}  , 文件的md5:{1}",filePath,stringbuffer.toString());
    return stringbuffer.toString();
    }

    private static  void appendHexPair(byte bt, StringBuffer stringbuffer) {
    char c0 = hexDigits[(bt & 0xf0) >> 4];
    char c1 = hexDigits[bt & 0xf];
    stringbuffer.append(c0);
    stringbuffer.append(c1);
    }
}