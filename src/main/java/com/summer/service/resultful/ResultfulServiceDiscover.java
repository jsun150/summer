package com.summer.service.resultful;

import com.alibaba.fastjson.JSON;
import com.summer.service.zookeeper.ZookeeperDiscover;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.URLDecoder;

/**
 * @author Jook
 * @create 2018-11-07 16:37
 **/
@Component
public class ResultfulServiceDiscover extends ZookeeperDiscover implements ApplicationListener<ApplicationReadyEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ResultfulServiceDiscover.class);

    private final String path = "/httpServer";

    @Override
    public void notify(String path) {
        if(StringUtils.isBlank(path)) return;
        try {
            String[] sps = path.split("/");
            if (sps.length  != 5) return;
            String application = sps[2];
            String clname = sps[3];
            ResultfulProtocol protocol = JSON.parseObject(URLDecoder.decode(sps[4],"utf-8"), ResultfulProtocol.class);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try {
            start(path);
        } catch (Exception e) {
            logger.info(path+" 服务发现失败 ....", e);
        }
    }


}
