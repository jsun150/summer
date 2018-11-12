package com.summer.service.common;

import com.jollychic.common.resultutil.BusinessException;

/**
 * @author Jook
 * @create 2018-08-27 16:54
 **/
public class BusinessExceptionUtil {

    public static BusinessException build (MessageCodeEnum codeEnum) {
        return new BusinessException(codeEnum.getMsgCode(), codeEnum.getMsg());
    }
}
