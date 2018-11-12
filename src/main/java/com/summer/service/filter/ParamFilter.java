package com.summer.service.filter;

import com.summer.service.dubbo.MethodInfo;
import com.summer.service.http.RequestBean;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jook
 * @create 2018-10-23 11:58
 **/
@Component
public class ParamFilter implements InvokeFilter{

    @Override
    public void filter(RequestBean bean, MethodInfo methodInfo, HttpServletResponse response) {

    }
}
