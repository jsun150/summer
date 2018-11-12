package com.summer.service.common;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jook
 * @create 2018-11-02 17:35
 **/
public class InvokeFunction {

    private String inter;   //接口名称
    private Anntation anntation;    //方法注解描述
    private ParamInfo paramInfo;    //参数
    private String requestMap;      //接口请求http 路径


    public static class ParamInfo {
        private LinkedHashMap<String, Class> paramMap;  //参数名称 类型
        private boolean isBean;     //true bean类型
        private Map<String, Anntation> paramAnntationMap;   //参数与对应的 注解描述

        public Map<String, Anntation> getParamAnntationMap() {
            return paramAnntationMap;
        }

        public void setParamAnntationMap(Map<String, Anntation> paramAnntationMap) {
            this.paramAnntationMap = paramAnntationMap;
        }

        public LinkedHashMap<String, Class> getParamMap() {
            return paramMap;
        }

        public void setParamMap(LinkedHashMap<String, Class> paramMap) {
            this.paramMap = paramMap;
        }

        public boolean isBean() {
            return isBean;
        }

        public void setBean(boolean bean) {
            isBean = bean;
        }
    }

    public String getInter() {
        return inter;
    }

    public void setInter(String inter) {
        this.inter = inter;
    }

    public Anntation getAnntation() {
        return anntation;
    }

    public void setAnntation(Anntation anntation) {
        this.anntation = anntation;
    }

    public ParamInfo getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(ParamInfo paramInfo) {
        this.paramInfo = paramInfo;
    }

    public String getRequestMap() {
        return requestMap;
    }

    public void setRequestMap(String requestMap) {
        this.requestMap = requestMap;
    }
}
