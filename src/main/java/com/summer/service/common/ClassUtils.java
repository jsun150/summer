package com.summer.service.common;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.jollychic.an.Api;
import com.jollychic.an.Paramter;
import com.summer.service.classLoader.JarLoader;
import com.summer.service.dubbo.InvokeInstance;
import com.summer.service.dubbo.MethodInfo;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 发现服务时候 加载class  -- 全量加载 需要完善
 *
 * @author Jook
 * @create 2018-10-20 14:14
 **/
public class ClassUtils {

    private static final Logger logger = LoggerFactory.getLogger(ClassUtils.class);
//    private static DefaultConversionService service = new DefaultConversionService();

    /**
     * 解析可对外提供服务的接口 method 信息
     *
     * @param invokeBean
     * @return
     */
    public static InvokeInstance.InvokeBean parserInterfaceMethods(InvokeInstance.InvokeBean invokeBean) {
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
                methodInfo.setHttpType(RequestMethod.valueOf(methodApi.httpType()));
                // todo login注解解析

                methodInfo.setMethodName(method.getName());

                //解析param的 参数名字和 参数类型(string) 不能用class
                CtClass[] paramTypes = method.getParameterTypes();
                if (paramTypes == null) continue;
                for (int i = 0; i < paramTypes.length; i++) {
                    LinkedHashMap<String, String> pm = new LinkedHashMap();
                    CtClass pt = paramTypes[i];
                    pt.getName();
                    Paramter ptAnntation = (Paramter) getParamsAnnotation(method, Paramter.class, i);
                    if (ptAnntation != null) {
                        pm.put(ptAnntation.name(), ctClass.getName());
                    } else {
                        logger.info("interface:{} | method:{} .paramter别名未配置", invokeBean.getInter(), method.getName());
                        pm.put("NULL" + i, null);
                    }
                }
                invokeBean.getPathMethodMap().put(invokeBean.getApplication() + "/" + path, methodInfo);
            }
            invokeBean.setPathList(paths);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return invokeBean;
    }

    /**
     * 组装参数
     * key : parameterTypes. 参数类型名字,  args . 参数value数组
     * @return
     */
    public static Map<String, Object[]> packageArgs(RequestContext context, MethodInfo methodInfo) throws Exception {
        Map<String, Object[]> result = null;
        Map<String, String> paramsMap = methodInfo.getParameterClass();
        // 不需要传参数
        if (paramsMap == null) return result;
        // 参数名字
        List<String> paramsTypeName  = new ArrayList<>(paramsMap.size());
        List<Object> paramValue  = new ArrayList<>(paramsMap.size());

        for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
            String requestParamValue = context.getRequestParams().get(entry.getKey());
            //参数 typename
            paramsTypeName.add(entry.getValue());
            if (org.apache.commons.lang.StringUtils.isBlank(requestParamValue)) {
                if (entry.getValue().startsWith("com.jollychic")) {
                    //兼容一些特殊情况.
                    paramValue.add(context.getRequestParams());
                }else {
                    paramValue.add(null);
                }
                continue;
            }
            //参数 value -- 用Map表示POJO参数，如果返回值为POJO也将自动转成Map, 基本类型以及Date,List,Map等不需要转换，直接调用
            if (entry.getValue().startsWith("com.jollychic")) {
                Map<String, Object> map = JSONObject.parseObject(requestParamValue, Map.class);
                paramValue.add(map);
            }else {
                paramValue.add(requestParamValue);
            }
        }
        result.put("parameterTypes",paramsTypeName.toArray());
        result.put("args", paramValue.toArray());
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
