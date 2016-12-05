
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.manage
*
* @File name : UserServiceImpl.java
*
* @Author : yll
*
* @Date : 2016年8月15日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年8月15日    yll    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/
	
package com.yonyou.dms.manage.service.basedata.employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.springframework.stereotype.Service;

import com.yonyou.dms.framework.DAO.DAOUtil;
import com.yonyou.dms.function.exception.ServiceBizException;
import com.yonyou.dms.function.utils.common.StringUtils;
import com.yonyou.dms.manage.domains.DTO.basedata.user.UserDto;
import com.yonyou.dms.manage.domains.PO.basedata.user.UserPO;
import com.yonyou.dms.manage.service.basedata.user.UserService;

/**
* 用户接口的实现类
* @author yll
* @date 2016年8月15日
*/
@Service
public class EmployeeUserServiceImpl implements UserService{

	/**
	 * 修改用户信息
	* @author yll
	* @date 2016年8月30日
	* @param id
	* @param userDto
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.user.UserService#modifyUser(java.lang.Long, com.yonyou.dms.manage.domains.DTO.basedata.user.UserDto)
	 */
	@Override
	public void modifyUser(Long id, UserDto userDto) throws ServiceBizException {
		UserPO userPO=UserPO.findById(id);
		setUser(userPO,userDto);
		userPO.saveIt();
		
	}

	/**
	 * 添加用户
	* @author yll
	* @date 2016年8月30日
	* @param userDto
	* @return
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.user.UserService#addUser(com.yonyou.dms.manage.domains.DTO.basedata.user.UserDto)
	 */
	@Override
	public Long addUser(UserDto userDto) throws ServiceBizException {
		String userCode=userDto.getUserCode();
		if(StringUtils.isNullOrEmpty(userDto.getEmployeeId())){
			throw new ServiceBizException("员工id不能为空");
		}
		if(StringUtils.isNullOrEmpty(userDto.getUserCode())){
			throw new ServiceBizException("用户账号不能为空");
		}
		if(StringUtils.isNullOrEmpty(userDto.getUserStatus())){
			throw new ServiceBizException("用户状态");
		}
		if(StringUtils.isNullOrEmpty(userDto.getPassword())){
			throw new ServiceBizException("用户密码不能为空");
		}
		
		if(SearchUserCode(userCode)){
			UserPO userPO=new UserPO();
			setUser(userPO,userDto);
			userPO.saveIt();
			return  userPO.getLongId();
		}else{
			throw new ServiceBizException("用户名已存在");
		}
		
		
	}

	/**
	 * 加载树状菜单的基础数据
	* @author yll
	* @date 2016年8月30日
	* @return
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.user.UserService#queryMenu()
	 */
	@Override
	public List<Map> queryMenu() throws ServiceBizException {
		StringBuilder sqlSb = new StringBuilder("select MENU_ID,MENU_NAME,MENU_URL,MENU_ICON,PARENT_ID,MENU_TYPE from tc_menu where 1=1 ");
		List<String> params = new ArrayList<String>();
        List<Map> list =  Base.findAll(sqlSb.toString(), params.toArray());
		return list;
	}

	/**
	 * 
	* 设置用户
	* @author yll
	* @date 2016年8月30日
	* @param userPO
	* @param userDto
	 */
	private void setUser(UserPO userPO,UserDto userDto){
		userPO.setString("USER_CODE",userDto.getUserCode());
		if(!StringUtils.isNullOrEmpty(userDto.getPassword())){
			userPO.setString("PASSWORD",userDto.getPassword());
		}
		userPO.setInteger("EMPLOYEE_ID", userDto.getEmployeeId());
		userPO.setInteger("USER_STATUS", userDto.getUserStatus());
		//userPO.setString("USER_NAME", userDto.getUserName());
	}

	/**
	 * 根据id获取用户
	* @author yll
	* @date 2016年8月30日
	* @param id
	* @return
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.user.UserService#getUserById(java.lang.Long)
	 */
	@Override
	public UserPO getUserById(Long id) throws ServiceBizException {
		return UserPO.findById(id);
	}

	/**
	 * 根据员工id获取用户id
	* @author yll
	* @date 2016年8月30日
	* @param id
	* @return
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.user.UserService#getUserIDByEmployeeId(java.lang.Long)
	 */
	@Override
	public Long getUserIDByEmployeeId(Long id) throws ServiceBizException {
		Long userId=null;
		 StringBuilder sqlSb = new StringBuilder("select DEALER_CODE,USER_ID  from tm_user  where 1=1 and EMPLOYEE_ID = ?");
	        List<Object> queryParams = new ArrayList<Object>();
	        queryParams.add(id);
	        List<Map> list=DAOUtil.findAll(sqlSb.toString(), queryParams);
	        if(list.size()>0){
	        	
	        	userId=Long.valueOf( ""+ list.get(0).get("USER_ID"));
	        }
	       if(userId==null){
	    	   return null;
	       }else{
	    	   return userId;
	       }
		
	}

	/**
	 * 根据用户code查找员工id
	* @author yll
	* @date 2016年8月30日
	* @param userCode
	* @return
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.user.UserService#getEmployeeIdByUserCode(java.lang.String)
	 */
	@Override
	public Long getEmployeeIdByUserCode(String userCode) throws ServiceBizException {
		Long employeeId=null;
		 StringBuilder sqlSb = new StringBuilder("select DEALER_CODE,EMPLOYEE_ID  from tm_user  where 1=1 and USER_CODE = ?");
	        List<Object> queryParams = new ArrayList<Object>();
	        queryParams.add(userCode);
	        List<Map> list=DAOUtil.findAll(sqlSb.toString(), queryParams);
	        if(list.size()>0){
	        	
	        	employeeId=Long.valueOf( ""+ list.get(0).get("EMPLOYEE_ID"));
	        }
	       if(employeeId==null){
	    	   return null;
	       }else{
	    	   return employeeId;
	       }
	}

	@Override
	public String getPasswordByUserCode(String userCode) throws ServiceBizException {
		String  psaaword=null;
		 StringBuilder sqlSb = new StringBuilder("select DEALER_CODE,PASSWORD  from tm_user  where 1=1 and USER_CODE = ?");
	        List<Object> queryParams = new ArrayList<Object>();
	        queryParams.add(userCode);
	        List<Map> list=DAOUtil.findAll(sqlSb.toString(), queryParams);
	        if(list.size()>0){
	        	
	        	psaaword=(String) list.get(0).get("PASSWORD");
	        }
	       if(psaaword==null){
	    	   return null;
	       }else{
	    	   return psaaword;
	       }
	}

	/**
	 * 
	* 查找用户名是否存在
	* @author yll
	* @date 2016年10月27日
	* @param brandCode
	* @param brandName
	* @return
	 */
	private boolean SearchUserCode(String userCode) {
		StringBuilder sqlSb = new StringBuilder("select USER_ID,DEALER_CODE from tm_user where 1=1");
		List<Object> params = new ArrayList<Object>();
		sqlSb.append(" and USER_CODE = ?");
		params.add(userCode);
		List<Map> map = DAOUtil.findAll(sqlSb.toString(),params);
		if(map.size()==0){
			return true;
		}
		return false;
	}

}
