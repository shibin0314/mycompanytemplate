
/** 
 *Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
 * This software is published under the terms of the Yonyou Software
 * License version 1.0, a copy of which has been included with this
 * distribution in the LICENSE.txt file.
 *
 * @Project Name : dms.manage
 *
 * @File name : CommonTreeDto.java
 *
 * @Author : yll
 *
 * @Date : 2016年8月7日
 *
----------------------------------------------------------------------------------
 *     Date       Who       Version     Comments
 * 1. 2016年8月7日    yll    1.0
 *
 *
 *
 *
----------------------------------------------------------------------------------
 */

package com.yonyou.dms.manage.domains.DTO.basedata;


/**
 * 树加载一般模型
 * @author yll
 * @date 2016年8月7日
 */

public class CommonTreeDto {
	private String id;
	private String parent;
	private String text;
	private CommonTreeStateDto state;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public CommonTreeStateDto getState() {
		return state;
	}
	public void setState(CommonTreeStateDto state) {
		this.state = state;
	}



}
