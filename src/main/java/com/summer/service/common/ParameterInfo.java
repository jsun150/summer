package com.summer.service.common;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Jook
 * @create 2018-11-05 14:25
 **/
public class ParameterInfo {

    private int paramType; // 1: 独立的参数, 2: bean类型参数 3.不用参数
    private LinkedHashMap<String, String> paramsMap;    //针对独立参数
    private Map<String, String> beanParamMap;
    private String beanParamName;

    public String getBeanParamName() {
        return beanParamName;
    }

    public void setBeanParamName(String beanParamName) {
        this.beanParamName = beanParamName;
    }

    public int getParamType() {
        return paramType;
    }

    public void setParamType(int paramType) {
        this.paramType = paramType;
    }

    public LinkedHashMap<String, String> getParamsMap() {
        return paramsMap;
    }

    public void setParamsMap(LinkedHashMap<String, String> paramsMap) {
        this.paramsMap = paramsMap;
    }

    public Map<String, String> getBeanParamMap() {
        return beanParamMap;
    }

    public void setBeanParamMap(Map<String, String> beanParamMap) {
        this.beanParamMap = beanParamMap;
    }
}
