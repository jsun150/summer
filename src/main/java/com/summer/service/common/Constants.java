package com.summer.service.common;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String GROUP_SPILT = "@";


    public static final URL SUBSCRIBE_PROVIDER = new URL(com.alibaba.dubbo.common.Constants.PROVIDER_PROTOCOL, NetUtils.getLocalHost(), 0, "",
            com.alibaba.dubbo.common.Constants.INTERFACE_KEY, com.alibaba.dubbo.common.Constants.ANY_VALUE,
            com.alibaba.dubbo.common.Constants.GROUP_KEY, com.alibaba.dubbo.common.Constants.ANY_VALUE,
            com.alibaba.dubbo.common.Constants.VERSION_KEY, com.alibaba.dubbo.common.Constants.ANY_VALUE,
            com.alibaba.dubbo.common.Constants.CLASSIFIER_KEY, com.alibaba.dubbo.common.Constants.ANY_VALUE,
            com.alibaba.dubbo.common.Constants.CATEGORY_KEY, com.alibaba.dubbo.common.Constants.PROVIDERS_CATEGORY,
            com.alibaba.dubbo.common.Constants.ENABLED_KEY, com.alibaba.dubbo.common.Constants.ANY_VALUE,
            com.alibaba.dubbo.common.Constants.CHECK_KEY, String.valueOf(false));


    public static final URL UNSUBSCRIBE_ADMIN_ALL = new URL(com.alibaba.dubbo.common.Constants.ADMIN_PROTOCOL, NetUtils.getLocalHost(), 0, "",
            com.alibaba.dubbo.common.Constants.INTERFACE_KEY, com.alibaba.dubbo.common.Constants.ANY_VALUE,
            com.alibaba.dubbo.common.Constants.GROUP_KEY, com.alibaba.dubbo.common.Constants.ANY_VALUE,
            com.alibaba.dubbo.common.Constants.VERSION_KEY, com.alibaba.dubbo.common.Constants.ANY_VALUE,
            com.alibaba.dubbo.common.Constants.CLASSIFIER_KEY, com.alibaba.dubbo.common.Constants.ANY_VALUE,
            com.alibaba.dubbo.common.Constants.CATEGORY_KEY, com.alibaba.dubbo.common.Constants.PROVIDERS_CATEGORY + ","
            + com.alibaba.dubbo.common.Constants.CONSUMERS_CATEGORY + ","
            + com.alibaba.dubbo.common.Constants.ROUTERS_CATEGORY + ","
            + com.alibaba.dubbo.common.Constants.CONFIGURATORS_CATEGORY,
            com.alibaba.dubbo.common.Constants.ENABLED_KEY, com.alibaba.dubbo.common.Constants.ANY_VALUE,
            com.alibaba.dubbo.common.Constants.CHECK_KEY, String.valueOf(false));

    public interface Protocol {
        String REST = "rest";
        String DUBBO = "dubbo";
        String HESSIAN = "hessian";
    }


    public static final List<String> ACCEPT_PROTOCOL = new ArrayList<String>();

    static {
        ACCEPT_PROTOCOL.add(Protocol.REST);
        ACCEPT_PROTOCOL.add(Protocol.DUBBO);
        ACCEPT_PROTOCOL.add(Protocol.HESSIAN);
    }


    public static final String HTTP_POST = "POST";
    public static final String HTTP_GET = "GET";

    public static final String APPLICATION_NAME = "summber";

    public static final Integer PARAMTER_TYPE_BEAN = 2;
    public static final Integer PARAMTER_TYPE_NOT_BEAN = 1;
    public static final Integer PARAMTER_TYPE_NULL = 3;
}
