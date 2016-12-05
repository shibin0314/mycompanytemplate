
/** 
*Copyright 2016 Yonyou Corporation Ltd. All Rights Reserved.
* This software is published under the terms of the Yonyou Software
* License version 1.0, a copy of which has been included with this
* distribution in the LICENSE.txt file.
*
* @Project Name : dms.web
*
* @File name : LoginController.java
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

package com.yonyou.dms.web.controller.login;

import java.sql.Connection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.javalite.activejdbc.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yonyou.dms.framework.DAO.DAOUtil;
import com.yonyou.dms.framework.domain.FrameworkParamBean;
import com.yonyou.dms.framework.domain.LoginInfoDto;
import com.yonyou.dms.framework.service.TenantDealerMappingService;
import com.yonyou.dms.framework.util.bean.AppliactionContextHelper;
import com.yonyou.dms.web.domains.DTO.login.MenuDto;
import com.yonyou.dms.web.service.login.LoginUserService;
import com.yonyou.dms.web.service.login.MenuService;
import com.yonyou.f4.common.acl.AclUser;
import com.yonyou.f4.common.database.DBService;
import com.yonyou.f4.mvc.annotation.TxnConn;
import com.yonyou.f4.mvc.controller.BaseController;

/**
 * 此Controller 主要实现登录、退出、修改密码等功能
 * 
 * @author zhangxc
 * @date 2016年6月30日
 */
@Controller
@RequestMapping("/common/login")
public class LoginController extends BaseController {

    @Autowired
    FrameworkParamBean          frameworkParam;

    @Autowired
    private MenuService         menuService;

    @Autowired
    DBService                   dbService;
    
    @Autowired
    private TenantDealerMappingService tenantDealerSerivce;

    @Autowired
    LoginUserService            loginUserService;

    // 定义日志接口
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    /**
     * 根据查询条件返回对应的用户数据
     * 
     * @author zhangxc
     * @date 2016年6月29日
     * @param queryParam 查询条件
     * @return 查询结果
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public void login(@RequestParam("username") String userName, @RequestParam("password") String password,
                      @RequestParam("dealerCode") String dealerCode, HttpServletRequest request) throws Exception {
        try {
            request.getSession().removeAttribute(frameworkParam.getTenantKey());
            request.getSession().removeAttribute("loginInfoDto");
            
            //获取对应的TENANT_ID
            Map<String,Map<String,String>> tenantDealerMaping = tenantDealerSerivce.getAll();
            String tenantId = tenantDealerMaping.get(dealerCode).get("TENANT_ID");
            dbService.beginTxn(tenantId);
            Connection conn = dbService.openConn(tenantId);
            Base.attach(conn);
            // 校验账号
            loginUserService.logCheck(dealerCode, userName, password);
            
            /**
             * 当前登录信息
             */
            LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
            //加载tenantID
            request.getSession().setAttribute(frameworkParam.getTenantKey(), tenantId);
            
            //构建AclUser
            AclUser aclUser = new AclUser();
            aclUser.setUID(loginInfo.getUserId().toString());
            aclUser.setName(loginInfo.getUserAccount());
            request.getSession().setAttribute(frameworkParam.getAclUserKey(), aclUser);
            logger.info("userName:" + userName + ";dealerCode:" + dealerCode + ";登陆成功");
        } catch (Exception e) {
            throw e;
        } finally {
            dbService.endTxn(true);
            Base.detach();
            dbService.clean();
        }

    }

    /**
     * 根据查询条件返回对应的用户数据
     * 
     * @author zhangxc
     * @date 2016年6月29日
     * @param queryParam 查询条件
     * @return 查询结果
     */
    @RequestMapping(method = RequestMethod.DELETE)
    @ResponseBody
    public void loginOut(HttpServletRequest request, HttpServletResponse response) {
        Enumeration<String> sessions = request.getSession().getAttributeNames();
        while (sessions.hasMoreElements()) {
            logger.info("sessions:" + sessions.nextElement());
        }
        // LoginInfoDto loginInfo = AppliactionContextHelper.getBeanByType(LoginInfoDto.class);
        // if(loginInfo!=null){
        // loginInfo.setUserAccount(null);
        // loginInfo.setDealerCode(null);
        // logger.info("userName:"+loginInfo.getUserAccount()+";dealerCode:"+loginInfo.getDealerCode()+";退出成功");
        // }
        request.getSession().removeAttribute(frameworkParam.getTenantKey());
        request.getSession().removeAttribute(frameworkParam.getAclUserKey());
        request.getSession().removeAttribute("loginInfoDto");
    }

    /**
     * 加载用户的菜单
     * 
     * @author zhangxc
     * @date 2016年10月11日
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}/menus/{language}", method = RequestMethod.GET)
    @ResponseBody
    @TxnConn
    public Map<String, MenuDto> getMenus(@PathVariable("userId") long userId) {

        List<Map> menus = menuService.queryMenu();
        Map<String, MenuDto> childMap = new HashMap<String, MenuDto>();
        Map<String, MenuDto> menuMap = new HashMap<String, MenuDto>();

        for (int i = 0; i < menus.size(); i++) {
            String type = menus.get(i).get("MENU_TYPE").toString();
            MenuDto menuDto = new MenuDto();
            menuDto.setMenuId((String) menus.get(i).get("MENU_ID").toString());	

            menuDto.setMenuName(DAOUtil.getLocaleFieldValue(menus.get(i), "MENU_NAME"));

            if (menus.get(i).get("MENU_ICON") != null) {
                menuDto.setMenuIcon((String) menus.get(i).get("MENU_ICON").toString());
            }
            String url = "system/pageError.html";
            if (menus.get(i).get("MENU_URL") != null) {
                url = (String) menus.get(i).get("MENU_URL").toString();
            }
            menuDto.setMenuUrl(url);
            String parentId = (String) menus.get(i).get("PARENT_ID").toString();
            menuDto.setParentId(parentId);
            menuDto.setMenuType(type);

            if ("1001".equals(type)) {
                menuDto.setChildren(new HashMap<String, MenuDto>());
                menuMap.put(menuDto.getMenuId(), menuDto);
            }
            if ("1002".equals(type)) {
                MenuDto parentMenu = menuMap.get(parentId);
                menuDto.setChildren(new HashMap<String, MenuDto>());
                childMap.put(menuDto.getMenuId(), menuDto);
                if (parentMenu != null) {
                    parentMenu.getChildren().put(menuDto.getMenuId(), menuDto);
                }
            }
            if ("1003".equals(type)) {
                MenuDto parentMenu = childMap.get(parentId);
                if (parentMenu != null) {
                    parentMenu.getChildren().put(menuDto.getMenuId(), menuDto);
                }
            }
        }
        return menuMap;
    }
    
    
    /**
     * 加载用户的操作功能
     * 
     * @author zhangxc
     * @date 2016年11月11日
     * @param userId
     * @return
     */
    @RequestMapping(value = "/{userId}/handles/{language}", method = RequestMethod.GET)
    @ResponseBody
    @TxnConn
     public List<Map> getHandles(@PathVariable("userId") long userId){
    	List<Map> handles =  menuService.queryHandles();
    	return handles;
    }
}
