package com.summer.service.server;

import com.summer.service.dubbo.InvokeInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jook
 * @create 2018-10-22 21:09
 **/
@Component
public class HttpServer {

    /**
     * dubbo
     */
    @Autowired
    private InvokeInstance invokeInstance;

    /**
     * http
     * @param request
     * @param response
     */


    public void getData(HttpServletRequest request, HttpServletResponse response) {
        invokeInstance.invoke(request, response);
    }


}
