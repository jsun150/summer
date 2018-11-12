package com.summer.service.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.registry.NotifyListener;
import com.alibaba.dubbo.registry.RegistryService;
import com.summer.service.classLoader.JarLoader;
import com.summer.service.common.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发现注册 和 失效的 dubbo服务
 */

@Component
public class ServiceDiscover implements InitializingBean, NotifyListener {

    private static final Logger logger = LoggerFactory.getLogger(ServiceDiscover.class);

    @Autowired
    private RegistryService registryService;
    @Autowired
    private ServiceLoader serviceLoader;
    //防止多次加载
    private Map<String, List<String>> JAR_INTERFACE_LOAD_MAP = new HashMap<>();


    @Override
    public void afterPropertiesSet() {
        logger.info("Init Api register Sync ...");
        registryService.subscribe(Constants.SUBSCRIBE_PROVIDER, this);
    }

    @Override
    public void notify(List<URL> urls) {
        if (urls == null || urls.isEmpty()) {
            return;
        }
        for (URL url : urls) {
            try {
                String protocol = url.getProtocol();
                String application = url.getParameter(com.alibaba.dubbo.common.Constants.APPLICATION_KEY);

                //第一次载入
                if (StringUtils.isNotEmpty(application) && CollectionUtils.isEmpty(JAR_INTERFACE_LOAD_MAP.get(application))) {
                    JarLoader.RemoveloadJar(application);
                    if (StringUtils.isBlank(JarLoader.LoadJar(application))) {
                        return;
                    }
                    JAR_INTERFACE_LOAD_MAP.put(application, new ArrayList<>());
                }
                List<String> interfaceList = JAR_INTERFACE_LOAD_MAP.get(application);
                String interfaceKey = url.getParameter(com.alibaba.dubbo.common.Constants.INTERFACE_KEY);
                if (Constants.ACCEPT_PROTOCOL.contains(protocol) && !interfaceList.contains(interfaceKey)) { //服务注册
                    logger.info("summer accept dubbo server:{}", url);
                    serviceLoader.loaderService(url);
                    interfaceList.add(interfaceKey);
                } else if (protocol.equals(com.alibaba.dubbo.common.Constants.EMPTY_PROTOCOL)) { // dubbo服务下线
                    logger.info("remove url:{}", url);
                }
            } catch (Exception e) {
                registryService.unregister(url);
                logger.error("服务载入失败....", e);
            }
        }
    }

    public void unloader(String application) {
        JarLoader.RemoveloadJar(application);
        JAR_INTERFACE_LOAD_MAP.remove(application);
        serviceLoader.unloadService(application);
    }

}
