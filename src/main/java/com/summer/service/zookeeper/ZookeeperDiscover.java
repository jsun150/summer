package com.summer.service.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * todo  session过期终端 无法重连zookeeper
 *
 * @author Jook
 * @create 2018-11-07 19:49
 **/
public abstract class ZookeeperDiscover {

    private CuratorFramework client;
    private TreeCache cache;

    public void start(String path) throws Exception {
        client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        cache = new TreeCache(client, path);
        client.start();
        TreeCacheListener listener = (client1, event) -> {
            // 监控临时节点的新增
            if (TreeCacheEvent.Type.NODE_ADDED == event.getType() && event.getData() != null) {
                notify(event.getData().getPath());
            }
        };
        cache.getListenable().addListener(listener);
        cache.start();
    }


    public abstract void notify(String path);

    public static void main(String[] args) throws Exception {

        String PATH = "/httpServer";
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", new ExponentialBackoffRetry(1000, 3));
        client.start();
        TreeCache cache = new TreeCache(client, PATH);
        TreeCacheListener listener = (client1, event) -> {
            // 监控临时节点的新增
            try {
                if (TreeCacheEvent.Type.NODE_ADDED == event.getType() || TreeCacheEvent.Type.INITIALIZED == event.getType()) {
                    if (event.getData() != null)
                    System.out.println("节点数据：" + event.getData().getPath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        cache.getListenable().addListener(listener);
        cache.start();

        Thread.sleep(10000);
//        cache.close();
//        client.close();
        System.out.println("OK!");


    }

}
