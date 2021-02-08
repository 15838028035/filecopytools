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
        e.printStackTrace();
    }
    }

    public static void  main(String []args) throws Exception{
    	File file = new File("d:\\soft\\Windows.iso");
    	String md5 = MD5Util.getFileMD5String(file);
    	System.out.println("md5==" + md5);
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
    File f = new File(fileName);
    return getFileMD5String(f);
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
    
    FileInputStream in = new FileInputStream(file);
    byte[] buffer = new byte[1024 * 1024 * 10];
    int len = 0;
    while ((len = in.read(buffer)) > 0) {
        messageDigest.update(buffer, 0, len);
    }
    in.close();
    return bufferToHex(messageDigest.digest());
    }

    private static  String bufferToHex(byte bytes[]) {
    return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
    StringBuffer stringbuffer = new StringBuffer(2 * n);
    int k = m + n;
    for (int l = m; l < k; l++) {
        appendHexPair(bytes[l], stringbuffer);
    }
    System.out.println("文件MD5值:" + stringbuffer);
    return stringbuffer.toString();
    }

    private static  void appendHexPair(byte bt, StringBuffer stringbuffer) {
    char c0 = hexDigits[(bt & 0xf0) >> 4];
    char c1 = hexDigits[bt & 0xf];
    stringbuffer.append(c0);
    stringbuffer.append(c1);
    }
}