package com.summer.service.dubbo;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.summer.service.spring.SpringApplicationContext;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jook
 * @create 2018-07-02 14:37
 **/
public class ReferenceConfigFactory {


    public static ReferenceConfig getConfig (URL url, String interfaceName, ApplicationContext applicationContext) {

        ApplicationConfig application = new ApplicationConfig();
        application.setName(com.summer.service.common.Constants.APPLICATION_NAME);
        application.setId(url.getParameter(Constants.APPLICATION_KEY) + url.getParameter(Constants.INTERFACE_KEY));
        ReferenceConfig reference = new ReferenceConfig();
        reference.setInterface(interfaceName);
        reference.setGeneric(true);
        reference.setRetries(1);
        for (Map.Entry<String, String> entry : url.getParameters().entrySet()) {
            switch (entry.getKey()) {
                case Constants.PROTOCOL_KEY:
                    reference.setProtocol(entry.getValue());
                    break;
                case Constants.TIMEOUT_KEY:
                    reference.setTimeout(Integer.parseInt(entry.getValue()));
                    break;
                case Constants.CHECK_KEY:
                    reference.setCheck(Boolean.valueOf(entry.getValue()));
                    break;
                case Constants.VERSION_KEY:
                    reference.setVersion(entry.getValue());
                    break;
                case Constants.GROUP_KEY:
                    reference.setGroup(entry.getValue());
                    break;
            }
        }
        reference.setApplication(application);
        RegistryConfig config = applicationContext.getBean("dubboRegister",RegistryConfig.class);
        reference.setRegistry(config);
        return reference;
    }

}
