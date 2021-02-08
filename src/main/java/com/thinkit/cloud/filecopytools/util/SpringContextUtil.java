package com.thinkit.cloud.filecopytools.util;

import org.springframework.context.ApplicationContext;

/**
 * SpringContext
 *
 * @author huzenghui
 * @data 2020/12/10
 */
public class SpringContextUtil {
    private static ApplicationContext applicationContext;

    /**
     * Sets application context.
     *
     * @param context the context
     */
    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    /**
     * Gets bean.
     *
     * @param beanId the bean id
     * @return the bean
     */
    public static Object getBean(String beanId) {
        return applicationContext.getBean(beanId);
    }
}
