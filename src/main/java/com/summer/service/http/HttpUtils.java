package com.summer.service.http;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.dubbo.common.utils.IOUtils;
import com.alibaba.fastjson.JSON;
import com.summer.service.common.Constants;
import com.summer.service.common.MessageCodeEnum;
import com.summer.service.common.RequestContext;
import com.summer.service.exception.SuException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jook
 * @create 2018-10-22 20:40
 **/
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 解析请求上下文
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static RequestContext parserRequest(HttpServletRequest request) throws Exception {
        RequestContext context = new RequestContext();
        context.setPath(request.getServletPath().replaceFirst("/api/", ""));
        //解析参数
        if (request.getMethod().equals(RequestMethod.GET.name())) { //get
            context.setRequestParams(parserGetParam(request.getParameterMap()));
            context.setRequestMethod(RequestMethod.GET);
        } else if (request.getMethod().equals(RequestMethod.POST.name())) { //post
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            context.setRequestParams(parserPostParam(IOUtils.read(reader)));
            context.setRequestMethod(RequestMethod.GET);
        } else {
            throw new SuException(MessageCodeEnum.PATH_NOT_FOUND);
        }
        return context;
    }

    private static Map<String, String> parserGetParam(Map<String, String[]> map) {
        Map<String, String> resultMap = new HashMap<>();
        if (map == null) return resultMap;
        map.forEach((k,v)-> resultMap.put(k, v[0]));
        return resultMap;
    }

    /**
     * body解析成map
     *
     * @param body
     * @return
     */
    private static Map<String, String> parserPostParam(String body) {
        Map<String, String> resultMap = new HashMap<>();
        if (StringUtils.isBlank(body)) return resultMap;
        Map<String, Object> map = JSON.parseObject(body, Map.class);
        map.forEach((k, v) -> resultMap.put(k, String.valueOf(v)));
        return resultMap;
    }

    /**
     * 输出不同格式的内容给HTTP客户端
     *
     * @param response
     * @param content
     * @param contentType
     */
    public static void writeResponse(HttpServletResponse response, String content, ContentTypeEnum contentType, String jsonpCallBack) {
        PrintWriter out = null;
        try {
            response.setCharacterEncoding("UTF-8");
            if (content == null) {
                content = "";
            }
            out = response.getWriter();

            switch (contentType) {
                case HTML:
                    response.setContentType("text/html");
                    out.write(content);
                    break;
                case JSON:
                    response.setContentType("application/Json");
                    out.write(content);
                    break;
                case JSONP:
                    response.setContentType("text/plain");
                    response.setHeader("Pragma", "No-cache");
                    response.setHeader("Cache-Control", "no-cache");
                    response.setDateHeader("Expires", 0);
                    out.write(jsonpCallBack + "(" + content + ")");
                default:
                    break;
            }
            out.flush();
        } catch (IOException e) {
            logger.error("Print output stream to client Error");
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}
