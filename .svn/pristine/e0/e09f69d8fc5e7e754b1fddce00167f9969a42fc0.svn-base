
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.function
*
* @File name : DAOUtil.java
*
* @Author : zhangxc
*
* @Date : 2016年7月6日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年7月6日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.yonyou.dms.framework.DAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.Paginator;
import org.javalite.activejdbc.RowProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yonyou.dms.framework.domain.LoginInfoDto;
import com.yonyou.dms.framework.domain.RequestPageInfoDto;
import com.yonyou.dms.framework.util.bean.AppliactionContextHelper;
import com.yonyou.dms.function.common.CommonConstants;
import com.yonyou.dms.function.exception.DALException;
import com.yonyou.dms.function.utils.common.StringUtils;

/**
 * 定义数据库层的util 方法
 * 
 * @author zhangxc
 * @date 2016年7月6日
 */

public class DAOUtil {

    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(DAOUtil.class);
    /**
     * 根据sql 语句进行查询
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    public static Map findFirst(String sql, List queryParam) {
        if(queryParam==null){
            queryParam = new ArrayList<Object>();
        }
        //获得封装了数据权限的SQL
        String sqlFinal = getAclSql(sql,queryParam);
        List<Map> result =  Base.findAll(sqlFinal, queryParam.toArray());
        if(result==null){
            return null;
        }
        if(result!=null&&result.size()==1){
            return result.get(0);
        }else{
            throw new DALException("返回的条数不正确");
        }
    }
    
    /**
     * 
    * 根据自定义接口查询对应的数据
    * @author zhangxc
    * @date 2016年7月7日
    * @param sql
    * @param queryParam
    * @param defindProcessor
    * @return
     */
    public static Map findFirst(String sql, List queryParam, 
                                    DefinedRowProcessor defindProcessor) {
        if(queryParam==null){
            queryParam = new ArrayList<Object>();
        }
        //获得封装了数据权限的SQL
        String sqlFinal = getAclSql(sql,queryParam);
        RowProcessor processor =  Base.find(sqlFinal, queryParam.toArray());
        processor.with(defindProcessor);
        List<Map> result = defindProcessor.getResult();
        if(result==null){
            return null;
        }
        if(result!=null&&result.size()==1){
            return result.get(0);
        }else{
            throw new DALException("返回的条数不正确");
        }
    }
    
    /**
     * 
    * 执行权限查询
    * @author zhangxc
    * @date 2016年8月1日
    * @param sql
    * @param queryParam
    * @return
     */
    public static List<Map> findAll(String sql, List queryParam) {
        return findAll(sql,queryParam,true);
    }
    /**
     * 根据sql 语句进行查询
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    public static List<Map> findAll(String sql, List queryParam,boolean isAclCheck) {
        //获得封装了数据权限的SQL
        if(queryParam==null){
            queryParam = new ArrayList<Object>();
        }
        String sqlFinal = null;
        if(isAclCheck){
            sqlFinal = getAclSql(sql,queryParam);  
        }else{
            sqlFinal = sql;
        }
        
        return Base.findAll(sqlFinal, queryParam.toArray());
    }
    
    /**
     * 
    * 根据自定义接口查询对应的数据
    * @author zhangxc
    * @date 2016年7月7日
    * @param sql
    * @param queryParam
    * @param defindProcessor
    * @return
     */
    public static List<Map> findAll(String sql, List queryParam, 
                                    DefinedRowProcessor defindProcessor) {
        if(queryParam==null){
            queryParam = new ArrayList<Object>();
        }
        //获得封装了数据权限的SQL
        String sqlFinal = getAclSql(sql,queryParam);
        RowProcessor processor =  Base.find(sqlFinal, queryParam.toArray());
        processor.with(defindProcessor);
        return defindProcessor.getResult();
    }
    
    
    
    /**
     * 根据sql 语句进行分页查询
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    public static PageInfoDto pageQuery(String sql, List queryParam) {
        if(queryParam==null){
            queryParam=new ArrayList<String>();
        }
        return commonPageQuery(sql,queryParam,null);
    }

    /**
     * 根据sql 语句进行分页查询,通过自定义实现接口
     * @date 2016年7月6日
     * @param sql
     * @param queryParam
     * @return
     */
    public static PageInfoDto pageQuery(String sql, List queryParam, 
                                        DefinedRowProcessor processor) {
        return commonPageQuery(sql,queryParam,processor);
    }
    
    /**
     * 
    * 通用功能查询
    * @author zhangxc
    * @date 2016年7月7日
    * @param sql 查询的SQL 语句
    * @param queryParam 查询的参数
    * @param processor 转换器
    * @return
     */
    private static PageInfoDto commonPageQuery(String sql, List queryParam,DefinedRowProcessor processor){
        
        
        //获取分页信息
        RequestPageInfoDto requestPageInfoDto = AppliactionContextHelper.getBeanByType(RequestPageInfoDto.class);
        Integer pageSize = Integer.parseInt(requestPageInfoDto.getLimit());
        String sort = requestPageInfoDto.getSort();
        String order = requestPageInfoDto.getOrder();
        Integer offset = Integer.parseInt(requestPageInfoDto.getOffset());
        int page = (offset / pageSize) + 1;
        
        //定义排序字段
        String orders = null;
        if (!StringUtils.isNullOrEmpty(sort)) {
            orders = sort + " " + order;
        }
        //获得封装了数据权限的SQL
        String sqlFinal = getAclSql(sql,queryParam);   
        Paginator paginator = new Paginator(pageSize, sqlFinal ,queryParam.toArray());  
        if(orders!=null){
            paginator.orderBy(orders);
        }
        PageInfoDto pageDto = new PageInfoDto();
        pageDto.setTotal(paginator.getCount());
        List<Map> results = null;
        if(processor==null){
            results = paginator.getPage(page);
        }else{
            paginator.getPage(page, processor);
            results = processor.getResult();
        }
        pageDto.setRows(results);
        return pageDto;
    }
    
    /**
     * 
    * 获取拼装后Dealer_Code 的SQL 语句
    * @author zhangxc
    * @date 2016年7月7日
    * @param sql
    * @param queryParam
    * @return
     */
    private static String getAclSql(String sql, List queryParam){
        //拼装Dealer_code 字段
        LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
        if(!StringUtils.isNullOrEmpty(loginInfo)&&!StringUtils.isNullOrEmpty(loginInfo.getDealerCode())){
            String dealerCode = loginInfo.getDealerCode();
            StringBuilder sbFinal = new StringBuilder();
            sbFinal.append("select * from (").append(sql).append(") tt where DEALER_CODE in (?,'").append(CommonConstants.PUBLIC_DEALER_CODE).append("')");
            queryParam.add(dealerCode);
            sql = sbFinal.toString();
        }
        return sql;
    }
    
    /**
     * 
    * 获得字段的本地化值
    * @author zhangxc
    * @date 2016年8月29日
    * @return
     */
    public static String getLocaleFieldValue(Map rowMap,String fieldName){
        String dbLanguage = getDBLanguage();
        if(dbLanguage==null||dbLanguage.equals("ZH")){
            return (String)rowMap.get(fieldName);
        }else{
            String returnValue = null;
            if((returnValue=(String)rowMap.get(fieldName+"_"+dbLanguage))!=null){
                return returnValue;
            }
            return (String)rowMap.get(fieldName+"_"+dbLanguage.toLowerCase());
        }
    }
    
    /**
     * 
    * 获得当前
    * @author zhangxc
    * @date 2016年8月29日
    * @return
     */
    private static String getDBLanguage(){
        LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
        Locale locale = loginInfo.getLocale();
        if(locale!=null){
            String language = locale.getLanguage();
            return language.split("_")[0].toUpperCase();
        }else{
            return null;
        }
    }
}
