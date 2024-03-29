package com.gloomyer.diff.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    //获取applicationContext
    public static ApplicationContext getContext() {
        return applicationContext;
    }

    //通过class获取Bean.
    public static <T> T getBean(String key, Class<T> clazz) {
        return getContext().getBean(key, clazz);
    }
}
