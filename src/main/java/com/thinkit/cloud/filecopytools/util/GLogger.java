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

  /**
   * 打印
   * 
   * @param s
   *          字符信息
   */
  public static void debug(String s) {
    if (logLevel <= DEBUG) {
      System.out.println("[Generator DEBUG] " + s);
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
      System.out.println("[Generator INFO] " + s);
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
      System.out.println("[Generator INFO] " + formattedMessage);
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
      System.err.println("[Generator WARN] " + s);
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
      System.err.println("[Generator WARN] " + s);
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
      System.err.println("[Generator ERROR] " + s);
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
      System.err.println("[Generator ERROR] " + s);
      e.printStackTrace();
    }
  }
}