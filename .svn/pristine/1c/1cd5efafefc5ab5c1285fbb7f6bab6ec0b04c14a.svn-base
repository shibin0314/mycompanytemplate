
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.framework
*
* @File name : TenantDealerMappingServiceImpl.java
*
* @Author : zhangxc
*
* @Date : 2016年11月4日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年11月4日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/
	
package com.yonyou.dms.framework.service.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;

import com.yonyou.dms.framework.service.TenantDealerMappingService;
import com.yonyou.f4.common.database.DBService;
import com.yonyou.f4.common.database.JdbcTemplate;
import com.yonyou.f4.common.database.impl.DBServiceImpl;


/**
* 加载租户与经销商的对应关系
* @author zhangxc
* @date 2016年11月4日
*/
public class TenantDealerMappingServiceImpl implements TenantDealerMappingService{

    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(TenantDealerMappingServiceImpl.class);
    
    private DBService dbService ;
    private Timer timer = null;

    @Value("${f4.multi.tenant}")
    public boolean isTenant = false;
    @Value("${f4.def.txn}")
    public String defTxn;
    @Value("${f4.def.ds}")
    public String defDs;
    @Value("${f4.def.schema}")
    public String defSchema;
    @Autowired
    ApplicationContext ac;
    private Map<String,Map<String,String>> mapingMap;
    
    /**
     * 
    * 执行加载
    * @author zhangxc
    * @date 2016年11月4日
     */
    @PostConstruct
    public void init() {
        dbService = (DBServiceImpl)ac.getBean(DBService.class);
        if(isTenant) {
            //加载对应关系
            loadTenantMapping();
            
            this.timer = new Timer("TenantDealerMappingLoader");
            this.timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {
                    loadTenantMapping();
                }
            }, 5 * 60 * 1000, 5 * 60 * 1000);
            logger.info("TenantDealerMappingLoader has been scheduled to start!");
        }
    }
    
    /**
     * 
    * 销毁资源
    * @author zhangxc
    * @date 2016年11月4日
     */
    @PreDestroy
    public void destory() {
        if(isTenant) {
            //Stop the timer
            if(this.timer != null) {
                this.timer.cancel();
                this.timer = null;
                logger.info("TenantInfoLoader will be stop!");
            }
        }
    }
    /**
     * 加载所有的Mapping 关系
    * @author zhangxc
    * @date 2016年11月4日
    * @return
    * (non-Javadoc)
    * @see com.yonyou.dms.framework.service.TenantDealerMappingService#getAll()
     */
    @Override
    public Map<String,Map<String,String>> getAll() {
        if(mapingMap==null&& isTenant==true){
            synchronized (mapingMap) {
                loadTenantMapping();
            }
        }
        return mapingMap;
    }

    /**
     * 加载有效的经销商信息
    * @author zhangxc
    * @date 2016年11月4日
    * @return
    * (non-Javadoc)
    * @see com.yonyou.dms.framework.service.TenantDealerMappingService#getValid()
     */
    @Override
    public Map<String,Map<String,String>> getValid() {
        return null;
    }
    
    /**
     * 
    * 执行初始化
    * @author zhangxc
    * @date 2016年11月4日
     */
    private void loadTenantMapping() {
        try{
            dbService.beginTxnById(defTxn, 30);
            Connection conn = dbService.openConnById(defDs);
            JdbcTemplate tpl = new JdbcTemplate(conn);
            List<Map<String,String>> ret= tpl.query("select DEALER_CODE,DEALER_SHORTNAME,TENANT_ID from f4_tenant_dealer_mapping order by DEALER_CODE", new JdbcTemplate.RowMapper<Map<String,String>>() {
                public Map<String, String> mapper(ResultSet rs) throws Exception {
                    Map<String,String> tenant = new HashMap<String, String>();
                    tenant.put("DEALER_CODE", rs.getString("DEALER_CODE") );
                    tenant.put("DEALER_SHORTNAME", rs.getString("DEALER_SHORTNAME"));
                    tenant.put("TENANT_ID", rs.getString("TENANT_ID"));
                    return Collections.synchronizedMap(tenant);
                }
            });
            
            Map<String,Map<String,String>> mps = new HashMap<String, Map<String, String>>();
            for(Map<String,String> t : ret ){
                mps.put(t.get("DEALER_CODE"), t);
            }

            this.mapingMap = Collections.synchronizedMap(mps);
            
        }catch(Exception e){
            logger.debug("加载租户与经销商关系失败",e);
        }finally{
            try{
                dbService.endTxn(true);
                dbService.clean();
            }catch(Exception e){
                logger.debug("清理资源失败",e);
            }
        }
    }
}
