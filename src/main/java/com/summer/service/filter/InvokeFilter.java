package com.summer.service.filter;

import com.summer.service.common.RequestContext;
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
     * @param context
     * @param response
     * @return
     */
    void filter(RequestContext context, MethodInfo methodInfo, HttpServletResponse response);
}
