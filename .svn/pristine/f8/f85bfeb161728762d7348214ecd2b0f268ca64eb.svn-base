
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.framework
*
* @File name : AclDmsResouce.java
*
* @Author : zhangxc
*
* @Date : 2016年11月17日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年11月17日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/
	
package com.yonyou.dms.framework.interceptors.acl;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yonyou.dms.framework.domain.LoginInfoDto;
import com.yonyou.dms.framework.interceptors.ExceptionControllerAdvice;
import com.yonyou.dms.framework.util.bean.AppliactionContextHelper;
import com.yonyou.f4.common.acl.AclResource;


/**
* 实现AclResource 方法
* @author zhangxc
* @date 2016年11月17日
*/

public class AclDmsResouce extends AclResource {
    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    private String module;
    private String actionUrl;
    private String method;
    /**
     * 
    * 构造对象
    * @author zhangxc
    * @date 2016年11月17日
    * @param module
    * @param actionUrl
    * @param method
     */
    public AclDmsResouce(String method,String module,String actionUrl){
        this.module = module;
        this.actionUrl = actionUrl;
        this.method = method;
    }
    /**
    * @author zhangxc
    * @date 2016年11月17日
    * @param obj:method:requestUrl 
    * @return
    * (non-Javadoc)
    * @see com.yonyou.f4.common.acl.AclResource#check(java.lang.Object)
    */
    @Override
    public boolean check(Object obj) {
        logger.debug("check URL:"+obj);
        boolean success = false;
        LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
        String[] urlString = loginInfo.getUserResouces();
        String[] defalt = {"GET:/dms.web/web/rest/common/login","DELETE:/dms.web/web/rest/common/login","GET:/dms.web/manage/rest/basedata/dealers","GET:/dms.web/web/rest/common/commonDatas",
					       "GET:/dms.web/web/rest/common/login/111/menus/cn","GET:/dms.web/web/rest/basedata/dicts",
					       "GET:/dms.web/customer/rest/ordermanage/confirmcar/quickQuery","GET:/dms.web/repair/rest/order/repair/queryInWorkOrder/homePage",
					       "GET:/dms.web/customer/rest/customerManage/salesPromotion/quickQuery"," GET:/dms.web/web/rest/common/login/111/handles/cn",
					       "GET:/dms.web/web/rest/common/login/111/handles/cn","GET:/dms.web/report/rest/homePage/statistical"};
        //默认通过的URL
       for(int i=0;i<defalt.length;i++){
    	   if(defalt[i].equals(obj)){
    		   success=true;
    		   break;
    	   }
       }
       //对未通过URL进行权限检验
        if(urlString!=null&&success==false){
	        for(int i =0;i<urlString.length;i++){
	        	success = Pattern.compile(urlString[i]).matcher((CharSequence) obj).matches() ;
	        	if(success){
	        		break;
	        	}
	        }
        }
        return true;
    }

}
