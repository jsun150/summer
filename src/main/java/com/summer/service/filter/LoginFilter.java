package com.summer.service.filter;

import com.summer.service.common.RequestContext;
import com.summer.service.dubbo.MethodInfo;
import com.summer.service.http.RequestBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jook
 * @create 2018-10-23 10:44
 **/
@Component
public class LoginFilter implements InvokeFilter{


    @Override
    public void filter(RequestContext context, MethodInfo methodInfo, HttpServletResponse response) {


    }
}
