/*
* Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
*
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : cmol.common.function
*
* @File name : SecurityParamUtil.java
*
* @Author : 
*
* @Date : 2016年2月26日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年2月29日                                     1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.yonyou.dms.function.utils.security;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.yonyou.dms.function.exception.UtilException;
import com.yonyou.dms.function.utils.common.StringUtils;

/*
*安全参数util
* @author 
* 
* @date 2016年2月29日
*/

public class SecurityParamUtil {

    /*
     * @author 特殊字符转化(值对象)
     * @date 2016年2月29日
     * @param model
     */
    public static void filterSpecialWord(Object model) throws UtilException {
        if (model != null) {
            Field[] field = model.getClass().getDeclaredFields();// 获取实体类的所有属性，返回Field数组
            for (int j = 0; j < field.length; j++) {// 遍历所有属性
                try {
                    String name = field[j].getName();// 获取属性的名字
                    String upperName = firstWordToUpper(name);// 首字母变大写
                    String type = field[j].getGenericType().toString();// 获取属性的类型
                    if (type.equals("class java.lang.String")) {// 如果type是类类型，则前面包含"class "，后面跟类名
                        Method m = model.getClass().getMethod("get" + upperName);
                        String value = (String) m.invoke(model);// 调用getter方法获取属性值
                        Method n = model.getClass().getDeclaredMethod("set" + upperName, String.class);
                        n.setAccessible(true);
                        n.invoke(model, parseSpecialWord(value));// 调用setter方法设定属性值
                    }
                } catch (Exception e) {
                    throw new UtilException("filterSpecialWord is error", e);
                }
            }
        }
    }

    /**
     * 功能说明 : 首字母转大写 创建者 : admin 修改日期 : 2015年8月19日
     * 
     * @param String name
     * @return name
     */
    public static String firstWordToUpper(String name) throws UtilException {
        String firstWord = StringUtils.EMPTY_STRING;
        String otherWord = StringUtils.EMPTY_STRING;
        ;
        if (!StringUtils.isNullOrEmpty(name)) {
            firstWord = name.substring(0, 1).toUpperCase();
            otherWord = name.substring(1, name.length());
            name = firstWord.concat(otherWord);
        }
        return name;
    }

    /*
     * @author 特殊字符转化(String)
     * @date 2016年2月29日
     * @param value
     * @return
     */
    public static String parseSpecialWord(String value) throws UtilException {
        // You'll need to remove the spaces from the html entities below
        /*
         * value = value.replaceAll("<script>", ""); value = value.replaceAll("</script>", ""); value =
         * value.replaceAll("alert", ""); value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;"); value =
         * value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;"); value = value.replaceAll("'", "&#39;"); value =
         * value.replaceAll("eval\\((.*)\\)", ""); value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']",
         * "\"\"");
         */
        return StringUtils.escapeHtml(value);
    }

}
