package com.thinkit.cloud.filecopytools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RestController;

import com.thinkit.cloud.filecopytools.config.FileCopy;
import com.thinkit.cloud.filecopytools.util.SpringContextUtil;

/**
 *启动类
 */
@SpringBootApplication
@RestController
public class FileCoypToolsApplication {
	private static  Logger logger = LoggerFactory.getLogger(FileCoypToolsApplication.class);
	
	
    public static void main(String[] args) {
    	ApplicationContext applicationContext = SpringApplication.run(FileCoypToolsApplication.class, args);
    	
    	if(applicationContext == null) {
    		logger.error("applicationContext为空");
    		return ;
    	}
    	
    	SpringContextUtil.setApplicationContext(applicationContext);
     
        FileCopy fileCopy = (FileCopy) SpringContextUtil.getBean("fileCopy") ;
        
        fileCopy.start();
    }
}
