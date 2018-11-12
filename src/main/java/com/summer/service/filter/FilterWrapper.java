package com.summer.service.filter;

import com.alibaba.dubbo.common.utils.StringUtils;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jook
 * @create 2018-10-23 11:57
 **/
public class FilterWrapper {

    public static List<InvokeFilter> createFilter(String filters, ApplicationContext context) {
        List<InvokeFilter> list = new ArrayList<>();
        if (StringUtils.isBlank(filters)) return null;
        String[] strs = filters.split(",");
        for (String beanName : strs) {
            try{
                Object o = context.getBean(beanName);
                if (o != null && o instanceof InvokeFilter)
                    list.add((InvokeFilter) o);
            }catch (Exception e){
            }
        }
        return list;
    }


}
