
/** 
 *Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
 * This software is published under the terms of the Yonyou Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * @Project Name : dms.manage
 *
 * @File name : RoleServiceImpl.java
 *
 * @Author : yll
 *
 * @Date : 2016年7月28日
 *
----------------------------------------------------------------------------------
 *     Date       Who       Version     Comments
 * 1. 2016年7月28日    yll    1.0
 *
 *
 *
 *
----------------------------------------------------------------------------------
 */

package com.yonyou.dms.manage.service.basedata.role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yonyou.dms.framework.DAO.DAOUtil;
import com.yonyou.dms.framework.DAO.PageInfoDto;
import com.yonyou.dms.function.exception.ServiceBizException;
import com.yonyou.dms.function.utils.common.StringUtils;
import com.yonyou.dms.manage.controller.basedata.RoleManageController;
import com.yonyou.dms.manage.domains.DTO.basedata.role.RoleDto;
import com.yonyou.dms.manage.domains.PO.basedata.role.RolePO;

/**
 * 角色信息实现类
 * @author yll
 * @date 2016年7月28日
 */
@Service
public class RoleServiceImpl implements RoleService{

	// 定义日志接口
	private static final Logger logger = LoggerFactory.getLogger(RoleManageController.class);
	String str="";

	/**
	 * 角色查询方法
	 * @author yll
	 * @date 2016年8月7日
	 * @param queryParam
	 * @return
	 * @throws ServiceBizException
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.role.RoleService#queryRole(java.util.Map)
	 */
	@Override
	public PageInfoDto queryRole(Map<String, String> queryParam) throws ServiceBizException {
		List<Object> params = new ArrayList<Object>();
		String sql = getQuerySql(queryParam,params);
		PageInfoDto pageInfoDto = DAOUtil.pageQuery(sql,params);
		return pageInfoDto;
	}

	/**
	 * 根据id获取角色信息
	 * @author yll
	 * @date 2016年8月7日
	 * @param id
	 * @return
	 * @throws ServiceBizException
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.role.RoleService#getRoleById(java.lang.Long)
	 */
	@Override
	public RolePO getRoleById(Long id) throws ServiceBizException {

		return RolePO.findById(id);
	}

	/**
	 * 角色添加方法
	 * @author yll
	 * @date 2016年8月7日
	 * @param roleDto
	 * @return
	 * @throws ServiceBizException
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.role.RoleService#addRole(com.yonyou.dms.manage.domains.DTO.basedata.role.RoleDto)
	 */
	@Override
	public Long addRole(RoleDto roleDto) throws ServiceBizException {
		String roleCode=roleDto.getRoleCode();
		String roleName=roleDto.getRoleName();	
		if(StringUtils.isNullOrEmpty(roleDto.getRoleCode())){
			throw new ServiceBizException("角色代码");
		}
		if(StringUtils.isNullOrEmpty(roleDto.getRoleName())){
			throw new ServiceBizException("角色名称");
		}
		if(searchBrandCode(roleCode,roleName)){
			RolePO rolePO=new RolePO();
			setRolePO(rolePO,roleDto);
			rolePO.saveIt();
			return  rolePO.getLongId();
		}else{
			throw new ServiceBizException("角色代码或名称不能重复");
		}
		
		
	}

	/**
	 * 
	 * 取得sql语句
	 * @author yll
	 * @date 2016年8月12日
	 * @param queryParam
	 * @param params
	 * @return
	 */
	private String getQuerySql(Map<String,String> queryParam,List<Object> params){
		StringBuilder sqlSb = new StringBuilder("select ROLE_ID,DEALER_CODE,ROLE_CODE,ROLE_NAME  from TM_ROLE where 1=1 ");

		if(!StringUtils.isNullOrEmpty(queryParam.get("roleCode"))){
			sqlSb.append(" and ROLE_CODE=?");
			params.add(queryParam.get("roleCode"));
		}
		if(!StringUtils.isNullOrEmpty(queryParam.get("roleName"))){
			sqlSb.append(" and ROLE_NAME like ?");
			params.add("%"+queryParam.get("roleName")+"%");
		} 
		return sqlSb.toString();
	}
	/**
	 * 
	 * 设置checkbox选中状态的方法
	 * @author yll
	 * @date 2016年8月7日
	 * @param role
	 * @param roleDto
	 */
	public void setRolePO(RolePO role,RoleDto roleDto){

		role.setString("ROLE_CODE", roleDto.getRoleCode());
		role.setString("ROLE_NAME", roleDto.getRoleName());

	}

//取得树的json格式，已停用
	@Override
	public String getJson(Integer parentId) {


		List<Map> lists=queryByParentId(parentId);
		for (int i = 0; i < lists.size(); i++)  
		{  
			Map a = lists.get(i);
			List<Map> lists2=queryByParentId((Integer) a.get("MENU_ID"));
			// List<Map> lists2=queryByParentId((Integer) a.get("PARENT_ID"));


			str += "{attributes:{id:\"" + a.get("MENU_ID")  
			+ "\"},state:\"open\",text:\"" + a.get("MENU_NAME") + "\" ,";  
			str += "children:[";  


			//还有子节点(递归调用)  
			if (queryByParentId((Integer) a.get("MENU_ID")).size()>0)  
			{  
				//  this.getJson((Integer) ac.get("PARENT_ID"));
				this.getJson((Integer) a.get("MENU_ID")); 
			}  


			str += "]";  
			str += "   }"; 
			if (i < lists.size() - 1)  
			{  
				str += ",";  
			}  



		}
		return str ;

	}

	/**
	 * 
	 * 根据parentid菜单树节点查询
	 * @author yll
	 * @date 2016年8月7日
	 * @param parentId
	 * @return
	 */
	public List<Map> queryByParentId(Integer parentId){
		StringBuilder sqlSb = new StringBuilder("select MENU_ID,MENU_NAME,MENU_URL,MENU_ICON,PARENT_ID,MENU_TYPE from tc_menu where 1=1 ");
		List<Object> params = new ArrayList<Object>();
		if (!StringUtils.isNullOrEmpty(Long.toString(parentId))) {
			sqlSb.append(" and PARENT_ID = ?");
			params.add(parentId);
		}

		List<Map> list =  Base.findAll(sqlSb.toString(), params.toArray());

		return list;

	}

	/**
	 * 菜单树节点查询
	 * @author yll
	 * @date 2016年8月7日
	 * @return
	 * @throws ServiceBizException
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.role.RoleService#queryMenu()
	 */
	@Override
	public List<Map> queryMenu() throws ServiceBizException {
		StringBuilder sqlSb = new StringBuilder("select MENU_ID,MENU_NAME,MENU_URL,MENU_ICON,PARENT_ID,MENU_TYPE from tc_menu where 1=1 ");
		List<String> params = new ArrayList<String>();
		List<Map> list =  Base.findAll(sqlSb.toString(), params.toArray());
		return list;
	}

	/**
	 * 删除角色信息及对应的权限
	* @author yll
	* @date 2016年10月19日
	* @param id
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.role.RoleService#deleteRoleById(java.lang.Long)
	 */
	@Override
	public void deleteRoleById(Long id) throws ServiceBizException {
		RolePO role=RolePO.findById(id);
		role.delete();
		
	}


	/**
	 * 
	* 判断是否存在已有的角色代码和名称
	* @author yll
	* @date 2016年10月19日
	* @param repairTypeName
	* @return
	 */
	 private boolean searchBrandCode(String roleCode,String roleName) {
		 StringBuilder sqlSb = new StringBuilder("select ROLE_ID,DEALER_CODE from tm_role where 1=1");
			List<Object> params = new ArrayList<Object>();
			sqlSb.append(" and ROLE_CODE = ?");
			params.add(roleCode);
			sqlSb.append(" or ROLE_NAME = ?");
			params.add(roleName);
			List<Map> map = DAOUtil.findAll(sqlSb.toString(),params);
	        if(map.size()==0){
	            return true;
	        }
	        return false;
	    }

}
