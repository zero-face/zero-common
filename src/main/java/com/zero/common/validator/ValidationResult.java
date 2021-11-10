package com.zero.common.validator;




import com.sun.deploy.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Zero
 * @Date: 2021/4/3 - 22:31
 * @since: jdk 1.8
 */
public class ValidationResult {
    //校验结果是否有错
    private boolean hasErrs = false;
    //存放错误信息的map
    private Map<String,String> errMsgMap = new HashMap<>();

    public boolean isHasErrs() {
        return hasErrs;
    }

    public void setHasErrs(boolean hasErrs) {
        this.hasErrs = hasErrs;
    }

    public Map<String, String> getErrMsgMap() {
        return errMsgMap;
    }

    public void setErrMsgMap(Map<String, String> errMsgMap) {
        this.errMsgMap = errMsgMap;
    }
    //实现通用的通过格式化字符串信息获取错误结果的方法
    public String getErrMsg() {
        return StringUtils.join(errMsgMap.values().toArray(),",");
    }



}
