package com.summer.service.Hystrix;

import com.summer.service.common.RequestContext;
import com.summer.service.dubbo.InvokeInstance;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jook
 * @create 2018-11-06 20:57
 **/
public class ServerHystrixFactory {


    public static ServerCommand getCommand(RequestContext context, InvokeInstance.InvokeBean invokeBean, InvokeInstance invokeInstance, HttpServletResponse response) {
        // 根据application 获取对应command的配置
        return new ServerCommand(getConfig(invokeBean.getApplication()), context, invokeBean, invokeInstance, response);
    }

    private static HystirxConfig getConfig(String application) {
        HystirxConfig config = new HystirxConfig(application);
        config.setCoreSize(2);
        config.setQueueSizeRejectionThreshold(3);
        return config;
//        return new HystirxConfig(application);
    }


}
