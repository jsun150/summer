package com.summer.service.filter;

import com.summer.service.dubbo.MethodInfo;
import com.summer.service.http.RequestBean;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jook
 * @create 2018-10-23 10:39
 **/
public interface InvokeFilter {

    /**
     * 过滤
     *
     * @param bean
     * @param response
     * @return
     */
    void filter(RequestBean bean, MethodInfo methodInfo, HttpServletResponse response);
}
