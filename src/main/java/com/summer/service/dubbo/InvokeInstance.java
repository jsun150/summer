package com.summer.service.dubbo;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.utils.ReferenceConfigCache;
import com.alibaba.dubbo.rpc.service.GenericService;
import com.summer.service.common.ClassUtils;
import com.summer.service.common.RequestContext;
import com.summer.service.filter.FilterWrapper;
import com.summer.service.filter.InvokeFilter;
import com.summer.service.rpc.Filter;
import com.summer.service.rpc.Invoke;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 创建dubbo 调用实例
 *
 * @author Jook
 * @create 2018-10-20 14:04
 **/
public abstract class InvokeInstance implements Invoke, Filter {

    private static final Logger logger = LoggerFactory.getLogger(InvokeInstance.class);
    // requstPath , InvokeBean
    private Map<String, InvokeBean> HTTP_PATH_INVOKE_MAP = new ConcurrentHashMap<>();
    @Autowired
    private FilterWrapper filterWrapper;

    /**
     * 初始化 可使用的 bean
     *
     * @param url
     * @param config
     * @param inter
     */
    public void createInstance(URL url, ReferenceConfig config, String inter) {
        InvokeBean bean = new InvokeBean(url, config, inter);
        List<String> paths = ClassUtils.parserInterfaceMethods(bean).getPathList();
        if (CollectionUtils.isEmpty(paths)) {
            return;
        }

        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
        bean.setInvokeBean((GenericService) cache.get(config));
        bean.setConfig(config);
        logger.info("summer 实例创建成功 : {}", paths);
        for (String str : paths) {
            HTTP_PATH_INVOKE_MAP.put(bean.getApplication() + "/" + str, bean);
        }
    }

    /**
     * 远程调用
     *
     * @param
     * @return
     */
    public abstract void invoke(HttpServletRequest request, HttpServletResponse response);


    /**
     * 销毁
     *
     * @param application
     */
    public void destory(String application) {
        for (Map.Entry<String, InvokeBean> entry : HTTP_PATH_INVOKE_MAP.entrySet()) {
            if (entry.getKey().startsWith(application)) {
                InvokeBean bean = HTTP_PATH_INVOKE_MAP.get(entry.getKey());
                ReferenceConfigCache.getCache().destroy(bean.getConfig());
                bean.setInvokeBean(null);
                bean = null;
                HTTP_PATH_INVOKE_MAP.remove(entry.getKey());
            }
        }
    }

    @Override
    public void filter(RequestContext context, MethodInfo methodInfo, HttpServletResponse response) {
        if (CollectionUtils.isEmpty(filterWrapper.getInvokeFilters())) return;
        for (InvokeFilter filter : filterWrapper.getInvokeFilters()) {
            filter.filter(context, methodInfo, response);
        }
    }

    public Map<String, InvokeBean> getHTTP_PATH_INVOKE_MAP() {
        return HTTP_PATH_INVOKE_MAP;
    }


    public static class InvokeBean {

        public InvokeBean(URL url, ReferenceConfig config, String inter) {
            this.url = url;
            this.config = config;
            this.inter = inter;
            this.application = url.getParameter(com.alibaba.dubbo.common.Constants.APPLICATION_KEY);
        }

        //请求路径对应 method
        private Map<String, MethodInfo> pathMethodMap = new HashMap<>();
        private ReferenceConfig config;
        private String inter;
        private URL url;
        private GenericService invokeBean;
        private String application;
        private List<String> pathList;

        public List<String> getPathList() {
            return pathList;
        }

        public void setPathList(List<String> pathList) {
            this.pathList = pathList;
        }

        public Map<String, MethodInfo> getPathMethodMap() {
            return pathMethodMap;
        }

        public void setPathMethodMap(Map<String, MethodInfo> pathMethodMap) {
            this.pathMethodMap = pathMethodMap;
        }

        public ReferenceConfig getConfig() {
            return config;
        }

        public void setConfig(ReferenceConfig config) {
            this.config = config;
        }

        public String getInter() {
            return inter;
        }

        public void setInter(String inter) {
            this.inter = inter;
        }

        public URL getUrl() {
            return url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }

        public GenericService getInvokeBean() {
            return invokeBean;
        }

        public void setInvokeBean(GenericService invokeBean) {
            this.invokeBean = invokeBean;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }
    }
}
