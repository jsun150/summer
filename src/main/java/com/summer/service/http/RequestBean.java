package com.summer.service.http;

import java.util.Map;

/**
 * @author Jook
 * @create 2018-10-22 20:47
 **/
public class RequestBean {

    private String httpType;
    private String postJson;
    private Map<String, String[]> requestParams;
    private String path;

    public String getHttpType() {
        return httpType;
    }

    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    public String getPostJson() {
        return postJson;
    }

    public void setPostJson(String postJson) {
        this.postJson = postJson;
    }

    public Map<String, String[]> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(Map<String, String[]> requestParams) {
        this.requestParams = requestParams;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
