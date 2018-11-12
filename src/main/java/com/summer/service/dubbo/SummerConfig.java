package com.summer.service.dubbo;

import com.alibaba.dubbo.config.ReferenceConfig;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存放调用的关联关系
 * @author Jook
 * @create 2018-10-23 18:00
 **/
public class SummerConfig {

    /**
     * api请求路径 对应的 调用invkeBean
     */
    private static Map<String, InvokeInstance.InvokeBean> API_PATH_INVOKE_MAP = new ConcurrentHashMap<>();

    /**
     * 当前加载的接口
     */
    private static Set<String> INTERFACELOAD = Collections.synchronizedSet(new HashSet<>());

    /**
     * 接口 对应的 api 路径
     */
    private static Map<String, List<String>> INTERFACE_API_PATH = new ConcurrentHashMap<>();

    /**
     * 接口 对应config  卸载dubbo链接使用
     */
    private static Map<String, ReferenceConfig> INTERFACE_REFERENCONFIG = new ConcurrentHashMap<>();


    public static boolean putApiMapInvokeBean (String api, InvokeInstance.InvokeBean bean) {
       return  null == API_PATH_INVOKE_MAP.putIfAbsent(api, bean) ? false : true;
    }

    public static boolean addInterfaceLoadList (String inter) {
        return INTERFACELOAD.add(inter);
    }

    public static void putInterfaceMapApiPath (String inter, List<String> paths) {
        INTERFACE_API_PATH.putIfAbsent(inter, paths);
    }

    public static boolean addInterMapReferenconfig (String inter, ReferenceConfig config) {
        return INTERFACE_REFERENCONFIG.putIfAbsent(inter, config) == null ? false : true;
    }

    /**
     * 卸载服务
     */
    public static void removeService (String inter) {

    }

    public static void main(String[] args)throws Exception{
        Set<String> set = new HashSet<>();
        System.out.println(set.add("1"));
        System.out.println(set.add("2"));
        System.out.println(set.add("1"));

    }


}
