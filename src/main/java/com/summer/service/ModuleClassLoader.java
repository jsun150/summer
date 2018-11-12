/*
 *
 *  * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.summer.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 模块的ClassLoader，继承自URLClassLoader，同时可以强制指定一些包下的class，由本ClassLoader自己加载，不通过父ClassLoader加载，突破双亲委派机制。
 *
 * @author tengfei.fangtf
 * @version $Id: ModuleClassLoader.java, v 0.1 Mar 20, 2017 4:04:32 PM tengfei.fangtf Exp $
 */
public class ModuleClassLoader extends URLClassLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleClassLoader.class);

    /**
     * java的包必须排除，避免安全隐患
     */
    public static final String[] DEFAULT_EXCLUDED_PACKAGES = new String[]{"java.", "javax.", "sun.", "oracle."};

    /**
     * 需要排除的包
     */
    private final Set<String> excludedPackages = new HashSet<String>();


    public ModuleClassLoader(List<URL> urls, ClassLoader parent, List<String> overridePackages) {
        super(urls.toArray(new URL[]{}), parent);

        this.excludedPackages.addAll(Sets.newHashSet(DEFAULT_EXCLUDED_PACKAGES));
    }

    private Object c = new Object() {
        @Override
        public void finalize() {
            System.out.println("ModuleClassLoader finalize gc......");
        }
    };

    public static List<URL> getMyURLs() {
        URL url = null;
        try {
            url = new File("E:\\jar\\user-center.jar").toURI().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return Arrays.asList(url);
    }

    public static void main(String[] args) throws Exception {
//        ClassLoader loaderd = Thread.currentThread().getContextClassLoader();
//
//        ModuleClassLoader loader = new ModuleClassLoader(getMyURLs(), Thread.currentThread().getContextClassLoader(), null);


        ClassPool pool = ClassPool.getDefault();
        ClassPath classPath = pool.insertClassPath("E:\\jar\\summer.jar");
        CtClass clazz = pool.get("com.jollychic.uc.spi.service.member.MemberCodeService");
        CtMethod[] ctMethods = clazz.getDeclaredMethods();
        for (CtMethod ctMethod : ctMethods) {
            if (ctMethod.getName().contains("queryByParamNew")) {
                CtClass paramClass = pool.get(ctMethod.getParameterTypes()[0].getName());
                Map<String, String> map = new HashMap<>();
                CtField[] ctFields = paramClass.getDeclaredFields();
                for (CtField ctField : ctFields) {
                    map.put(ctField.getName(), ctField.getType().getName());
                }

                CtClass superClass = pool.get(paramClass.getSuperclass().getName());
                for (CtField ctField : superClass.getDeclaredFields()) {
                    map.put(ctField.getName(), ctField.getType().getName());
                }

                System.out.println(JSON.toJSONString(map));

            }


        }


        System.gc();


    }



    public static void createAnntation(String des) {


    }

    public static class ServerBean {

        public List<String> methodList;
        public String inter;

        public List<String> getMethodList() {
            return methodList;
        }

        public void setMethodList(List<String> methodList) {
            this.methodList = methodList;
        }

        public String getInter() {
            return inter;
        }

        public void setInter(String inter) {
            this.inter = inter;
        }
    }

    /**
     * 覆盖双亲委派机制
     *
     * @see ClassLoader#loadClass(String, boolean)
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        Class<?> result = null;
        synchronized (ModuleClassLoader.class) {
            if (name.startsWith("com.jollychic.uc")) {
                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("Load class for overriding: {}", name);
                }
                result = loadClassForOverriding(name);
            }
            if (result != null) {
                //链接类
                if (resolve) {
                    resolveClass(result);
                }
                return result;
            }
        }
        //使用默认类加载方式
        return super.loadClass(name, resolve);

    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        return loadClass(name, false);
    }


    /**
     * 加载一个子模块覆盖的类
     *
     * @param name
     * @return
     * @throws ClassNotFoundException
     */
    private Class<?> loadClassForOverriding(String name) throws ClassNotFoundException {
        //查找已加载的类
        Class<?> result = findLoadedClass(name);
        if (result == null) {
            //加载类
            result = findClass(name);
        }
        return result;
    }


    /**
     * 判断class是否排除
     *
     * @param className
     * @return
     */
    protected boolean isExcluded(String className) {
        checkNotNull(className, "className is null");
        for (String packageName : this.excludedPackages) {
            if (className.startsWith(packageName)) {
                return true;
            }
        }
        return false;
    }

}