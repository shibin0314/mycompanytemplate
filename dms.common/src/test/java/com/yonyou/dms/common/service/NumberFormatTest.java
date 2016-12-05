
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.common
*
* @File name : NumberFormatTest.java
*
* @Author : zhangxc
*
* @Date : 2016年8月31日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年8月31日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/
	
package com.yonyou.dms.common.service;

import java.text.DecimalFormat;

import org.junit.Test;

import com.yonyou.dms.function.utils.common.NumberUtil;


/**
* 数字格式化Test
* @author zhangxc
* @date 2016年8月31日
*/

public class NumberFormatTest {

    @Test
    public void test() {
        double value = 100000000.00001;
        System.out.println(NumberUtil.getShortString(value));
        System.out.println(String.valueOf(value));
        
        value = 100000000;
        DecimalFormat df = new DecimalFormat("0.########");
        String formatValue = df.format(value);
        System.out.println(formatValue);
        String[] digitArray = formatValue.split("\\.");
        System.out.println(digitArray.length);
        if(digitArray.length>=1){
            int digitLength = digitArray==null?formatValue.length():digitArray[0].length();
            System.out.println(digitLength);
        }
        
    }

}
