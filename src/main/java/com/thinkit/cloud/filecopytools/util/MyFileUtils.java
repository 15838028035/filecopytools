package com.thinkit.cloud.filecopytools.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * 自定义文件工具类
 *
 */
public class MyFileUtils {

	   public static Collection<File> listFiles(
	            File directory, IOFileFilter fileFilter, IOFileFilter dirFilter) {
	    	validateListFilesParameters(directory, fileFilter);

	        IOFileFilter effFileFilter = setUpEffectiveFileFilter(fileFilter);
	        IOFileFilter effDirFilter = setUpEffectiveDirFilter(dirFilter);

	        //Find files
	        Collection<File> files = new java.util.LinkedList<File>();
	        innerListFiles(files, directory,
	            FileFilterUtils.or(effFileFilter, effDirFilter), false);
	        return files;
	    }
	    
	    private static void validateListFilesParameters(File directory, IOFileFilter fileFilter) {
	        if (!directory.isDirectory()) {
	            throw new IllegalArgumentException("Parameter 'directory' is not a directory");
	        }
	        if (fileFilter == null) {
	            throw new NullPointerException("Parameter 'fileFilter' is null");
	        }
	    }
	    
	    private static IOFileFilter setUpEffectiveFileFilter(IOFileFilter fileFilter) {
	        return FileFilterUtils.and(fileFilter, FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE));
	    }

	    private static IOFileFilter setUpEffectiveDirFilter(IOFileFilter dirFilter) {
	        return dirFilter == null ? FalseFileFilter.INSTANCE : FileFilterUtils.and(dirFilter,
	                DirectoryFileFilter.INSTANCE);
	    }
	    
	  //-----------------------------------------------------------------------
	    /**
	     * Finds files within a given directory (and optionally its
	     * subdirectories). All files found are filtered by an IOFileFilter.
	     *
	     * @param files the collection of files found.
	     * @param directory the directory to search in.
	     * @param filter the filter to apply to files and directories.
	     * @param includeSubDirectories indicates if will include the subdirectories themselves
	     */
	    private static void innerListFiles(Collection<File> files, File directory,
	            IOFileFilter filter, boolean includeSubDirectories) {
	        File[] found = directory.listFiles((FileFilter) filter);
	        
	        List<File> foundFileList = Arrays.asList(found);
	        
	        if (found != null) {
	            for (File file : foundFileList) {
	                if (file.isDirectory()) {
	                    if (includeSubDirectories) {
	                        files.add(file);
	                    }
	                    innerListFiles(files, file, filter, includeSubDirectories);
	                } else {
	                    files.add(file);
	                }
	            }
	        }
	    }
}
