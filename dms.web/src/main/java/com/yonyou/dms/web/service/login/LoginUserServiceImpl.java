
/** 
 *Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
 * This software is published under the terms of the Yonyou Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * @Project Name : dms.common
 *
 * @File name : SystemUserServiceImpl.java
 *
 * @Author : yll
 *
 * @Date : 2016年10月8日
 *
----------------------------------------------------------------------------------
 *     Date       Who       Version     Comments
 * 1. 2016年10月8日    yll    1.0
 *
 *
 *
 *
----------------------------------------------------------------------------------
 */

package com.yonyou.dms.web.service.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yonyou.dms.framework.DAO.DAOUtil;
import com.yonyou.dms.framework.domain.LoginInfoDto;
import com.yonyou.dms.framework.interceptors.ExceptionControllerAdvice;
import com.yonyou.dms.framework.util.bean.AppliactionContextHelper;
import com.yonyou.dms.function.exception.ServiceBizException;
import com.yonyou.dms.function.utils.common.CommonUtils;
import com.yonyou.dms.function.utils.common.StringUtils;

/**
 * 
 * @author yll
 * @date 2016年10月8日
 */
@Service
public class LoginUserServiceImpl implements LoginUserService{
	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
	/**
	 * 登录校验
	 * @author yll
	 * @date 2016年10月10日
	 * @param dealerCode
	 * @param userCode
	 * (non-Javadoc)
	 * @see com.yonyou.dms.web.service.login.LoginUserService#logCheck(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
	public Map  logCheck(String dealerCode, String userCode,String password) {
		//判断用户是否存在
		Map information=new HashMap();
		StringBuilder sql = new StringBuilder("SELECT tdb.DEALER_CODE,te.GENDER,te.EMPLOYEE_NO,tu.PASSWORD,te.EMPLOYEE_NAME, tdb.DEALER_SHORTNAME, tdb.DEALER_NAME, tdb.DEALER_ID, tu.USER_ID,tu.USER_CODE,te.ORG_CODE,org.ORG_NAME,org.organization_id FROM tm_employee te INNER JOIN tm_user tu ON te.EMPLOYEE_ID=tu.EMPLOYEE_ID LEFT  JOIN tm_dealer_basicinfo tdb ON tdb.DEALER_CODE=te.DEALER_CODE left join TM_DEALER_ORGANIZATION org on org.ORG_CODE=te.ORG_CODE and te.DEALER_CODE=org.DEALER_CODE WHERE 1=1");
		List<Object> queryParam = new ArrayList<Object>();
		sql.append("  and tu.USER_CODE  = ?");
		queryParam.add(userCode);
		sql.append(" and tdb.DEALER_CODE = ?");
        queryParam.add(dealerCode);
		List<Map> listMap=DAOUtil.findAll(sql.toString(), queryParam);
		
		//如果查询通过
		if(!CommonUtils.isNullOrEmpty(listMap)){
		    Map userInfo = listMap.get(0);
			  /*  String passwordMD5=(String) userInfo.get("PASSWORD");//密码
	        boolean validation=MD5Util.validPassword(password, passwordMD5);
	    	if(validation==false){
	    	    throw new ServiceBizException("密码不正确");
	    		}*/
		    LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
		   
	    	String employeeNo= (String) userInfo.get("EMPLOYEE_NO");//员工编号
	    	if(!StringUtils.isNullOrEmpty(employeeNo)){
	    	    loginInfo.setEmployeeNo(employeeNo);
			}
	    	String employeeName=(String) userInfo.get("EMPLOYEE_NAME");//员工名字
	    	if(!StringUtils.isNullOrEmpty(employeeName)){
	    	    loginInfo.setUserName(employeeName);
			}
	    	Integer gender=(Integer) userInfo.get("GENDER");//员工性别
	    	if(!StringUtils.isNullOrEmpty(gender)){
	    	    loginInfo.setGender(gender);
			}
	    	String dealerName=(String) userInfo.get("DEALER_NAME");//经销商名字
	    	if(!StringUtils.isNullOrEmpty(dealerName)){
	    	    loginInfo.setDealerName(dealerName);
			}
	    	String dealerShortName=(String) userInfo.get("DEALER_SHORTNAME");//经销商短名
	    	if(!StringUtils.isNullOrEmpty(dealerShortName)){
	    	    loginInfo.setDealerShortName(dealerShortName);
			}
	    	Object userId=userInfo.get("USER_ID");//用户id
	    	if(!StringUtils.isNullOrEmpty(userId)){
	    	    loginInfo.setUserId(Long.parseLong(userId.toString()));
			}
	    	Object dealerID=userInfo.get("DEALER_ID");//经销商id
	    	if(!StringUtils.isNullOrEmpty(dealerID)){
	    	    loginInfo.setDealerId(Long.parseLong(dealerID.toString()));
			}
	        // 组织orgCode
	    	String orgCode=(String) userInfo.get("ORG_CODE");//经销商id
            if (!StringUtils.isNullOrEmpty(orgCode)) {
                loginInfo.setOrgCode(orgCode);
            }
            // 组织orgName
            String orgName=(String) userInfo.get("ORG_NAME");//经销商id
            if (!StringUtils.isNullOrEmpty(orgName)) {
                loginInfo.setOrgName(orgName);
            }
            // 组织orgName
            Object organizationId=userInfo.get("organization_id");//经销商id
            if (!StringUtils.isNullOrEmpty(organizationId)) {
                loginInfo.setOrgId(Long.parseLong(organizationId.toString()));
            }
            //设置经销商信息及账号信息
            loginInfo.setDealerCode(dealerCode);
            loginInfo.setUserAccount(userCode);
                
             Long  userIdPower  =  Long.parseLong(userId.toString());
             
             Map<String,String> powerMap = getDataPower(userIdPower);
            //获取该用户整车仓库权限
            loginInfo.setCarLoadDepot(powerMap.get("carLoadDepot")); 
            //获取该用户配件仓库权限
            loginInfo.setPurchaseDepot(powerMap.get("purchaseDepot"));
            //获取该用户维修数据权限
            loginInfo.setRepair(powerMap.get("repair"));
            //获取该用户配件数据权限
            loginInfo.setPurchase(powerMap.get("purchase"));
            //获取优惠模式数据权限
            loginInfo.setPreferentialMode(powerMap.get("preferentialMode"));
            //获取该用户菜单和按钮权限
            loginInfo.setUserResouces(getActionUrl(userIdPower,dealerCode) );
        }else{
        	throw new ServiceBizException("账号不存在");
        }
		return information;
	}
	
	
	//获取该用户的数据权限
	@SuppressWarnings("unchecked")
	public Map<String,String> getDataPower(Long USER_ID){
		Map<String,String> powerString = new HashMap<String,String>();
		Map<String,List> reusltPower = new HashMap<String,List>();
		reusltPower.put("1319", new ArrayList());
		reusltPower.put("1320", new ArrayList());
		reusltPower.put("1211", new ArrayList());
		reusltPower.put("1313", new ArrayList());
		reusltPower.put("1400", new ArrayList());
		StringBuilder  powerSql = new  StringBuilder("SELECT  CTRL.CTRL_CODE  code , type  FROM  tm_user_ctrl CTRL WHERE CTRL.USER_ID = ");
		powerSql.append(USER_ID).append(" GROUP BY  CTRL.CTRL_CODE");
		List<Map> PowerList = Base.findAll(powerSql.toString());
		Iterator<Map> powerIterator = PowerList.iterator();
		while(powerIterator.hasNext()){
			
			Map power = powerIterator.next();
			String powerType =  power.get("type").toString();
			if("1319".equals(powerType)){
				reusltPower.get("1319").add(power.get("code"));
			}else if("1320".equals(powerType)){
				reusltPower.get("1320").add(power.get("code"));
			}else if("1211".equals(powerType)){
				reusltPower.get("1211").add(power.get("code"));
			}else if("1313".equals(powerType)){
				reusltPower.get("1313").add(power.get("code"));
			}else if("1400".equals(powerType)){
				reusltPower.get("1400").add(power.get("code"));
			}
		}
		powerString.put("carLoadDepot", handleDataPower(reusltPower.get("1319")));
		powerString.put("purchaseDepot", handleDataPower(reusltPower.get("1320")));
		powerString.put("repair", handleDataPower(reusltPower.get("1211")));
		powerString.put("purchase", handleDataPower(reusltPower.get("1313")));
		powerString.put("preferentialMode", handleDataPower(reusltPower.get("1400")));
		return powerString;
	}

	
	//处理权限格式
	public String  handleDataPower(List powerList){
		//1.获取整车仓库权限内容
		Iterator<Map> powerIterator = powerList.iterator();
		StringBuilder powerResult = new StringBuilder("(");
		if(powerList.size()==0){
		}else if(powerList.size()==1){
			powerResult.append("'").append(powerList.get(0)).append("'");
		}else{
			while(powerIterator.hasNext()){
			   powerResult.append("'").append(powerIterator.next()).append("',");
			}
		}
		powerResult.append("' ')");
		return powerResult.toString();
	}
	
	//获取菜单和按钮权限在后台拦截
	public String[]  getActionUrl(Long USER_ID ,String dealerCode){
		StringBuilder actionSql = new StringBuilder("SELECT ACTION_METHOD method , MODULE model ,ACTION_CODE code  FROM tc_menu_action  WHERE ACTION_CTL = 10041002 AND MENU_ID IN (");
		actionSql.append("SELECT MENU_ID  FROM  tm_user_menu WHERE  USER_ID =").append(USER_ID).append(" AND DEALER_CODE=").append(dealerCode).append("  GROUP BY MENU_ID )GROUP BY  ACTION_CODE ");
		actionSql.append("UNION SELECT ACTION_METHOD method ,  ACTION.MODULE model,ACTION.ACTION_CODE code FROM tm_user_menu MENU ,tc_menu_action ACTION ,tm_user_menu_action TUM  ");
		actionSql.append("WHERE MENU.USER_MENU_ID = TUM.USER_MENU_ID AND MENU.USER_ID=").append(USER_ID).append(" AND DEALER_CODE=").append(dealerCode);
		actionSql.append("  AND TUM.MENU_CURING_ID= ACTION.MENU_CURING_ID GROUP BY ACTION.ACTION_CODE ");
		List<Map> actionUrlList = Base.findAll(actionSql.toString());
		Iterator<Map> urlIterator = actionUrlList.iterator();
		String[] urlString = new String[actionUrlList.size()];
		int i = 0;  
		while(urlIterator.hasNext()){
			Map urlMap = urlIterator.next();
			String method = (String)urlMap.get("method");
			String model = (String) urlMap.get("model");
			String resultUrl = (String) urlMap.get("code");
		    resultUrl = resultUrl.replaceAll("\\{[^/]+\\}", ".+");
		    StringBuilder url = new StringBuilder(method);
		    url.append(":/").append("dms.web/").append(model).append("/rest").append(resultUrl);
		    urlString[i]=url.toString();
		    i++;
		}
		return urlString;
	}
	
}
