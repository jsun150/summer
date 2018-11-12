package com.summer.service.common;

import java.util.LinkedHashMap;

/**
 * @author Jook
 * @create 2018-11-05 13:57
 **/
public class ParameterBean {

    //参数名字和对应的类型name
    private LinkedHashMap<String, String> paramMap;
//    private Map<String, xx>   参数限制


    public LinkedHashMap<String, String> getParamMap() {
        return paramMap;
    }

    public void setParamMap(LinkedHashMap<String, String> paramMap) {
        this.paramMap = paramMap;
    }
}
