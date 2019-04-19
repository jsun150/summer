package com.summer.service.resultful;

import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 服务注册 协议
 * @author Jook
 * @create 2018-11-08 10:14
 **/
public class ResultfulProtocol {
    private String protocl = "httpServer";
    private String className;
    private List<Method> methodList;
    private String ip;
    private Integer port;
    private String path;
    private String application;
    private boolean login;

    public static class Method {
        private String MethodName;
        private RequestMethod requestMethod;
        private String path;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getMethodName() {
            return MethodName;
        }

        public void setMethodName(String methodName) {
            MethodName = methodName;
        }

        public RequestMethod getRequestMethod() {
            return requestMethod;
        }

        public void setRequestMethod(RequestMethod requestMethod) {
            this.requestMethod = requestMethod;
        }
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getProtocl() {
        return protocl;
    }

    public void setProtocl(String protocl) {
        this.protocl = protocl;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<Method> getMethodList() {
        return methodList;
    }

    public void setMethodList(List<Method> methodList) {
        this.methodList = methodList;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }
}
