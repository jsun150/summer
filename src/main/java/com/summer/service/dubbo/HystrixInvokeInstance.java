package com.summer.service.dubbo;

import com.summer.service.Hystrix.ServerCommand;
import com.summer.service.Hystrix.ServerHystrixFactory;
import com.summer.service.common.MessageCodeEnum;
import com.summer.service.http.ContentTypeEnum;
import com.summer.service.http.HttpUtils;
import com.summer.service.http.RequestBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jook
 * @create 2018-11-07 10:22
 **/
public class HystrixInvokeInstance extends InvokeInstance {

    @Override
    public void invoke(HttpServletRequest request, HttpServletResponse response) {
        String result = "system busy";
        try {
            System.out.println(Thread.currentThread().getId());
            long s = System.currentTimeMillis()/1000;
            RequestBean requestBean = HttpUtils.parser(request);
            InvokeBean invokeBean = getHTTP_PATH_INVOKE_MAP().get(requestBean.getPath());
            if (invokeBean != null) {
                ServerCommand command = ServerHystrixFactory.getCommand(requestBean, invokeBean, this, response);
                result = command.queue().get();
            }else {
                result = MessageCodeEnum.PATH_NOT_FOUND.getMsg();
            }
            System.out.println(Thread.currentThread().getId() +"  out: ."+((System.currentTimeMillis()/1000) -s)+". result :" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpUtils.writeResponse(response, result, ContentTypeEnum.JSON, null);
    }
}
