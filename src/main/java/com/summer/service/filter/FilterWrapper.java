package com.summer.service.filter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.summer.service.common.PropertyConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jook
 * @create 2018-10-23 11:57
 **/
@Component
public class FilterWrapper implements ApplicationContextAware {

    private ApplicationContext context;
    private List<InvokeFilter> invokeFilters;

    @PostConstruct
    public void initFilter() {
        this.invokeFilters = createFilter(PropertyConfigurer.getProperty("invoke.filter"), context);
    }

    private List<InvokeFilter> createFilter(String filters, ApplicationContext context) {
        if (StringUtils.isBlank(filters)) return null;
        List<InvokeFilter> list = new ArrayList<>();
        String[] strs = filters.split(",");
        for (String beanName : strs) {
            try {
                Object o = context.getBean(beanName);
                if (o != null && o instanceof InvokeFilter)
                    list.add((InvokeFilter) o);
            } catch (Exception e) {
            }
        }
        return list;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    public List<InvokeFilter> getInvokeFilters() {
        return invokeFilters;
    }

    public void setInvokeFilters(List<InvokeFilter> invokeFilters) {
        this.invokeFilters = invokeFilters;
    }
}
