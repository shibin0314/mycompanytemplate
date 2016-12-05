
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.function
*
* @File name : ImportResultDto.java
*
* @Author : zhangxc
*
* @Date : 2016年8月18日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年8月18日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/
	
package com.yonyou.dms.function.domains.DTO;

import java.util.List;

/**
* 定义excel 导入反馈结果
* @author zhangxc
* @date 2016年8月18日
*/

public class ImportResultDto<T extends DataImportDto> {
    
    private boolean isSucess;
    private List<T> dataList;
    private List<T> errorList;
    
    public boolean isSucess() {
        return isSucess;
    }
    
    public void setSucess(boolean isSucess) {
        this.isSucess = isSucess;
    }
    
    public List<T> getDataList() {
        return dataList;
    }
    
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
    
    public List<T> getErrorList() {
        return errorList;
    }
    
    public void setErrorList(List<T> errorList) {
        this.errorList = errorList;
    }
}
