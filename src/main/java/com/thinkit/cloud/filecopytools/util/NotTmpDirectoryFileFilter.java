package com.thinkit.cloud.filecopytools.util;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.io.filefilter.AbstractFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * 自定义过滤器，过滤非tmp目录
 *
 */
@SuppressWarnings("all")
public class NotTmpDirectoryFileFilter extends AbstractFileFilter implements Serializable {

    /**
     * Singleton instance of directory filter.
     * @since 1.3
     */
    public static final IOFileFilter DIRECTORY = new NotTmpDirectoryFileFilter();
    /**
     * Singleton instance of directory filter.
     * Please use the identical DirectoryFileFilter.DIRECTORY constant.
     * The new name is more JDK 1.5 friendly as it doesn't clash with other
     * values when using static imports.
     */
    public static final IOFileFilter INSTANCE = DIRECTORY;

    /**
     * Restrictive consructor.
     */
    protected NotTmpDirectoryFileFilter() {
    }

    /**
     * Checks to see if the file is a directory.
     *
     * @param file  the File to check
     * @return true if the file is a directory
     */
    @Override
    public boolean accept(File file) {
    	Boolean isNotTmp = !file.getName().contains("tmp");
        return file.isDirectory() && isNotTmp;	
    }

}
