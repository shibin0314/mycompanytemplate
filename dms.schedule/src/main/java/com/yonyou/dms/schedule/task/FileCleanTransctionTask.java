
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.schedule
*
* @File name : FileCleanTask.java
*
* @Author : zhangxc
*
* @Date : 2016年11月8日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年11月8日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/
	
package com.yonyou.dms.schedule.task;


import java.sql.Connection;
import java.util.Date;
import java.util.List;

import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.yonyou.dms.framework.domains.PO.file.FileUploadInfoPO;
import com.yonyou.dms.framework.service.FileStoreService;
import com.yonyou.dms.function.common.DictCodeConstants;
import com.yonyou.dms.function.utils.common.CommonUtils;
import com.yonyou.dms.function.utils.common.DateUtil;
import com.yonyou.f4.common.database.DBService;
import com.yonyou.f4.mvc.annotation.TxnConn;
import com.yonyou.f4.schedule.task.TenantSingletonTask;

/**
* 文件清理的task
* @author zhangxc
* @date 2016年11月8日
*/

@TxnConn
public class FileCleanTransctionTask extends TenantSingletonTask{

    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(FileCleanTransctionTask.class);
    @Autowired
    FileStoreService fileService;
    @Autowired
    DBService dbService;
    
    
    public void execute() throws Exception {
        try{
            System.out.println("into FileCleanTransctionTask");
            System.out.println(dbService.isTenantMode());
            String tenantId = super.getTriggerInfo().getTenants();
            //开始连接
            dbService.beginTxn(tenantId);
            Connection conn = dbService.openConn(tenantId);
            Base.attach(conn);
            
            //查询无效的文件
            List<FileUploadInfoPO> invalidFiles = FileUploadInfoPO.find("IS_VALID = ? and CREATED_AT < ?", DictCodeConstants.STATUS_NOT_VALID,DateUtil.addDay(new Date(),-1));
            if(!CommonUtils.isNullOrEmpty(invalidFiles)){
              
                //查询无效的文件
                for(FileUploadInfoPO fileUploadInfo:invalidFiles){
                    String fileId = null;
                    try{
                        fileId = fileUploadInfo.getString("FILE_ID");
                        fileService.deleteFile(fileId);
                        fileUploadInfo.delete();
                    }catch(Exception e){
                        logger.error("清理文件失败:"+fileId,e);
                    }
                }
            }
            dbService.endTxn(true);
        }catch(Exception e){
            dbService.endTxn(false);
            logger.error(e.getMessage(),e);
        }finally{
            Base.detach();
            dbService.clean();
        }
       
    }

}
