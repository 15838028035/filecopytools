package com.thinkit.cloud.filecopytools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;

public class FileHelper {
  public static String getRelativePath(File baseDir, File file) {
    if (baseDir.equals(file))
      return "";
    if (baseDir.getParentFile() == null)
      return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length());
    return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length() + 1);
  }


  public static File getFile(String file) {
    if (StringHelper.isBlank(file)) {
      throw new IllegalArgumentException("'file' must be not blank");
    }
    try {
      if (file.startsWith("classpath:")) {
        return getFileByClassLoader(file.substring("classpath:".length()));
      } else {
        return new File(toFilePathIfIsURL(new File(file)));
      }
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e.toString(), e);
    } catch (IOException e) {
      throw new RuntimeException("getFile() error,file:" + file, e);
    }
  }

  public static InputStream getInputStream(String file) throws FileNotFoundException {
    InputStream inputStream = null;
    if (file.startsWith("classpath:")) {
      inputStream = ClassHelper.getDefaultClassLoader().getResourceAsStream(file.substring("classpath:".length()));
    } else {
      inputStream = new FileInputStream(file);
    }
    return inputStream;
  }

  public static File mkdir(String dir, String file) {
    if (dir == null)
      throw new IllegalArgumentException("dir must be not null");
    File result = new File(dir, file);
    parentMkdir(result);
    return result;
  }

  public static File parentMkdir(String file) {
    if (file == null)
      throw new IllegalArgumentException("file must be not null");
    File result = new File(file);
    parentMkdir(result);
    return result;
  }

  public static void parentMkdir(File outputFile) {
    File parentFile = outputFile.getParentFile();
    if (parentFile != null && !parentFile.equals(outputFile)) {
      parentFile.mkdirs();
    }
  }

  public static File getFileByClassLoader(String resourceName) throws IOException {
    String pathToUse = resourceName;
    if (pathToUse.startsWith("/")) {
      pathToUse = pathToUse.substring(1);
    }
    Enumeration<URL> urls = ClassHelper.getDefaultClassLoader().getResources(pathToUse);
    while (urls.hasMoreElements()) {
      return new File(urls.nextElement().getFile());
    }
    urls = FileHelper.class.getClassLoader().getResources(pathToUse);
    while (urls.hasMoreElements()) {
      return new File(urls.nextElement().getFile());
    }
    urls = ClassLoader.getSystemResources(pathToUse);
    while (urls.hasMoreElements()) {
      return new File(urls.nextElement().getFile());
    }
    throw new FileNotFoundException("classpath:" + resourceName);
  }

  public static String getExtension(String filename) {
    if (filename == null) {
      return null;
    }
    int index = filename.indexOf(".");
    if (index == -1) {
      return "";
    } else {
      return filename.substring(index + 1);
    }
  }

  public static void deleteDirectory(File directory) throws IOException {
    if (!directory.exists()) {
      return;
    }

    cleanDirectory(directory);
    if (!directory.delete()) {
      String message = "Unable to delete directory " + directory + ".";

      throw new IOException(message);
    }
  }

  public static boolean deleteQuietly(File file) {
    if (file == null)
      return false;
    try {
      if (file.isDirectory())
        cleanDirectory(file);
    } catch (Exception e) {
    }
    try {
      return file.delete();
    } catch (Exception e) {
    }
    return false;
  }

  public static void cleanDirectory(File directory) throws IOException {
    if (!directory.exists()) {
      String message = directory + " does not exist";
      throw new IllegalArgumentException(message);
    }

    if (!directory.isDirectory()) {
      String message = directory + " is not a directory";
      throw new IllegalArgumentException(message);
    }

    File[] files = directory.listFiles();
    if (files == null) {
      throw new IOException("Failed to list contents of " + directory);
    }

    IOException exception = null;
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      try {
        forceDelete(file);
      } catch (IOException ioe) {
        exception = ioe;
      }
    }

    if (null != exception)
      throw exception;
  }

  public static void forceDelete(File file) throws IOException {
    if (file.isDirectory()) {
      deleteDirectory(file);
    } else {
      boolean filePresent = file.exists();
      if (!file.delete()) {
        if (!filePresent) {
          throw new FileNotFoundException("File does not exist: " + file);
        }
        String message = "Unable to delete file: " + file;

        throw new IOException(message);
      }
    }
  }

  public static String toFilePathIfIsURL(File file) {
    try {
      return new URL(((File) file).getPath()).getPath();
    } catch (MalformedURLException e) {
      // ignore,fallback to file.getPath()
      return file.getPath();
    }
  }

  public static String getTempDir() {
    return System.getProperty("java.io.tmpdir");
  }
}