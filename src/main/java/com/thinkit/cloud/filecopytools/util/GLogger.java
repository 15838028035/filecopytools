package com.thinkit.cloud.filecopytools.util;

import java.text.MessageFormat;

/**
 * 
 * 生成器日志类
 *
 */
public class GLogger {
	
  private static final int DEBUG = 1;
  private static final int INFO = 5;
  private static final int ERROR = 10;
  private static final int WARN = 15;
  public static int logLevel = INFO;
  
  private GLogger() {
	  throw new IllegalStateException("Utility class");
  }
  

  /**
   * 打印
   * 
   * @param s
   *          字符信息
   */
  public static void debug(String s) {
    if (logLevel <= DEBUG) {
    	printlnInfo("[CopyInfo DEBUG] " + s);
    }
  }

  /**
   * 打印
   * 
   * @param s
   *          字符信息
   */
  public static void info(String s) {
    if (logLevel <= INFO) {
    	printlnInfo("[CopyInfo INFO] " + s);
    }
  }
  
  /**
   * 打印
   * 
   * @param s
   *          字符信息
   */
  public static void info(String s, Object... arguments) {
	  
	  String formattedMessage;
      if (arguments != null) {
          formattedMessage = MessageFormat.format(s, arguments);
      } else {
          formattedMessage = s;
      }
      
    if (logLevel <= INFO) {
    	printlnInfo("[CopyInfo INFO] " + formattedMessage);
    }
  }

  /**
   * 打印
   * 
   * @param s
   *          字符信息
   */
  public static void warn(String s) {
    if (logLevel <= WARN) {
    	printlnErrorInfo("[CopyInfo WARN] " + s);
    }
  }

  /**
   * 打印
   * 
   * @param s
   *          字符信息
   */
  public static void warn(String s, Throwable e) {
    if (logLevel <= WARN) {
    	printlnErrorInfo("[CopyInfo WARN] " + s);
      e.printStackTrace();
    }
  }

  /**
   * 打印
   * 
   * @param s
   *          字符信息
   */
  public static void error(String s) {
    if (logLevel <= ERROR) {
    	printlnErrorInfo("[CopyInfo ERROR] " + s);
    }
  }

  /**
   * 打印
   * 
   * @param s
   *          字符信息
   */
  public static void error(String s, Throwable e) {
    if (logLevel <= ERROR) {
      System.err.println("[CopyInfo ERROR] " + s);
      e.printStackTrace();
    }
  }
  
  private static void printlnInfo(String msg) {
	  System.out.println(msg);
  }
  
  private static void printlnErrorInfo(String msg) {
	  System.err.println(msg);
  }
}