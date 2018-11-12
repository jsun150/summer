package com.summer.service.controller;

import com.summer.service.dubbo.ServiceDiscover;
import com.summer.service.server.HttpServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jook
 * @create 2018-10-17 13:46
 **/
@Controller
@RequestMapping(value = "/api")
public class ApiController {

    @Autowired
    private HttpServer httpServer;
    @Autowired
    private ServiceDiscover serviceDiscover;

    @RequestMapping(method = RequestMethod.GET)
    public void getApi(HttpServletRequest request, HttpServletResponse response) {
        httpServer.getData(request, response);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void postApi(HttpServletRequest request, HttpServletResponse response) {
        httpServer.getData(request, response);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void deleteApi(HttpServletRequest request, HttpServletResponse response) {
        serviceDiscover.unloader(request.getParameter("application"));
    }


}
