
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.common
*
* @File name : SystemParamServiceImpl.java
*
* @Author : zhanshiwei
*
* @Date : 2016年9月12日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年9月12日    zhanshiwei    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/
	
package com.yonyou.dms.framework.service.baseData.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yonyou.dms.framework.DAO.DAOUtil;
import com.yonyou.dms.framework.DAO.DefinedRowProcessor;
import com.yonyou.dms.framework.domains.DTO.baseData.BasicParametersDTO;
import com.yonyou.dms.framework.service.baseData.SystemParamService;
import com.yonyou.dms.function.exception.ServiceBizException;
import com.yonyou.dms.function.utils.common.StringUtils;

/**
* 系统参数
* @author zhanshiwei
* @date 2016年9月12日
*/
@Service
public class SystemParamServiceImpl implements SystemParamService{


    
    /**
     * 根据参数类型(PARAM_TYPE)查询参数信息
    * @author zhanshiwei
    * @date 2016年9月19日
    * @param type
    * @return
    * @throws ServiceBizException
    * (non-Javadoc)
    * @see com.yonyou.dms.common.service.basedata.SystemParamService#queryBasicParameterByType(java.lang.Long)
    */
    	
    @Override
    public List<BasicParametersDTO> queryBasicParameterByType(Long type) throws ServiceBizException {
        StringBuilder sb = new StringBuilder(
                "select PARAM_ID,DEALER_CODE,PARAM_CODE,PARAM_TYPE,PARAM_NAME,PARAM_VALUE,REMARK from tc_system_param where 1=1");
        List<String> params = new ArrayList<String>();
        if(!StringUtils.isNullOrEmpty(type)){
            sb.append(" and PARAM_TYPE = ?");
            params.add(type.toString());
        }
     
        final List<BasicParametersDTO>  listbasi=new ArrayList<BasicParametersDTO>();
        DAOUtil.findAll(sb.toString(), params,new DefinedRowProcessor(){             
                @Override
                protected void process(Map<String, Object> row) {
                        BasicParametersDTO basiDto=new BasicParametersDTO();
                        basiDto.setParamId(Integer.parseInt(row.get("param_id").toString()));
                        basiDto.setParamType(Integer.parseInt(row.get("param_type")!=null?row.get("param_type").toString():null));
                        basiDto.setParamCode(row.get("param_code")!=null?row.get("param_code").toString():null);
                        basiDto.setParamValue(row.get("param_value")!=null?row.get("param_value").toString():null);
                        listbasi.add(basiDto);
                  
                }
            });
        return listbasi;
    }

    
    /**
    * @author zhanshiwei
    * @date 2016年9月19日
    * @param type
    * @param code
    * @return
    * @throws ServiceBizException
    * (non-Javadoc)
    * @see com.yonyou.dms.common.service.basedata.SystemParamService#queryBasicParameterByTypeandCode(java.lang.Long, java.lang.String)
    */
    	
    @Override
    public BasicParametersDTO queryBasicParameterByTypeandCode(Long type, String code) throws ServiceBizException {
        StringBuilder sb = new StringBuilder(
                "select PARAM_ID,DEALER_CODE,PARAM_CODE,PARAM_TYPE,PARAM_NAME,PARAM_VALUE,REMARK from tc_system_param where 1=1");
        List<Object> params = new ArrayList<Object>();
        if(!StringUtils.isNullOrEmpty(type)){
            sb.append(" and PARAM_TYPE = ?");
            params.add(type);
        }
        if(!StringUtils.isNullOrEmpty(code)){
            sb.append(" and PARAM_CODE = ?");
            params.add(code);
        }
        final BasicParametersDTO basiDto=new BasicParametersDTO();
        DAOUtil.findAll(sb.toString(), params,new DefinedRowProcessor(){             
                @Override
                protected void process(Map<String, Object> row) {
                    basiDto.setParamId(Integer.parseInt(row.get("param_id").toString()));
                    basiDto.setParamType(Integer.parseInt(row.get("param_type")!=null?row.get("param_type").toString():null));
                    basiDto.setParamCode(row.get("param_code")!=null?row.get("param_code").toString():null);
                    basiDto.setParamValue(row.get("param_value")!=null?row.get("param_value").toString():null);
                }
            });
        return basiDto;
    }


  
}
