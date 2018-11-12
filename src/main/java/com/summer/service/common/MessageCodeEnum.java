package com.summer.service.common;


/**
 * 接口返回码定义
 * 主要为失败的返回码，成功的返回码如果前端需要也定义在该枚举类
 *
 * @author Sean
 * @description msgCode是唯一的，定义规则如下
 */
public enum MessageCodeEnum {


    PATH_NOT_FOUND(404, "404 NOT FOUND", "请求地址不存在"),

    ;

    private Integer msgCode;
    private String msg;
    private String cnMsg;

    MessageCodeEnum(Integer msgCode, String msg, String cnMsg) {
        this.msgCode = msgCode;
        this.msg = msg;
        this.cnMsg = cnMsg;
    }

    public Integer getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(Integer msgCode) {
        this.msgCode = msgCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCnMsg() {
        return cnMsg;
    }

    public void setCnMsg(String cnMsg) {
        this.cnMsg = cnMsg;
    }
}
