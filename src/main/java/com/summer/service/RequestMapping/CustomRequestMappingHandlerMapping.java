package com.summer.service.RequestMapping;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Jook
 * @create 2018-07-31 10:18
 **/
@Component
public class CustomRequestMappingHandlerMapping extends RequestMappingHandlerMapping {
    private final static Map<String, HandlerMethod> HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP = Maps.newHashMap();

    public CustomRequestMappingHandlerMapping() {
        setOrder(0);
    }

    // 用于保存处理方法和RequestMappingInfo的映射关系
    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        HandlerMethod handlerMethod = super.createHandlerMethod(handler, method);
        RequestMapping rm = handlerMethod.getMethodAnnotation(RequestMapping.class);
        if (null != rm && rm.method() != null && rm.method().length > 0 && !StringUtils.isBlank(rm.method()[0].name())) {
            HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP.put(rm.method()[0].name(), handlerMethod);
        }

        super.registerHandlerMethod(handler, method, mapping);
    }

    @Override
    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) {
        String method = request.getMethod();
        if (lookupPath.equalsIgnoreCase("/error")) {
            // 返回错误页面
            return null;
        }
        if (!method.equalsIgnoreCase("POST") && !method.equalsIgnoreCase("GET") && StringUtils.isBlank(lookupPath) && !lookupPath.startsWith("api")) {
            return null;
        }
        return HANDLER_METHOD_REQUEST_MAPPING_INFO_MAP.get(method);
    }
}
