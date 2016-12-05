
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.schedule
*
* @File name : TransactionAutoManagerImpl.java
*
* @Author : zhangxc
*
* @Date : 2016年11月9日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年11月9日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/
	
package com.yonyou.dms.schedule.manager.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Base;
import org.javalite.activejdbc.LazyList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonyou.dms.function.utils.common.CommonUtils;
import com.yonyou.dms.schedule.manager.TransactionAutoManager;
import com.yonyou.dms.schedule.service.AutoTransactionAction;
import com.yonyou.dms.schedule.service.AutoTransactionListAction;
import com.yonyou.f4.common.database.DBService;

/**
* TODO description
* @author zhangxc
 * @param <E>
 * @param <E>
* @date 2016年11月9日
*/
@Service
public class TransactionAutoManagerImpl<T extends List<E>,E> implements TransactionAutoManager<T,E> {

    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(TransactionAutoManagerImpl.class);
    @Autowired
    DBService dbService;
    
    public T autoTransListExcute(String tenantId,AutoTransactionListAction<T> autoTransaction) throws Exception {
        try{
            //开始连接
            dbService.beginTxn(tenantId);
            Connection conn = dbService.openConn(tenantId);
            Base.attach(conn);
            //执行业务逻辑
            T returnList;
            T returnResult =  autoTransaction.autoTransAction();
            if(returnResult instanceof LazyList){
                if(!CommonUtils.isNullOrEmpty(returnResult)){
                    List<Object> conventList = new ArrayList<Object>();
                    for(Object fileUploadInfo:returnResult){
                        conventList.add(fileUploadInfo);
                    }
                    returnList = (T)conventList;
                }else{
                    return null;
                }
            }else{
                returnList = returnResult;
            }
            //结束事务
            dbService.endTxn(true);
            
            return returnList;
        }catch(Exception e){
            try{
                dbService.endTxn(false);
            }catch(Exception ex){
                logger.error(ex.getMessage(),ex);
            }
          throw e;
        }finally{
            Base.detach();
            dbService.clean();
        }
        
    }

    /**
     * 执行数据库操作，但是不返回值
    * @author zhangxc
    * @date 2016年11月9日
    * @param tenantId
    * @param action
    * @throws Exception
    * (non-Javadoc)
    * @see com.yonyou.dms.schedule.manager.TransactionAutoManager#autoTransExcute(java.lang.String, com.yonyou.dms.schedule.service.AutoTransactionAction)
     */
    @Override
    public void autoTransExcute(String tenantId,E dataValue, AutoTransactionAction<E> autoTransaction) throws Exception {
        try{
            //开始连接
            dbService.beginTxn(tenantId);
            Connection conn = dbService.openConn(tenantId);
            Base.attach(conn);
            //执行业务逻辑
            autoTransaction.autoTransAction(dataValue);
            
            //结束事务
            dbService.endTxn(true);
            
        }catch(Exception e){
            try{
                dbService.endTxn(false);
            }catch(Exception ex){
                logger.error(ex.getMessage(),ex);
            }
          throw e;
        }finally{
            Base.detach();
            dbService.clean();
        }
        
    }

}
