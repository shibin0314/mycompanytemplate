
/** 
 *Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
 * This software is published under the terms of the Yonyou Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * @Project Name : dms.manage
 *
 * @File name : EmployeeServiceImpl.java
 *
 * @Author : jcsi
 *
 * @Date : 2016年7月8日
 *
----------------------------------------------------------------------------------
 *     Date       Who       Version     Comments
 * 1. 2016年7月8日    Administrator    1.0
 *
 *
 *
 *
----------------------------------------------------------------------------------
 */

package com.yonyou.dms.manage.service.basedata.employee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.yonyou.dms.framework.DAO.DAOUtil;
import com.yonyou.dms.framework.DAO.PageInfoDto;
import com.yonyou.dms.framework.domains.DTO.baseData.BasicParametersDTO;
import com.yonyou.dms.framework.util.FrameworkUtil;
import com.yonyou.dms.function.common.DictCodeConstants;
import com.yonyou.dms.function.exception.ServiceBizException;
import com.yonyou.dms.function.utils.common.StringUtils;
import com.yonyou.dms.manage.domains.DTO.basedata.EmployeeDto;
import com.yonyou.dms.manage.domains.PO.basedata.EmployeePo;
import com.yonyou.dms.manage.domains.PO.basedata.EmployeeRolePo;


/**
 * 员工信息  实现类
 * @author jcsi
 * @date 2016年7月8日
 */

@Service
public class EmployeeServiceImpl implements EmployeeService {



	/**
	 * 根据条件查询员工信息
	 * @author jcsi
	 * @date 2016年7月29日
	 * @param param
	 * @return
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#searchEmployees(java.util.Map)
	 */

	public PageInfoDto searchEmployees(@RequestParam Map<String, String> param)throws ServiceBizException{
		StringBuilder sb=new StringBuilder("select tme.EMPLOYEE_ID,tme.DEALER_CODE,tme.EMPLOYEE_NO,tme.EMPLOYEE_NAME,tme.GENDER,tme.CERTIFICATE_ID,tme.ORG_CODE,tmdo.ORG_NAME,tme.MOBILE,tme.IS_ONJOB from tm_employee tme LEFT JOIN tm_dealer_organization tmdo on tme.ORG_CODE=tmdo.ORG_CODE and tmdo.DEALER_CODE=tme.DEALER_CODE where 1=1 ");
		List<Object> queryParam=new ArrayList<Object>();
		if(!StringUtils.isNullOrEmpty(param.get("employeeName"))){
			sb.append(" and EMPLOYEE_NAME like ?");
			queryParam.add("%"+param.get("employeeName")+"%");
		}
		if(!StringUtils.isNullOrEmpty(param.get("employeeNo"))){
			sb.append(" and EMPLOYEE_NO like ?");
			queryParam.add("%"+param.get("employeeNo")+"%");
		}
		if(!StringUtils.isNullOrEmpty(param.get("isOnjob"))){
			sb.append(" and IS_ONJOB = ?");
			queryParam.add(Integer.parseInt(param.get("isOnjob")+""));
		}
		if(!StringUtils.isNullOrEmpty(param.get("orgName"))){
		    sb.append(" and ORG_NAME like ? ");
		    queryParam.add("%"+param.get("orgName")+"%");
		}
		return DAOUtil.pageQuery(sb.toString(), queryParam);

	}

	/**
	 * 新增员工信息
	 * @author jcsi
	 * @date 2016年7月29日
	 * @param empDto
	 * @return
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#addEmployee(com.yonyou.dms.manage.domains.DTO.basedata.EmployeeDto)
	 */

	@Override
	public Long addEmployee(EmployeeDto empDto)throws ServiceBizException {
		EmployeePo emp=new EmployeePo();
		EmployeeRolePo erPo=null;
		String employeeNo=FrameworkUtil.getLoginInfo().getDealerCode()+empDto.getEmployeeNo();  //员工编号
		//查询数据库中是否已经存在此员工编号  如果不存在保存  否则抛出异常
		boolean isTrue=SearchByEmployeeNo(employeeNo);
		boolean mobileIsTrue=SearchByMobile(empDto.getMobile(),null);
		boolean CertificateIdIsTrue=SearchByCertificateId(empDto.getCertificateId(),null);
		if(CertificateIdIsTrue==false){
		    throw new ServiceBizException("身份证维护不规范");
		}
		if(isTrue){
		    if(mobileIsTrue){
		        emp.setString("EMPLOYEE_NO", employeeNo);
	            setEmployee(emp,empDto);
	            emp.saveIt();
	            //保存用户角色
	            List<String> list=empDto.getEmployeeRoles();
	            for(int i=0;i<list.size();i++){
	                erPo=new EmployeeRolePo();
	                erPo.setString("ROLE",list.get(i));
	                emp.add(erPo);
	            }

	            return emp.getLongId();
		    }else{
		        throw new ServiceBizException("该手机号已存在");
		    }			
		}else{
			throw new ServiceBizException("该员工编号已经存在");
		}

	}
	/**
	 * 查询数据库中是否已经存在此员工编号  
	 * @author jcsi
	 * @date 2016年8月1日
	 * @param employeeNo
	 * @return 
	 * @throws ServiceBizException
	 */
	public boolean SearchByEmployeeNo(String employeeNo)throws ServiceBizException{
		StringBuilder sb=new StringBuilder("SELECT t.EMPLOYEE_ID,t.DEALER_CODE from tm_employee t where t.EMPLOYEE_NO=?");
		List<Object> param=new ArrayList<Object>();
		param.add(employeeNo);
		List<Map> map=DAOUtil.findAll(sb.toString(), param);
		if(map.size()==0){
			return true;
		}
		return false;
	}

	/**
     * 查询数据库中是否存在手机号（同一个经销商 、在职员工）
     * @author jcsi
     * @date 2016年8月1日
     * @param employeeNo
     * @return 
     * @throws ServiceBizException
     */
    public boolean SearchByMobile(String Mobile,Long id)throws ServiceBizException{
        List<Object> param=new ArrayList<Object>();
        StringBuilder sb=new StringBuilder("SELECT t.EMPLOYEE_ID,t.DEALER_CODE from tm_employee t where t.MOBILE=? and t.DEALER_CODE=? and IS_ONJOB=? ");
        param.add(Mobile);
        param.add(FrameworkUtil.getLoginInfo().getDealerCode());
        param.add(DictCodeConstants.EMPLOYEE_ISJOB);
        //修改
        if(!StringUtils.isNullOrEmpty(id)){
            sb.append(" and EMPLOYEE_ID !=? ");
            param.add(id);
        }
        List<Map> map=DAOUtil.findAll(sb.toString(), param);
        if(map.size()==0){
            return true;
        }
        return false;
    }

    /**
    * 查询身份证号码是否重复
    * @author jcsi
    * @date 2016年10月17日
    * @param Mobile
    * @param id
    * @return
    * @throws ServiceBizException
     */
    public boolean SearchByCertificateId(String CertificateId,Long id)throws ServiceBizException{
        List<Object> param=new ArrayList<Object>();
        StringBuilder sb=new StringBuilder("SELECT t.EMPLOYEE_ID,t.CERTIFICATE_ID,t.DEALER_CODE from tm_employee t where t.CERTIFICATE_ID=? and t.DEALER_CODE=? ");
        param.add(CertificateId);
        param.add(FrameworkUtil.getLoginInfo().getDealerCode());
        //修改
        if(!StringUtils.isNullOrEmpty(id)){
            sb.append(" and EMPLOYEE_ID !=? ");
            param.add(id);
        }
        List<Map> map=DAOUtil.findAll(sb.toString(), param);
        if(map.size()==0){
            return true;
        }
        return false;
    }

	/**
	 * 根据不同的查询语句查询
	 * @author jcsi
	 * @date 2016年7月11日
	 * @param str
	 * @return
	 */
	public List<Map> FindAll(String str)throws ServiceBizException{
		List<Map> map=DAOUtil.findAll(str.toString(), null);
		return map;
	}

	/**
	 * 根据id查找员工信息
	 * @author jcsi
	 * @date 2016年7月29日
	 * @param id
	 * @return
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#findById(java.lang.Long)
	 */

	@Override
	public Map findById(Long id)throws ServiceBizException{
	    StringBuilder sb=new StringBuilder();
	    sb.append("SELECT tme.EMPLOYEE_ID,tme.DEALER_CODE,tme.EMPLOYEE_NO,tme.EMPLOYEE_NAME,tme.GENDER,tme.WORKGROUP_CODE,");
	    sb.append("tme.CERTIFICATE_ID,tme.ORG_CODE,tmdo.ORG_NAME,tme.PHONE,tme.IS_ONJOB,POSITION_CODE,MOBILE,");
	    sb.append("E_MAIL,BIRTHDAY,ADDRESS,ZIP_CODE,WORKER_TYPE_CODE,TECHNICIAN_GRADE,DEFAULT_POSITION,FOUND_DATE,DIMISSION_DATE   ");
	    sb.append("FROM tm_employee tme ");
	    sb.append("LEFT JOIN tm_dealer_organization tmdo ON tme.ORG_CODE = tmdo.ORG_CODE and tmdo.DEALER_CODE=tme.DEALER_CODE  ");
	    sb.append(" WHERE tme.EMPLOYEE_ID=?");
	    List<Object> param=new ArrayList<Object>();
	    param.add(id);
	    return DAOUtil.findFirst(sb.toString(), param);
	}

	/**
	 * 根据id更新员工信息
	 * @author jcsi
	 * @date 2016年7月29日
	 * @param id
	 * @param empDto
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#updateEmpById(java.lang.Long, com.yonyou.dms.manage.domains.DTO.basedata.EmployeeDto)
	 */

	@Override
	public void updateEmpById(Long id, EmployeeDto empDto)throws ServiceBizException {
		EmployeePo empo=EmployeePo.findById(id);
		boolean mobileIsTrue=SearchByMobile(empDto.getMobile(),id);
		boolean certificatedIdIsTrue=SearchByCertificateId(empDto.getCertificateId(),id);
		if(certificatedIdIsTrue==false){
		    throw new ServiceBizException("身份证维护不规范");
		}
		if(mobileIsTrue){
		    setEmployee(empo,empDto);
	        //保存员工信息
	        empo.saveIt();
	        //删除之前保存的员工全部角色信息
	        List<String> roles=empDto.getEmployeeRoles();
	        EmployeeRolePo.delete("EMPLOYEE_ID=?", id);
	        EmployeeRolePo erPo=null;
	        //保存修改之后的员工角色信息
	        if(roles.size()>0){
	            for(int i=0;i<roles.size();i++){
	                erPo=new EmployeeRolePo();
	                erPo.setString("EMPLOYEE_ID", id);
	                erPo.setString("ROLE",roles.get(i));
	                erPo.saveIt();
	            }
	        }
		}else{
		    throw new ServiceBizException("该手机号已存在");
		}
		


	}

	/**
	 * 根据id删除员工信息
	 * @author jcsi
	 * @date 2016年7月29日
	 * @param id
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#deleteById(java.lang.Long)
	 */

	@Override
	public void deleteById(Long id) throws ServiceBizException{
		EmployeePo empo= EmployeePo.findById(id);        
		empo.deleteCascadeShallow();  
	}






	/**
	 * 给EmployeePo属性赋值 
	 * @author jcsi
	 * @date 2016年7月29日
	 * @param emp
	 * @param empDto
	 */

	public void setEmployee(EmployeePo emp,EmployeeDto empDto)throws ServiceBizException{       
		emp.setString("EMPLOYEE_NAME",empDto.getEmployeeName());
		emp.setString("ORG_CODE", empDto.getOrgCode());
		emp.setString("POSITION_CODE",empDto.getPositionCode());
		emp.setLong("GENDER", empDto.getGender());
		emp.setString("CERTIFICATE_ID",empDto.getCertificateId());
		emp.setString("PHONE", empDto.getPhone());
		emp.setString("MOBILE", empDto.getMobile());
		emp.setString("E_MAIL", empDto.geteMail());
		emp.setDate("BIRTHDAY", empDto.getBirthday());
		emp.setString("ADDRESS", empDto.getAddress());
		emp.setString("ZIP_CODE", empDto.getZipCode());        
		emp.setString("WORKER_TYPE_CODE", empDto.getWorkerTypeCode());
		emp.setLong("TECHNICIAN_GRADE", empDto.getTechnicianGrade());
		emp.setString("DEFAULT_POSITION", empDto.getDefaultPosition());
		emp.setLong("IS_ONJOB", empDto.getIsOnjob());
		emp.setString("WORKGROUP_CODE", empDto.getWorkgroupCode());
		//建档时间 ，如果不填 系统默认为当前时间
		if(empDto.getFoundDate()!=null){
			emp.setDate("FOUND_DATE", empDto.getFoundDate());
		}else{
			emp.setDate("FOUND_DATE", new Date()); 
		}
		
		//如果在职状态为“离职”并且离职日期为空，则默认离职时间为当前时间
		if((DictCodeConstants.EMPLOYEE_NOJOB+"").equals(empDto.getIsOnjob()+"")&&empDto.getDimissionDate()==null){
			emp.setDate("DIMISSION_DATE", new Date());
		}else{
			emp.setDate("DIMISSION_DATE", empDto.getDimissionDate());
		}

	}

	/**
	 * 根据员工id查找员工角色信息
	 * @author jcsi
	 * @date 2016年7月29日
	 * @param id
	 * @return
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#findEmpRolesByEmpId(java.lang.Long)
	 */

	@Override
	public List<Map> findEmpRolesByEmpId(Long id)throws ServiceBizException {
		StringBuilder sb=new StringBuilder("select r.DEALER_CODE,r.ROLE from tm_employee_role r where r.EMPLOYEE_ID=?");
		List<String> param=new ArrayList<String>();
		param.add(Long.toString(id));
		return DAOUtil.findAll(sb.toString(),param);
	}
	/**
	 * 员工信息下拉框调用的方法
	 * @author yll
	 * @date 2016年7月20日
	 * @param queryParam
	 * @return
	 * @throws ServiceBizException
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#selectEmployees(java.util.Map)
	 */
	@Override
	public List<Map> selectEmployees(Map<String, String> queryParam) throws ServiceBizException {
		StringBuilder sqlSb = new StringBuilder("select em.EMPLOYEE_NO,em.EMPLOYEE_NAME,em.DEALER_CODE,em.RECORD_VERSION,  org.ORG_CODE,  org.ORG_NAME   from tm_employee em inner JOIN tm_dealer_organization org on em.ORG_CODE=org.ORG_CODE and   em.DEALER_CODE=org.DEALER_CODE  where 1=1 ");
		sqlSb.append(" and em.IS_ONJOB="+DictCodeConstants.EMPLOYEE_ISJOB);
		List<String> params = new ArrayList<String>();
		if(!StringUtils.isNullOrEmpty(queryParam.get("ROLE"))){
			sqlSb.append(" and   EXISTS ( select 1 from TM_EMPLOYEE_ROLE er where ROLE in (?) and em.EMPLOYEE_ID=er.EMPLOYEE_ID)");
			params.add(queryParam.get("ROLE"));
		}
		if(!StringUtils.isNullOrEmpty(queryParam.get("orgCode"))){
		    sqlSb.append(" and em.ORG_CODE=?");
		    params.add(queryParam.get("orgCode"));
		}
		
		return DAOUtil.findAll(sqlSb.toString(),params);
	}


	

	/**
	 * 查询当前用户信息
	* @author yll
	* @date 2016年8月30日
	* @param param
	* @return
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#searchUserEmployees(java.util.Map)
	 */
	public PageInfoDto searchUserEmployees(@RequestParam Map<String, String> param)throws ServiceBizException{
		StringBuilder sb=new StringBuilder("SELECT te.EMPLOYEE_ID AS EMPLOYEE_ID,group_concat(tc.CODE_CN_DESC) as EMP_ROLE,te.DEALER_CODE,te.EMPLOYEE_NO,te.EMPLOYEE_NAME,tu.USER_CODE,tu.USER_STATUS,tu.PASSWORD,te.ORG_CODE,te.POSITION_CODE, ");
		sb.append("te.GENDER,te.CERTIFICATE_ID,te.PHONE,te.MOBILE,te.E_MAIL,te.BIRTHDAY,te.ADDRESS,te.ZIP_CODE,te.WORKER_TYPE_CODE,te.TECHNICIAN_GRADE,te.DEFAULT_POSITION,te.IS_ONJOB,");
		sb.append("te.FOUND_DATE,te.DIMISSION_DATE from tm_employee te LEFT JOIN tm_employee_role ter on te.EMPLOYEE_ID=ter.EMPLOYEE_ID LEFT JOIN tc_code tc on tc.CODE_ID=ter.ROLE LEFT JOIN tm_user tu ON tu.EMPLOYEE_ID=te.EMPLOYEE_ID where 1=1");
		List<Object> queryParam=new ArrayList<Object>();

		if(!StringUtils.isNullOrEmpty(param.get("employeeNo"))){
			sb.append(" and te.EMPLOYEE_NO like ?");
			queryParam.add("%"+param.get("employeeNo")+"%");
		}
		if(!StringUtils.isNullOrEmpty(param.get("userCode"))){
			sb.append(" and tu.USER_CODE like ?");
			queryParam.add("%"+param.get("userCode")+"%");
		}
		if(!StringUtils.isNullOrEmpty(param.get("employeeName"))){
			sb.append(" and te.EMPLOYEE_NAME like ?");
			queryParam.add("%"+param.get("employeeName")+"%");
		}
		
		if(!StringUtils.isNullOrEmpty(param.get("workingState"))){
			sb.append(" and te.IS_ONJOB = ?");
			queryParam.add(Integer.parseInt(param.get("workingState")));
		}  
		if(!StringUtils.isNullOrEmpty(param.get("userState"))){
			sb.append(" and tu.USER_STATUS = ?");
			queryParam.add(Integer.parseInt(param.get("userState")));
		} 
		
		//group by用法？ 加入后有bug
		sb.append(" GROUP BY EMPLOYEE_ID");
		return DAOUtil.pageQuery(sb.toString(), queryParam);
	}

	/**
	 * 根据EmployeeId查找员工对应的账号信息
	 * @author yll
	 * @date 2016年8月22日
	 * @param id
	 * @return
	 * @throws ServiceBizException
	 * (non-Javadoc)
	 * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#findbyEmployeeId(java.lang.Long)
	 */
	@Override
	public Map findUserByEmployeeId(Long id) throws ServiceBizException {
		StringBuilder sb=new StringBuilder("select USER_ID ,DEALER_CODE,USER_CODE,USER_STATUS,PASSWORD,LOGIN_LAST_TIME from tm_user where 1=1 ");
		List<Object> queryParam=new ArrayList<Object>();
		if(!StringUtils.isNullOrEmpty(id)){
			sb.append(" and EMPLOYEE_ID = ?");
			queryParam.add(id);
		} 
		List<Map> list= DAOUtil.findAll(sb.toString(), queryParam);
		Map map=null;
		if(list.size()>0){
			map=list.get(0);
		}
		return map;
	}
	
	/**
	 * 整车销售订单-销售顾问下拉数据
	*  @author xukl
	* @date 2016年9月7日
	* @param orgCode
	* @return
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#qrySalesConsultant(java.lang.String)
	*/
		
	@Override
	public List<Map> qrySalesConsultant(String orgCode) throws ServiceBizException {
	    StringBuilder sqlSb = new StringBuilder("select t.EMPLOYEE_NO,t.EMPLOYEE_NAME,t.DEALER_CODE,t.ORG_CODE from TM_EMPLOYEE  t LEFT JOIN TM_EMPLOYEE_ROLE tt on t.EMPLOYEE_ID = tt.EMPLOYEE_ID where 1=1 and tt.ROLE = ?");
        List<Object> params = new ArrayList<Object>();
        params.add(DictCodeConstants.SALES_CONSULTANT);
        if(!orgCode.equals("-1")){
            sqlSb.append(" and t.ORG_CODE = ?");
            params.add(orgCode);
        }
        List<Map> list = DAOUtil.findAll(sqlSb.toString(), params);
        return list;
	}
	
	/**
	* 查询销售经理和财务经理
	* @author xukl
	* @date 2016年9月22日
	* @param basiDtolist
	* @return
	* @throws ServiceBizException
	*/
		
	@Override
    public PageInfoDto qryAudit( List<BasicParametersDTO> basiDtolist) throws ServiceBizException {
	    String saleaudit = "";//是否需要经理审核
        for (BasicParametersDTO basicParametersDTO : basiDtolist) {
            if(basicParametersDTO.getParamCode().equals("vehicle_sale_audit")){
                saleaudit=basicParametersDTO.getParamValue();
            }
        }
        StringBuilder sqlSb = new StringBuilder("select t.EMPLOYEE_ID,t.EMPLOYEE_NO,t.EMPLOYEE_NAME,t.DEALER_CODE,tt.ROLE from TM_EMPLOYEE  t LEFT JOIN TM_EMPLOYEE_ROLE tt on t.EMPLOYEE_ID = tt.EMPLOYEE_ID where tt.ROLE = ?");
        List<Object> params = new ArrayList<Object>();
        if(!StringUtils.isNullOrEmpty(saleaudit)){
            params.add(DictCodeConstants.SALES_AUDIT);
        }else{
            params.add(DictCodeConstants.FINANCE_AUDIT);
        }
        PageInfoDto list = DAOUtil.pageQuery(sqlSb.toString(), params);
        return list;
    }
	
	/**
	 * 查询财务经理
	*  @author xukl
	* @date 2016年9月28日
	* @return
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#qryFinanceAudit()
	*/
		
	@Override
    public List<Map> qryFinanceAudit() throws ServiceBizException {
        StringBuilder sqlSb = new StringBuilder("select t.EMPLOYEE_ID,t.EMPLOYEE_NO,t.EMPLOYEE_NAME,t.DEALER_CODE,tc.CODE_CN_DESC as ROLE from TM_EMPLOYEE  t LEFT JOIN TM_EMPLOYEE_ROLE tt on t.EMPLOYEE_ID = tt.EMPLOYEE_ID LEFT JOIN tc_code tc on tc.CODE_ID=tt.ROLE where tt.ROLE = ?");
        List<Object> params = new ArrayList<Object>();
            params.add(DictCodeConstants.FINANCE_AUDIT);
            List<Map> list = DAOUtil.findAll(sqlSb.toString(), params);
        return list;
    }
	/**
	 * 查询技师下拉框
	* @author rongzoujie
	* @date 2016年9月27日
	* @return
	* @throws ServiceBizException
	* (non-Javadoc)
	* @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#queryTechnician()
	 */
    @Override
    public List<Map> queryTechnician() throws ServiceBizException {
        StringBuilder sqlSb = new StringBuilder("SELECT t1.DEALER_CODE,t1.employee_name,t1.employee_no FROM tm_employee t1 left JOIN tm_employee_role t2 ON (t2.employee_id = t1.employee_id )");
        sqlSb.append(" where t2.role = 10061004 and t1.is_onjob = 10081001");
        List<Object> params = new ArrayList<Object>();
        return DAOUtil.findAll(sqlSb.toString(),params);
       
    }
    
    /**
     * 查询检验人表格下拉框 
    * @author rongzoujie
    * @date 2016年10月11日
    * @return
    * @throws ServiceBizException
     */
    public List<Map> queryFinisher() throws ServiceBizException {
        StringBuilder sqlSb = new StringBuilder("SELECT t1.DEALER_CODE,t1.employee_name,t1.employee_no FROM tm_employee t1 left JOIN tm_employee_role t2 ON (t2.employee_id = t1.employee_id )");
        sqlSb.append(" where t2.role = 10061006 and t1.is_onjob = 10081001");
        List<Object> params = new ArrayList<Object>();
        return DAOUtil.findAll(sqlSb.toString(),params);
    }

    /**
     * 服务顾问下拉框
    * @author rongzoujie
    * @date 2016年9月27日
    * @return
    * @throws ServiceBizException
    * (non-Javadoc)
    * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#queryServiceAss()
     */
    @Override
    public List<Map> queryServiceAss() throws ServiceBizException {
        StringBuilder sql = new StringBuilder("SELECT t1.employee_name,t1.dealer_code,t1.employee_no FROM tm_employee t1 LEFT JOIN tm_employee_role t2 ON t1.employee_id = t2.employee_id WHERE t2.role = 10061003");
        sql.append(" and t1.IS_ONJOB=?  ");
        List<Object> params = new ArrayList<Object>();
        params.add(DictCodeConstants.EMPLOYEE_ISJOB);
        return DAOUtil.findAll(sql.toString(), params);
    }

    /**
     *只修改员工角色
    * @author yll
    * @date 2016年10月27日
    * @param id
    * @param employeeRoles
    * @throws ServiceBizException
    * (non-Javadoc)
    * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#updateEmpById(java.lang.Long, java.util.List)
     */
	@Override
	public void updateEmpById(Long id, List<String> employeeRoles) throws ServiceBizException {
		EmployeePo empo=EmployeePo.findById(id);
	        //保存员工信息
	        empo.saveIt();
	        //删除之前保存的员工全部角色信息
	        List<String> roles=employeeRoles;
	        EmployeeRolePo.delete("EMPLOYEE_ID=?", id);
	        EmployeeRolePo erPo=null;
	        //保存修改之后的员工角色信息
	        if(roles.size()>0){
	            for(int i=0;i<roles.size();i++){
	                erPo=new EmployeeRolePo();
	                erPo.setString("EMPLOYEE_ID", id);
	                erPo.setString("ROLE",roles.get(i));
	                erPo.saveIt();
	            }
	        }		
	}

    
    /**
     * 职位过滤员工
    * @author zhanshiwei
    * @date 2016年11月1日
    * @param role
    * @return
    * @throws ServiceBizException
    * (non-Javadoc)
    * @see com.yonyou.dms.manage.service.basedata.employee.EmployeeService#selectEmployeeByrole(java.lang.Integer)
    */
    	
    @Override
    public List<Map> selectEmployeeByrole(Integer role) throws ServiceBizException {
        StringBuilder sqlSb = new StringBuilder("select em.EMPLOYEE_NO,em.EMPLOYEE_NAME,em.DEALER_CODE,em.RECORD_VERSION,  org.ORG_CODE,  org.ORG_NAME   from tm_employee em inner JOIN tm_dealer_organization org on em.ORG_CODE=org.ORG_CODE and   em.DEALER_CODE=org.DEALER_CODE  where 1=1 ");
        sqlSb.append(" and em.IS_ONJOB="+DictCodeConstants.EMPLOYEE_ISJOB);
        List<Object> params = new ArrayList<Object>();
        if(!StringUtils.isNullOrEmpty(role)){
            sqlSb.append(" and   EXISTS ( select 1 from TM_EMPLOYEE_ROLE er where ROLE in (?) and em.EMPLOYEE_ID=er.EMPLOYEE_ID)");
            params.add(role);
        }
        return DAOUtil.findAll(sqlSb.toString(),params);
    }
}
