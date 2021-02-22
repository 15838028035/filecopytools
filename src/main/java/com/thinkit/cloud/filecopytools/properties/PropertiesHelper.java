package com.thinkit.cloud.filecopytools.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.thinkit.cloud.filecopytools.util.ClassHelper;
import com.thinkit.cloud.filecopytools.util.FileHelper;
import com.thinkit.cloud.filecopytools.util.GLogger;
import com.thinkit.cloud.filecopytools.util.StringHelper;


public class PropertiesHelper {
  boolean isSearchSystemProperty = false;
  Properties p;

  public PropertiesHelper(Properties p) {
    this.p = p;
  }

  public PropertiesHelper(Properties p, boolean isSearchSystemProperty) {
    this.p = resolveProperties(p);
    this.isSearchSystemProperty = isSearchSystemProperty;
  }

  public Properties getProperties() {
    return p;
  }

  public String getProperty(String key, String defaultValue) {
    String value = null;
    if (isSearchSystemProperty) {
      value = System.getProperty(key);
    }
    if (value == null || "".equals(value.trim())) {
      value = getProperties().getProperty(key);
    }
    return value == null || "".equals(value.trim()) ? defaultValue : value;
  }

  public String getProperty(String key) {
    return getProperty(key, null);
  }

  public String getRequiredProperty(String key) {
    String value = getProperty(key);
    if (value == null || "".equals(value.trim())) {
      throw new IllegalStateException("required property is blank by key=" + key);
    }
    return value;
  }

  public Integer getInt(String key) {
    if (getProperty(key) == null) {
      return null;
    }
    return Integer.parseInt(getRequiredProperty(key));
  }

  public int getInt(String key, int defaultValue) {
    if (getProperty(key) == null) {
      return defaultValue;
    }
    return Integer.parseInt(getRequiredProperty(key));
  }

  public int getRequiredInt(String key) {
    return Integer.parseInt(getRequiredProperty(key));
  }

  public String[] getStringArray(String key) {
    String v = getProperty(key);
    if (v == null) {
      return new String[0];
    } else {
      return StringHelper.tokenizeToStringArray(v, ", \t\n\r\f");
    }
  }

  public int[] getIntArray(String key) {
    String[] array = getStringArray(key);
    int[] result = new int[array.length];
    for (int i = 0; i < array.length; i++) {
      result[i] = Integer.parseInt(array[i]);
    }
    return result;
  }

  public Boolean getBoolean(String key) {
    if (getProperty(key) == null) {
      return null;
    }
    return Boolean.parseBoolean(getRequiredProperty(key));
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    if (getProperty(key) == null) {
      return defaultValue;
    }
    return Boolean.parseBoolean(getRequiredProperty(key));
  }

  public boolean getRequiredBoolean(String key) {
    return Boolean.parseBoolean(getRequiredProperty(key));
  }

  public String getNullIfBlank(String key) {
    String value = getProperty(key);
    if (value == null || "".equals(value.trim())) {
      return null;
    }
    return value;
  }

  public PropertiesHelper setProperty(String key, String value) {
    p.setProperty(key, value);
    return this;
  }

  public PropertiesHelper putAll(Properties props) {
    p.putAll(resolveProperties(props));
    return this;
  }

  public void clear() {
    p.clear();
  }

  public Set<Entry<Object, Object>> entrySet() {
    return p.entrySet();
  }

  public Enumeration<?> propertyNames() {
    return p.propertyNames();
  }

  public static Properties load(String... files) throws InvalidPropertiesFormatException, IOException {
    Properties properties = new Properties();
    for (String f : files) {
      File file = FileHelper.getFile(f);
      InputStream input = new FileInputStream(file);
      try {
        if (file.getPath().endsWith(".xml")) {
          properties.loadFromXML(input);
        } else {
          properties.load(input);
        }
        properties.putAll(properties);
      } finally {
        input.close();
      }
    }
    return properties;
  }

  public static String[] loadAllPropertiesFromClassLoader(Properties properties, String... resourceNames)
      throws IOException {
    List successLoadProperties = new ArrayList();
    for (String resourceName : resourceNames) {
      Enumeration urls = ClassHelper.getDefaultClassLoader().getResources(resourceName);
      while (urls.hasMoreElements()) {
        URL url = (URL) urls.nextElement();
        successLoadProperties.add(url.getFile());
        InputStream input = null;
        try {
          URLConnection con = url.openConnection();
          con.setUseCaches(false);
          input = con.getInputStream();
          if (resourceName.endsWith(".xml")) {
            properties.loadFromXML(input);
          } else {
            Properties loadProperties = new Properties();
            loadProperties.load(input);
            
            Iterator it = loadProperties.keySet().iterator();
            while(it.hasNext()) {
              String key = (String)it.next();
              Object value = loadProperties.get(key);
                if(properties.get(key)==null ) {
                  properties.put(key, value);
                }else {
                  GLogger.debug("PropertiesHelper loadAllPropertiesFromClassLoader key " + key + " exists");
                }
              }
            
          }
        } finally {
          if (input != null) {
            input.close();
          }
        }
      }
    }
    return (String[]) successLoadProperties.toArray(new String[0]);
  }

  private static Properties resolveProperties(Properties props) {
		/*
		 * Properties result = new Properties(); for (Object s : props.keySet()) {
		 * String sourceKey = s.toString(); String key = sourceKey; String value =
		 * props.getProperty(sourceKey); result.setProperty(key, value); } return
		 * result;
		 */
	  return props;
  }

}