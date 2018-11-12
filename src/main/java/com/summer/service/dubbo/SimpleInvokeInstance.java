package com.summer.service.dubbo;

import com.alibaba.fastjson.JSONObject;
import com.jollychic.common.resultutil.BusinessException;
import com.summer.service.common.BusinessExceptionUtil;
import com.summer.service.common.ClassUtils;
import com.summer.service.common.MessageCodeEnum;
import com.summer.service.http.ContentTypeEnum;
import com.summer.service.http.HttpUtils;
import com.summer.service.http.RequestBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Jook
 * @create 2018-11-07 10:11
 **/
public class SimpleInvokeInstance extends InvokeInstance{

    @Override
    public void invoke(HttpServletRequest request, HttpServletResponse response) {
        String result = "system busy";
        try{
            RequestBean requestBean = HttpUtils.parser(request);
            InvokeBean invokeBean = getHTTP_PATH_INVOKE_MAP().get(requestBean.getPath());
            if (invokeBean == null) {
                throw BusinessExceptionUtil.build(MessageCodeEnum.PATH_NOT_FOUND);
            }

            MethodInfo methodInfo = invokeBean.getPathMethodMap().get(requestBean.getPath());
            if (methodInfo == null) {
                throw BusinessExceptionUtil.build(MessageCodeEnum.PATH_NOT_FOUND);
            }
            //过滤
            filter(requestBean, methodInfo, response);
            Object[][] params = ClassUtils.packageArgs(requestBean, invokeBean.getPathMethodMap().get(requestBean.getPath()));
            if (params == null) {
                invokeBean.getInvokeBean().$invoke(methodInfo.getMethodName(), null, null);
            }
            String[] nams = new String[params[0].length];
            for (int i = 0; i < params[0].length; i++) nams[i] = params[0][i].toString();
            //组装参数
            result = JSONObject.toJSONString(invokeBean.getInvokeBean().$invoke(methodInfo.getMethodName(), nams, params[1]));
        }catch (BusinessException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }catch (Throwable e) {
            e.printStackTrace();
        }
        HttpUtils.writeResponse(response,result, ContentTypeEnum.JSON,null);
    }
}
