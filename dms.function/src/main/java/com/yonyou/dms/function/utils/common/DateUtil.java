/*
* Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
*
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : cmol.common.function
*
* @File name : DateUtil.java
*
* @Author : ChenPeiYu
*
* @Date : 2016年3月28日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年3月28日    ChenPeiYu    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.yonyou.dms.function.utils.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.yonyou.dms.function.common.CommonConstants;
import com.yonyou.dms.function.exception.UtilException;

/**
 * 
* 日期格式化函数
* @author zhangxc
* @date 2016年6月29日
 */

public class DateUtil {

    
    /**
     * 对日期时间字符串转化为日期对象，格式：yyyy-MM-dd HH:mm:ss
    * TODO description
    * @author zhangxc
    * @date 2016年6月29日
    * @param dateTime
    * @return
     */
    public static Date parseDefaultDateTime(String dateTime){
        return parseDate(dateTime,CommonConstants.FULL_DATE_TIME_FORMAT);
    }
    /**
     * 对日期时间字符串转化为日期对象，格式：yyyy-MM-dd
    * TODO description
    * @author zhangxc
    * @date 2016年6月29日
    * @param dateTime
    * @return
     */
    public static Date parseDefaultDate(String dateTime){
        return parseDate(dateTime,CommonConstants.SIMPLE_DATE_FORMAT);
    }
    
    /**
    * 对日期时间字符串转化为日期对象，格式：yyyy-MM
    * @author zhanshiwei
    * @date 2016年10月11日
    * @param dateTime
    * @return
    */
    	
    public static Date parseDefaultDateMonth(String dateTime){
        return parseDate(dateTime,CommonConstants.SIMPLE_DATE_MONTH_FORMAT);
    }
    
    /**
     * 
    * 格式化默认日期,格式：yyyy-MM-dd
    * @author zhangxc
    * @date 2016年6月29日
    * @param date
    * @return
     */
    public static String formatDefaultDate(Date date) {
        return formatDateByFormat(date,CommonConstants.SIMPLE_DATE_FORMAT);
    }
    
    /**
     * 
    * 格式化默认日期,格式：yyyy-MM-dd HH:mm
    * @author zhangxc
    * @date 2016年6月29日
    * @param date
    * @return
     */
    public static String formatDefaultDateTime(Date date) {
        return formatDateByFormat(date,CommonConstants.SIMPLE_DATE_TIME_FORMAT);
    }
    /**
    * 字符串转化时间
    * @author zhangxc
    * @date 2016年6月29日
    * @param time
    * @param format
    * @return
    * @throws Exception
    */
    	
    public static Date parseDate(String time, String format){
        if(StringUtils.isNullOrEmpty(time)){
            return null;
        }
        if (StringUtils.isNullOrEmpty(format)) {
            throw new UtilException("日期格式不正确");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            return dateFormat.parse(time);
        } catch (Exception e) {
            throw new UtilException("日期转换出错：" + e.getMessage(), e);
        }
    }

    /*
     * @author LiaoYuzhi 根据当前日期计算n天后的日期
     * @date 2016年3月28日
     * @param date
     * @param n
     * @return
     */

    public static Date addDay(Date date, int n) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, n);
        Date destDay = c.getTime();
        return destDay;
    }

    /*
     * @author LiaoYuzhi 以指定的格式来格式化日期
     * @date 2016年3月28日
     * @param date
     * @param format
     * @return
     */

    public static String formatDateByFormat(Date date, String format) {
        String result = StringUtils.EMPTY_STRING;
        if (date != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                result = sdf.format(date);
            } catch (Exception ex) {
                throw new UtilException("日期转换出错：" + ex.getMessage(), ex);
            }
        }
        return result;
    }
    
    /**
    * 加1天
    * @author jcsi
    * @date 2016年8月29日
    * @param param
    * @return
     */
    public static Date addOneDay(Object param){
        return addDay(parseDefaultDate(param+""),1);        
        //return formatDefaultDate(value);
    }
    
    
    /**
    *  指定日期的第一天
    * @author zhanshiwei
    * @date 2016年9月30日
    * @param date
    * @return
    */
    	
    public static Date getFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                     calendar.get(Calendar.MONTH), 1);
        return parseDefaultDate(formatDefaultDate(calendar.getTime()));
    }
    
    /**
    * 指定日期的最后一天
    * @author zhanshiwei
    * @date 2016年9月30日
    * @param date
    * @return
    */
    	
    public static Date getLastDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR),
                     calendar.get(Calendar.MONTH), 1);
        calendar.roll(Calendar.DATE, -1);
        return parseDefaultDate(formatDefaultDate(calendar.getTime()));
    }
    
    
    /**
    * 获取下一个月的第一天
    * @author zhanshiwei
    * @date 2016年9月30日
    * @param date
    * @return
    */
    	
    public static Date getPerFirstDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        return parseDefaultDate(formatDefaultDate(calendar.getTime()));
    }
    
    /**
    * 比较两个时间相差多少分钟
    * @author jcsi
    * @date 2016年10月28日
    * @param endDate  结束时间
    * @param nowDate  开始时间
    * @return
     */
    public static Long toCompareTime(Date endDate,Date nowDate){
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return (Long)day*24+hour*60+min;
    }
    

}
