package com.summer.service.http;

import com.alibaba.dubbo.common.utils.IOUtils;
import com.alibaba.fastjson.JSON;
import com.summer.service.common.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jook
 * @create 2018-10-22 20:40
 **/
public class HttpUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 解析http
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static RequestBean parser(HttpServletRequest request) throws Exception {
        RequestBean bean = new RequestBean();
        bean.setHttpType(request.getMethod());
        if (bean.getHttpType().equalsIgnoreCase(Constants.HTTP_POST)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String body = IOUtils.read(reader);
            bean.setPostJson(body);
            bean.setRequestParams(parser(body));
        } else {
            bean.setRequestParams(request.getParameterMap());
        }
        bean.setPath(request.getServletPath().replaceFirst("/api/", ""));
        return bean;

    }

    /**
     * body解析成map
     *
     * @param body
     * @return
     */
    private static Map<String, String[]> parser(String body) {
        Map<String, Object> map = JSON.parseObject(body, Map.class);
        Map<String, String[]> resultMap = new HashMap<>();
        map.forEach((k, v) -> {
            String[] values = new String[1];
            values[0] = v.toString();
            resultMap.put(k, values);
        });
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
