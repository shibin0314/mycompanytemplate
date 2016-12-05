/*
* Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
*
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.manage
*
* @File name : TestController.java
*
* @Author : zhangxc
*
* @Date : 2016年6月29日
*
----------------------------------------------------------------------------------
*     Date       Who       Version     Comments
* 1. 2016年6月29日    zhangxc    1.0
*
*
*
*
----------------------------------------------------------------------------------
*/

package com.yonyou.dms.framework.filter;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.yonyou.dms.framework.domain.LoginInfoDto;
import com.yonyou.dms.framework.domain.RequestPageInfoDto;
import com.yonyou.dms.framework.service.LoginFrameworkService;
import com.yonyou.dms.framework.util.bean.AppliactionContextHelper;
import com.yonyou.dms.function.utils.common.StringUtils;
import com.yonyou.dms.function.utils.cookie.CookieUtil;

/**
 * @author zhangxianchao XSSFilter
 * @date 2016年2月23日
 */
public class LoginFilter implements Filter {
    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);
    
    /**
     * @author zhangxianchao
     * @date 2016年2月23日
     * @param filterConfig
     * @throws ServletException (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /*
     * @author zhangxianchao
     * @date 2016年2月23日
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
     * javax.servlet.FilterChain)
     */

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
                                                                                              ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        //获取请求的URL
        String path = httpServletRequest.getRequestURI();
        logger.debug("into LoginFilter:请求的地址："+path+";请求方法："+httpServletRequest.getMethod());
        if (path.indexOf("/web/rest/common/login")!=-1||path.indexOf("/manage/rest/basedata/dealers")!=-1) {
            chain.doFilter(request, response);
            return;
        }
        
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //判断是否登陆
        LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
        
        if(loginInfo==null || StringUtils.isNullOrEmpty(loginInfo.getUserAccount())){
            //httpServletResponse.sendRedirect(httpServletRequest.getContextPath()+"/html/logout.html");
            httpServletResponse.sendError(HttpStatus.UNAUTHORIZED.value(), "用户未登录");
            logger.debug("out LoginFilter without session");
            return;
        }
        String requestLocale = null;
        if((requestLocale = CookieUtil.getValueFromCookie(httpServletRequest,"language"))!=null){
            loginInfo.setLocale(new Locale(requestLocale));
        }
        
        //判断是否有功能权限
        String dmsFuncId = httpServletRequest.getParameter("dmsFuncId");
        if(!StringUtils.isNullOrEmpty(dmsFuncId)){
            LoginFrameworkService loginService = AppliactionContextHelper.getBeanByType(LoginFrameworkService.class);
            Long funcId = Long.parseLong(dmsFuncId);
            boolean isCanAccess = loginService.checkIsCanAccess(funcId, path,httpServletRequest.getMethod());
            if(!isCanAccess){
                httpServletResponse.sendError(HttpStatus.FORBIDDEN.value(), "用户没有此功能的权限");
                logger.info("用户没有此功能的权限:"+funcId+";url:"+path+";method"+httpServletRequest.getMethod());
                return;
            }
        }
        
        //判断是否判断分页信息
        if(StringUtils.isEqualsNoCasetive(httpServletRequest.getMethod(),"GET")){
            if(!StringUtils.isNullOrEmpty(httpServletRequest.getParameter("limit"))){
                RequestPageInfoDto requestPageInfoDto = AppliactionContextHelper.getBeanByType(RequestPageInfoDto.class);
                requestPageInfoDto.setLimit(httpServletRequest.getParameter("limit"));
                requestPageInfoDto.setOffset(httpServletRequest.getParameter("offset"));
                requestPageInfoDto.setSort(httpServletRequest.getParameter("sort"));
                requestPageInfoDto.setOrder(httpServletRequest.getParameter("order"));
            }
        }
        
        chain.doFilter(request, response);
        logger.debug("out LoginFilter sucess");
    }

    /*
     * @author zhangxianchao
     * @date 2016年2月23日 (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */

    @Override
    public void destroy() {

    }

}
