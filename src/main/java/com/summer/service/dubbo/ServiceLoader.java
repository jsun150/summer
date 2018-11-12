package com.summer.service.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.summer.service.classLoader.JarLoader;
import javassist.CtClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jook
 * @create 2018-10-20 11:22
 **/
@Component
public class ServiceLoader implements ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLoader.class);
    private ApplicationContext applicationContext;

    @Autowired
    private InvokeInstance invokeInstance;

    /**
     * 加载dubbo服务
     *
     * @param url
     */
    public void loaderService(URL url) {
        ReferenceConfig config = null;
        try {
            String inter = url.getParameter(com.alibaba.dubbo.common.Constants.INTERFACE_KEY);
            config = ReferenceConfigFactory.getConfig(url, inter, applicationContext);
            invokeInstance.createInstance(url, config, inter);
        } catch (Exception e) {
            ReferenceConfigCache.getCache().destroy(config);
            logger.error("ServiceLoader loaderService error", e);
            throw e;
        }
    }

    /**
     * 卸载dubbo服务
     *
     * @param application
     */
    public void unloadService(String application) {
        invokeInstance.destory(application);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
