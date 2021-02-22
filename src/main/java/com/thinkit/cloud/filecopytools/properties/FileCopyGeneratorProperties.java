package com.thinkit.cloud.filecopytools.properties;

import java.io.IOException;
import java.util.Arrays;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import com.thinkit.cloud.filecopytools.config.FileCopyBean;
import com.thinkit.cloud.filecopytools.util.GLogger;

/**
 * 
 *  代码生成器属性配置
 *
 */
public class FileCopyGeneratorProperties {
  public static final String [] PROPERTIES_FILE_NAMES = new String[] { "application.properties" };
  public static PropertiesHelper propertiesHelper = new PropertiesHelper(new Properties(), true);

  private FileCopyGeneratorProperties() {
  }

  static {
    reload();
  }

  public static void load(String... files) throws InvalidPropertiesFormatException, IOException {
    putAll(PropertiesHelper.load(files));
  }

  public static void putAll(Properties props) {
    FileCopyGeneratorProperties.getHelper().putAll(props);
  }

  public static void clear() {
    FileCopyGeneratorProperties.getHelper().clear();
  }

  /**
   * 加载配置
   */
  public static void reload() {
    try {
      GLogger.info("Start Load application.properties from classpath:" + Arrays.toString(PROPERTIES_FILE_NAMES));
      Properties p = new Properties();
      String[] loadedFiles = PropertiesHelper.loadAllPropertiesFromClassLoader(p, PROPERTIES_FILE_NAMES);
      GLogger.info("application.properties Load Success,files:" + Arrays.toString(loadedFiles));
      setProperties(p);

      FileCopyBean fileCopyBean = new FileCopyBean();
      
      for (Iterator it = p.entrySet().iterator(); it.hasNext();) {
        Map.Entry entry = (Map.Entry) it.next();
        System.out.println("[Property] " + entry.getKey() + "=" + entry.getValue());
        
        String keyToString = entry.getKey().toString();
        
        if("copy.sourceDir".equalsIgnoreCase(keyToString)) {
        	fileCopyBean.setSourceDir(entry.getValue().toString());
        } else if("copy.destDir".equalsIgnoreCase(keyToString)) {
        	fileCopyBean.setDestDir(entry.getValue().toString());
        } else if("copy.copyFiles".equalsIgnoreCase(keyToString)) {
        	fileCopyBean.setCopyFiles(entry.getValue().toString());
        }
        else if("copy.ingoredList".equalsIgnoreCase(keyToString)) {
        	fileCopyBean.setIngoredList(entry.getValue().toString());
        } else if("copy.synType".equalsIgnoreCase(keyToString)) {
        	fileCopyBean.setSynType(entry.getValue().toString());
        } else if("copy.updateTime".equalsIgnoreCase(keyToString)) {
        	fileCopyBean.setUpdateTime(Integer.valueOf(entry.getValue().toString()));
        }
        	
      }
      
      Properties propsA = new Properties();
      propsA.put("fileCopyBean", fileCopyBean);
      putAll(propsA);

    } catch (IOException e) {
      throw new RuntimeException("Load " + PROPERTIES_FILE_NAMES + " error", e);
    }
  }

  public static Properties getProperties() {
    return getHelper().getProperties();
  }

  private static PropertiesHelper getHelper() {
    Properties fromContext = GeneratorContext.getGeneratorProperties();
    if (fromContext != null) {
      return new PropertiesHelper(fromContext, true);
    }
    return propertiesHelper;
  }

  public static String getProperty(String key, String defaultValue) {
    return getHelper().getProperty(key, defaultValue);
  }

  public static String getProperty(String key) {
    return getHelper().getProperty(key);
  }

  public static String getRequiredProperty(String key) {
    return getHelper().getRequiredProperty(key);
  }

  public static int getRequiredInt(String key) {
    return getHelper().getRequiredInt(key);
  }

  public static boolean getRequiredBoolean(String key) {
    return getHelper().getRequiredBoolean(key);
  }

  public static String getNullIfBlank(String key) {
    return getHelper().getNullIfBlank(key);
  }

  public static String[] getStringArray(String key) {
    return getHelper().getStringArray(key);
  }

  public static int[] getIntArray(String key) {
    return getHelper().getIntArray(key);
  }

  public static boolean getBoolean(String key, boolean defaultValue) {
    return getHelper().getBoolean(key, defaultValue);
  }

  public static void setProperty(String key, String value) {
    GLogger.debug("[setProperty()] " + key + "=" + value);
    getHelper().setProperty(key, value);
  }

  /**
   * 设置属性
   * @param inputProps s输入属性
   */
  public static void setProperties(Properties inputProps) {
    propertiesHelper = new PropertiesHelper(inputProps, true);
    for (Iterator it = propertiesHelper.entrySet().iterator(); it.hasNext();) {
      Map.Entry entry = (Map.Entry) it.next();
      // assertPropertyKey(entry.getKey().toString());
      GLogger.debug("[Property] " + entry.getKey() + "=" + entry.getValue());
    }
    GLogger.info("");
  }
}