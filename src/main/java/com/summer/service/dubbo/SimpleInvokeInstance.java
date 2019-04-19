package com.summer.service.dubbo;

import com.alibaba.fastjson.JSONObject;
import com.jollychic.common.resultutil.BusinessException;
import com.summer.service.common.BusinessExceptionUtil;
import com.summer.service.common.ClassUtils;
import com.summer.service.common.MessageCodeEnum;
import com.summer.service.common.RequestContext;
import com.summer.service.http.ContentTypeEnum;
import com.summer.service.http.HttpUtils;
import com.summer.service.http.RequestBean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Jook
 * @create 2018-11-07 10:11
 **/
public class SimpleInvokeInstance extends InvokeInstance{

    @Override
    public void invoke(HttpServletRequest request, HttpServletResponse response) {
        String result = "system busy";
        try{
            RequestContext context = HttpUtils.parserRequest(request);
            InvokeBean invokeBean = getHTTP_PATH_INVOKE_MAP().get(context.getPath());
            if (invokeBean == null) {
                throw BusinessExceptionUtil.build(MessageCodeEnum.PATH_NOT_FOUND);
            }

            MethodInfo methodInfo = invokeBean.getPathMethodMap().get(context.getPath());
            if (methodInfo == null) {
                throw BusinessExceptionUtil.build(MessageCodeEnum.PATH_NOT_FOUND);
            }
            //过滤
            filter(context, methodInfo, response);
            Map<String, Object[]> params = ClassUtils.packageArgs(context, invokeBean.getPathMethodMap().get(context.getPath()));
            if (params == null) {
                invokeBean.getInvokeBean().$invoke(methodInfo.getMethodName(), null, null);
            }
            //接口调用
            result = JSONObject.toJSONString(invokeBean.getInvokeBean().$invoke(methodInfo.getMethodName(), (String[]) params.get("parameterTypes"), params.get("args")));
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
