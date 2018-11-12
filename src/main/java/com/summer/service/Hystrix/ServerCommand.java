package com.summer.service.Hystrix;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSONObject;
import com.jollychic.common.resultutil.BusinessException;
import com.netflix.hystrix.*;
import com.summer.service.common.BusinessExceptionUtil;
import com.summer.service.common.ClassUtils;
import com.summer.service.common.MessageCodeEnum;
import com.summer.service.dubbo.InvokeInstance;
import com.summer.service.dubbo.MethodInfo;
import com.summer.service.http.RequestBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Jook
 * @create 2018-11-06 20:47
 **/
public class ServerCommand extends HystrixCommand<String> {
    private static final Logger logger = LoggerFactory.getLogger(ServerCommand.class);

    /**
     * @param groupKey                       相同的group 同一个线程池
     * @param commandKey                     调用的接口名字  熔断基准   -- Hundreds of keys is fine, tens of thousands is probably not.
     * @param poolName                       没有则为 groupkey, 可以作为线程池控制的 更细方式
     * @param executionTimeoutInMilliseconds 任务过期时间
     * @param queueSize                      队列大小
     * @param coreSize                       核心线程数
     */
    public ServerCommand(String groupKey, String commandKey, String poolName, Integer executionTimeoutInMilliseconds, Integer queueSize, Integer coreSize) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(poolName))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionTimeoutInMilliseconds(executionTimeoutInMilliseconds)
                )
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter()
                                .withMaxQueueSize(queueSize)   //配置队列大小
                                .withCoreSize(coreSize)    // 配置线程池里的线程数
                                .withQueueSizeRejectionThreshold(5000)

                )
        );
    }

    public ServerCommand(HystirxConfig config, RequestBean requestBean, InvokeInstance.InvokeBean invokeBean, InvokeInstance invokeInstance, HttpServletResponse response) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(config.getGroupKey()))
                .andCommandKey(HystrixCommandKey.Factory.asKey(config.getCommandKey()))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(config.getPoolName()))
                .andCommandPropertiesDefaults(
                        HystrixCommandProperties.Setter()
                                .withExecutionTimeoutInMilliseconds(config.getExecutionTimeoutInMilliseconds())
                )
                .andThreadPoolPropertiesDefaults(
                        HystrixThreadPoolProperties.Setter()
                                .withCoreSize(config.getCoreSize())    // 配置线程池里的线程数
                                .withQueueSizeRejectionThreshold(config.getQueueSizeRejectionThreshold())

                )
        );
        this.invokeBean = invokeBean;
        this.requestBean = requestBean;
        this.invokeInstance = invokeInstance;
        this.response = response;
    }

    private InvokeInstance.InvokeBean invokeBean;
    private RequestBean requestBean;
    private InvokeInstance invokeInstance;
    private HttpServletResponse response;

    @Override
    protected String run() throws Exception {
        String result = "system busy";
        try {
            MethodInfo methodInfo = invokeBean.getPathMethodMap().get(requestBean.getPath());
            if (methodInfo == null) {
                throw BusinessExceptionUtil.build(MessageCodeEnum.PATH_NOT_FOUND);
            }
            //过滤
            invokeInstance.filter(requestBean, methodInfo, response);
            Object[][] params = ClassUtils.packageArgs(requestBean, invokeBean.getPathMethodMap().get(requestBean.getPath()));
            if (params == null) {
                invokeBean.getInvokeBean().$invoke(methodInfo.getMethodName(), null, null);
            }
            String[] nams = new String[params[0].length];
            for (int i = 0; i < params[0].length; i++) nams[i] = params[0][i].toString();
            //组装参数
            result = JSONObject.toJSONString(invokeBean.getInvokeBean().$invoke(methodInfo.getMethodName(), nams, params[1]));
        } catch (BusinessException e) {
            logger.info("业务错误...", e);
            result = e.getMessage();
        } catch (RpcException e) {
            logger.info("dubbo错误...", e);
        } catch (Exception e) {
            logger.info("",e);
        } catch (Throwable t) {
            logger.info("",t);
        }
        return result;
    }


    @Override
    protected String getFallback() {
        return "getFallback";
    }


}
