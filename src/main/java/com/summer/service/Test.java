package com.summer.service;


import okhttp3.*;

import java.util.concurrent.TimeUnit;

/**
 * @author Jook
 * @create 2018-11-02 14:31
 **/
public class Test {


    public static void main(String[] args) throws Exception {


//
//        ApplicationConfig applicationConfig = new ApplicationConfig();
//        applicationConfig.setName("user-center1");
//        //这里配置了dubbo的application信息*(demo只配置了name)*，因此demo没有额外的dubbo.xml配置文件
//        RegistryConfig registryConfig = new RegistryConfig();
//        registryConfig.setAddress("zookeeper://127.0.0.1:2181");
//        //这里配置dubbo的注册中心信息，因此demo没有额外的dubbo.xml配置文件
//
//
//        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
//        reference.setApplication(applicationConfig);
//        reference.setRegistry(registryConfig);
//        reference.setInterface("com.jollychic.uc.spi.service.member.MemberService"); // 接口名
//        reference.setGeneric(true); // 声明为泛化接口
//
//        //ReferenceConfig实例很重，封装了与注册中心的连接以及与提供者的连接，
//        //需要缓存，否则重复生成ReferenceConfig可能造成性能问题并且会有内存和连接泄漏。
//        //API方式编程时，容易忽略此问题。
//        //这里使用dubbo内置的简单缓存工具类进行缓存
//
//        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
//        GenericService genericService = cache.get(reference);
//        Object result = genericService.$invoke("getUserMemberInfo", new String[] { "java.lang.Integer" }, new Object[] { 10086 });
//        cache.destroyAll();
//        System.out.println();


//        // 普通编码配置方式
//        ApplicationConfig application = new ApplicationConfig();
//        application.setName("user-center");
//
//        // 连接注册中心配置
//        RegistryConfig registry = new RegistryConfig();
//        registry.setAddress("172.31.0.139:2181");
//
//        ReferenceConfig<GenericService> reference = new ReferenceConfig<GenericService>();
//        reference.setApplication(application);
//        reference.setRegistry(registry);
//        reference.setInterface("com.jollychic.uc.spi.service.member");
//        reference.setGeneric(true); // 声明为泛化接口
//
//        ReferenceConfigCache cache = ReferenceConfigCache.getCache();
//        GenericService genericService = cache.get(reference);
//
//        // 基本类型以及Date,List,Map等不需要转换，直接调用
//        Object result = genericService.$invoke("cancelAutoMemberPayNew", new String[] { "java.lang.Integer" },
//                new Object[] { 1 });
//        System.out.println(result);

        okHtppPostTest();
    }

    public static void okHtppGetTest() throws Exception {
        String url = "https://www.baidu.com";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String as = response.body().string();
        System.out.println(as);

    }

    public static void okHtppPostTest() throws Exception{


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);

        OkHttpClient mOkHttpClient = builder.build();


        MediaType JSON = MediaType.get("application/json; charset=utf-8");

        String url= "http://localhost:8080/user/phoneExists.do";
        String json = "{\"phone\":\"79293354\",\"terminalType\":\"1\",\"countryCode\":\"JO\",\"cookieId\":\"a5779f6c-1e0b-401a-be51-6e84dbeb4581\",\"appTimestamp\":\"1547634238626\",\"lang\":\"1\",\"currency\":\"JOD\",\"sign\":\"492110afc1943c3b1c6c65fe2ab88bda\",\"appVersion\":\"7.6.2\",\"appChannel\":\"GooglePlay\",\"appTypeId\":\"0\",\"appKey\":\"android_lk98f83\",\"countryNumber\":\"962\"}";

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = mOkHttpClient.newCall(request).execute()) {
             System.out.println(response.body().string());
        }


    }
}
