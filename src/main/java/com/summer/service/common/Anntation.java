package com.summer.service.common;

/**
 * 方法 参数的注解描述
 * @author Jook
 * @create 2018-11-02 17:40
 **/
public class Anntation {

    private boolean login;
    private boolean isNotNull;
    private Integer type;   // 1 method 2 param

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public boolean isNotNull() {
        return isNotNull;
    }

    public void setNotNull(boolean notNull) {
        isNotNull = notNull;
    }
}
