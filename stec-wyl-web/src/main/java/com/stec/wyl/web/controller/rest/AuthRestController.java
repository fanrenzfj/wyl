package com.stec.wyl.web.controller.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.masterdata.entity.auth.SysOrg;
import com.stec.wyl.web.controller.BaseController;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.wyl.web.service.SessionUtils;
import com.stec.masterdata.entity.auth.SysPrivilege;
import com.stec.masterdata.entity.auth.SysRole;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.handler.auth.AuthHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Joe Xie
 * Date: 2018/6/14 0014
 * Time: 11:22
 */
@Api(value = "认证controller", tags = {"认证相关接口"})
@RestController
@RequestMapping(value = "/rest/auth", method = RequestMethod.POST)
public class AuthRestController extends BaseController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Reference
    private AuthHandler authHandler;

    @Autowired
    private SessionUtils sessionUtils;

    @ApiOperation("获取登录用户的详细信息")
    @RequestMapping("/getUserInfo")
    public ResultForm getUserInfo(HttpSession session) {
//        logger("获取登录用户详细信息");
        System.err.println(session.getId());
        ResultForm<?> result = null;
        Map<String, Object> map = new HashMap<>();
        SysUser user = null;
        try {
            try {
                user = sessionUtils.getSyncCurrentUser();
                List<SysRole> roles =  authHandler.selectUserRoles(user.getId());
                List<SysPrivilege> privileges = authHandler.selectUserPrivileges(user.getId());
                SysOrg sysOrg =sessionUtils.getCurrentOrg();
                map.put("user", user);
                map.put("roles", roles);
                map.put("privileges", privileges);
                map.put("org",sysOrg);
                return ResultForm.createSuccessResultForm(map, "success！");

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (user == null) {
                map.put("isLogin", 0);
                result = ResultForm.createSuccessResultForm(map, "您还有没有登录！");
                result.setStatus(1001);
                return result;
            }
            result = ResultForm.createSuccessResultForm(map, "查询成功");
        } catch (ServiceException e) {
            e.printStackTrace();
            result = ResultForm.createErrorResultForm(null, e.getMessage());
        }
        return result;
    }

    @ApiOperation("用户登出")
    @RequestMapping("/logout")
    public Object logout(HttpServletRequest request, HttpServletResponse response) {
        logger("用户登出");
        ResultForm<?> result = null;
        try {
            request.logout();
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null) {
                new SecurityContextLogoutHandler().logout(request, response, auth);
            }
            result = ResultForm.createSuccessResultForm(null, "注销成功");
        } catch (ServletException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            result = ResultForm.createErrorResultForm(null, "注销失败");
        }
        return result;
    }
}
