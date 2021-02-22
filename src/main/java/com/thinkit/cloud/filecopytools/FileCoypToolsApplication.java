package com.thinkit.cloud.filecopytools;

import com.thinkit.cloud.filecopytools.config.FileCopy;
import com.thinkit.cloud.filecopytools.config.FileCopyBean;
import com.thinkit.cloud.filecopytools.properties.FileCopyGeneratorProperties;

/**
 *启动类
 */
public class FileCoypToolsApplication {
	
	
    public static void main(String[] args) {
    	
    	FileCopyGeneratorProperties.reload();
    	
    	FileCopyBean fileCopyBean = (FileCopyBean)FileCopyGeneratorProperties.getProperties().get("fileCopyBean");
    	
        FileCopy fileCopy = new FileCopy();
        
        fileCopy.start(fileCopyBean);
    }
}
