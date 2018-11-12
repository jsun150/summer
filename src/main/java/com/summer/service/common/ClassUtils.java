package com.summer.service.common;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jollychic.an.Api;
import com.jollychic.an.Paramter;
import com.summer.service.classLoader.JarLoader;
import com.summer.service.dubbo.InvokeInstance;
import com.summer.service.dubbo.MethodInfo;
import com.summer.service.http.RequestBean;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * 发现服务时候 加载class  -- 全量加载 需要完善
 *
 * @author Jook
 * @create 2018-10-20 14:14
 **/
public class ClassUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);
    private static DefaultConversionService service = new DefaultConversionService();

    /**
     * 解析可对外提供服务的接口
     *
     * @param invokeBean
     * @return
     */
    public static List<String> initInterfaceMethods(InvokeInstance.InvokeBean invokeBean) {
        try {
            ClassPool pool = JarLoader.getPool(invokeBean.getApplication());
            CtClass ctClass = pool.get(invokeBean.getInter());
            if (!ctClass.isInterface()) return null;
            //收集所有的http请求路径
            List<String> paths = new ArrayList<>();
            CtMethod[] methods = ctClass.getMethods();
            for (CtMethod method : methods) {
                //过滤过期的方法
                Deprecated deprecated = (Deprecated) method.getAnnotation(Deprecated.class);
                com.jollychic.an.Api methodApi = (Api) method.getAnnotation(Api.class);
                if (methodApi == null) continue;
                String path = methodApi.path();
                if (StringUtils.isBlank(path)) continue;
                paths.add(path);
                if (deprecated != null || methodApi == null) continue;
                //组装methodinfo
                MethodInfo methodInfo = new MethodInfo();
                ParameterInfo parameterInfo = new ParameterInfo();
                methodInfo.setParameterInfo(parameterInfo);

                methodInfo.setMethodName(method.getName());
                CtClass[] paramTypes = method.getParameterTypes();
                if (paramTypes == null) {
                    parameterInfo.setParamType(Constants.PARAMTER_TYPE_NULL);
                } else if (paramTypes != null && paramTypes.length == 1 && paramTypes[0].getName().startsWith("com.jollychic")) {//bean类型参数
                    CtClass paramClass = pool.get(method.getParameterTypes()[0].getName());
                    Map<String, String> map = new HashMap<>();
                    CtField[] ctFields = paramClass.getDeclaredFields();
                    for (CtField ctField : ctFields) {
                        map.put(ctField.getName(), ctField.getType().getName());
                    }
                    String superClassName = paramClass.getSuperclass().getName();
                    if (!StringUtils.isBlank(superClassName) && !superClassName.equals("java.lang.Object")) {
                        CtClass superClass = pool.get(superClassName);
                        for (CtField ctField : superClass.getDeclaredFields()) {
                            map.put(ctField.getName(), ctField.getType().getName());
                        }
                    }
                    parameterInfo.setBeanParamMap(map);
                    parameterInfo.setParamType(Constants.PARAMTER_TYPE_BEAN);
                    parameterInfo.setBeanParamName(paramTypes[0].getName());
                } else { //其他
                    LinkedHashMap pm = new LinkedHashMap();
                    for (int i = 0; i < paramTypes.length; i++) {
                        CtClass pt = paramTypes[i];
                        pt.getName();
                        Paramter ptAnntation = (Paramter) getParamsAnnotation(method, Paramter.class, i);
                        if (ptAnntation != null) {
                            pm.put(ptAnntation.name(), pt.getName());
                        } else {
                            logger.info("interface:{} | method:{} .paramter别名未配置", invokeBean.getInter(), method.getName());
                            pm.put("NULL" + i, null);
                        }
                    }
                    parameterInfo.setParamsMap(pm);
                    parameterInfo.setParamType(Constants.PARAMTER_TYPE_NOT_BEAN);
                }
                invokeBean.getPathMethodMap().put(invokeBean.getApplication() + "/" + path, methodInfo);
            }
            return paths;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 组装参数
     *
     * @return
     */
    public static Object[][] packageArgs(RequestBean requestBean, MethodInfo methodInfo) throws Exception {
        Object[][] result = null;
        ParameterInfo parameterInfo = methodInfo.getParameterInfo();
        //不需要参数传入
        if (parameterInfo.getParamType() == Constants.PARAMTER_TYPE_NULL) return null;
        //需要参数
        if (parameterInfo.getParamType() == Constants.PARAMTER_TYPE_BEAN) {
            result = new Object[2][1];
            Map<String, Object> invokeParam = new HashMap<>();
            JSONObject json = JSONObject.parseObject(requestBean.getPostJson());
            Map<String, String> map = parameterInfo.getBeanParamMap();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey(), classType = entry.getValue();
                if (json.get(key) != null)
                    invokeParam.put(key, service.convert(json.getString(key), Class.forName(classType)));
            }
            result[0][0] = methodInfo.getParameterInfo().getBeanParamName();
            result[1][0] = invokeParam;
        } else if (parameterInfo.getParamType() == Constants.PARAMTER_TYPE_NOT_BEAN) {
            if (CollectionUtils.isEmpty(requestBean.getRequestParams())) return null;
            int size = parameterInfo.getParamsMap().size();
            result = new Object[2][size];
            Object[] keyResult = new Object[size];
            Object[] valueReuslt = new Object[size];
            //按照methodinfo的参数 顺序 封装参数
            int count = 0;
            for (Map.Entry<String, String> entry : parameterInfo.getParamsMap().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                keyResult[count] = value;
                String[] params = requestBean.getRequestParams().get(key);
                if (params == null || params.length == 0) {
                    valueReuslt[count] = null;
                } else {
                    valueReuslt[count] = service.convert(params[0], Class.forName(value));
                }
            }
            result[0] = keyResult;
            result[1] = valueReuslt;
        }
        return result;
    }

    public static Object getParamsAnnotation(CtMethod method, Class anntationClass, int index) {
        try {
            Object[] ans = method.getParameterAnnotations()[index];
            if (ans != null && ans.length > 0) {
                for (Object an : ans) {
                    if (an.toString().contains(anntationClass.getName())) {
                        return an;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
