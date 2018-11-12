package com.summer.service.dubbo;

import com.summer.service.common.ParameterInfo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jook
 * @create 2018-10-22 16:39
 **/
public class MethodInfo {

    private String HttpType;        //post get
    private ParameterInfo parameterInfo;
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

    public String getHttpType() {
        return HttpType;
    }

    public void setHttpType(String httpType) {
        HttpType = httpType;
    }

    public ParameterInfo getParameterInfo() {
        return parameterInfo;
    }

    public void setParameterInfo(ParameterInfo parameterInfo) {
        this.parameterInfo = parameterInfo;
    }
}
