package com.summer.service.dubbo;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedHashMap;

/**
 * 接口描述 --  方法名字, 提供服务类型, 接口要求, 接口params描述
 *
 * @author Jook
 * @create 2018-10-22 16:39
 **/
public class MethodInfo {

    // 接口支持的请求类型
    private RequestMethod HttpType;
    // 参数类型 有序 -- dubbo 需要目前, key参数名字  value 参数类型
    private LinkedHashMap<String, String> parameterClass;
    private Boolean login;
    private String methodName;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Boolean getLogin() {
        return login;
    }

    public void setLogin(Boolean login) {
        this.login = login;
    }

    public RequestMethod getHttpType() {
        return HttpType;
    }

    public void setHttpType(RequestMethod httpType) {
        HttpType = httpType;
    }

    public LinkedHashMap<String, String> getParameterClass() {
        return parameterClass;
    }

    public void setParameterClass(LinkedHashMap<String, String> parameterClass) {
        this.parameterClass = parameterClass;
    }
}
