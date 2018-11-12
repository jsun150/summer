package com.summer.service.classLoader;

import com.alibaba.dubbo.common.utils.StringUtils;
import javassist.ClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Jook
 * @create 2018-11-05 9:42
 **/
public class JarLoader {

    private static final String prefix = "E:/jar/";
    private static Map<String, ClassPool> APPLICATION_POOL_MAP = new ConcurrentHashMap<>();
    private static Map<String, ClassPath> APPLICATION_PATH_MAP = new ConcurrentHashMap<>();

    public static ClassPool getPool(String application) {
        return APPLICATION_POOL_MAP.get(application);
    }

    public static String LoadJar(String application) throws NotFoundException {
        //读取文件夹下的第一个jar
        String jarName = getJarName(application);
        if (StringUtils.isBlank(jarName)) return null;
        String jarPath = prefix + application + "/" + jarName;
        ClassPool pool = APPLICATION_POOL_MAP.get(jarPath);
        ClassPath classPath = null;
        if (pool == null){
            pool = new ClassPool();
            classPath = pool.insertClassPath(jarPath);
            pool.appendSystemPath();
        }
        APPLICATION_POOL_MAP.put(application, pool);
        APPLICATION_PATH_MAP.put(application, classPath);
        return jarName;
    }

    public static void RemoveloadJar(String application) {
        ClassPool pool = APPLICATION_POOL_MAP.get(application);
        ClassPath path = null;
        if (pool != null && (path = APPLICATION_PATH_MAP.remove(application)) != null){
            pool.removeClassPath(path);
        }
    }

    public static String getJarName(String application) {
        File file = new File(prefix + application);
        if (file.isDirectory()) {
            return file.list()[0];
        }
        return null;
    }

//    public static void main(String[] args)throws Exception{
//        JarLoader.LoadJar("user-center");
//        CtClass cs = JarLoader.getPool("user-center").get("com.jollychic.uc.spi.service.member.MemberService");
//        System.out.println();
//    }
}
