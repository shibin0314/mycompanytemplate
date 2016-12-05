
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.web
*
* @File name : LoginInfoDto.java
*
* @Author : zhangxc
*
* @Date : 2016年6月30日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年6月30日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.yonyou.dms.framework.domain;

import java.util.Locale;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 记录登录的相关信息
 * 
 * @author zhangxc
 * @date 2016年6月30日
 */
@Component
@Scope("session")
public class LoginInfoDto {

    private String  dealerCode;
    private String  userAccount;
    private Long dealerId;
    private String  dealerName;
    private String  dealerShortName;
    private String  userName;
    private Long userId;
    private Locale  locale;
    private String  employeeNo;
    private Integer gender;
    private Long orgId;
    //设置可以管理的仓库
    private String canAccessStores;
    private String carLoadDepot ;               //整车仓库权限
    private String purchaseDepot;               //配件仓库权限
    private String repair;                      //维修权限
    private String purchase;                    //配件权限
    private String preferentialMode;            //优惠模式权限
    
    /**
     * method:module:url,分隔符使用：FrameworkConstants.ACL_RESOUCCE_SPLIT
     * 
     */
    private String[] userResouces; //用户的权限URL列表
    
    
    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    private String orgCode;
    private String orgName;

    public String getDealerCode() {
        return dealerCode;
    }

    public void setDealerCode(String dealerCode) {
        this.dealerCode = dealerCode;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userName) {
        this.userAccount = userName;
    }

    @Override
    public String toString() {
        return "LoginInfoDto [dealerCode=" + dealerCode + ", userName=" + userAccount + "]";
    }

    public Long getDealerId() {
        return dealerId;
    }

    public void setDealerId(Long dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerShortName() {
        return dealerShortName;
    }

    public void setDealerShortName(String dealerShortName) {
        this.dealerShortName = dealerShortName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    
    public Long getOrgId() {
        return orgId;
    }

    
    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    
    public String getCanAccessStores() {
        return canAccessStores;
    }

    
    public void setCanAccessStores(String canAccessStores) {
        this.canAccessStores = canAccessStores;
    }

	public String getCarLoadDepot() {
		return carLoadDepot;
	}

	public void setCarLoadDepot(String carLoadDepot) {
		this.carLoadDepot = carLoadDepot;
	}

	public String getPurchaseDepot() {
		return purchaseDepot;
	}

	public void setPurchaseDepot(String purchaseDepot) {
		this.purchaseDepot = purchaseDepot;
	}

	public String getRepair() {
		return repair;
	}

	public void setRepair(String repair) {
		this.repair = repair;
	}

	public String getPurchase() {
		return purchase;
	}

	public void setPurchase(String purchase) {
		this.purchase = purchase;
	}

	public String getPreferentialMode() {
		return preferentialMode;
	}

	public void setPreferentialMode(String preferentialMode) {
		this.preferentialMode = preferentialMode;
	}

    
    public String[] getUserResouces() {
        return userResouces;
    }

    
    public void setUserResouces(String[] userResouces) {
        this.userResouces = userResouces;
    }
}
