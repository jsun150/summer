//package com.summer.service.web;
//
//import com.alibaba.fastjson.JSON;
//import com.summer.service.zookeeper.ZookeeperDiscover;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.curator.framework.CuratorFramework;
//import org.apache.curator.framework.CuratorFrameworkFactory;
//import org.apache.curator.framework.recipes.cache.TreeCache;
//import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
//import org.apache.curator.framework.recipes.cache.TreeCacheListener;
//import org.apache.curator.retry.ExponentialBackoffRetry;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//import java.net.URLDecoder;
//
///**
// * @author Jook
// * @create 2018-11-07 16:37
// **/
//@Component
//public class WebServiceDiscover extends ZookeeperDiscover implements ApplicationListener<ApplicationReadyEvent> {
//    private static final Logger logger = LoggerFactory.getLogger(WebServiceDiscover.class);
//
//    private final String path = "/httpServer";
//
//    @Override
//    public void notify(String path) {
//        if(StringUtils.isBlank(path)) return;
//        try {
//            String[] sps = path.split("/");
//            if (sps.length  != 5) return;
//            String application = sps[2];
//            String clname = sps[3];
//            WebProtocol protocol = JSON.parseObject(URLDecoder.decode(sps[4],"utf-8"), WebProtocol.class);
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onApplicationEvent(ApplicationReadyEvent event) {
//        try {
//            start(path);
//        } catch (Exception e) {
//            logger.info(path+" 服务发现失败 ....", e);
//        }
//    }
//
//
//}
