package com.stec.wyl.web.controller;

import com.stec.framework.form.JsonRequestBody;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.wyl.web.service.SessionUtils;
import com.stec.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

/**
 * Created with IntelliJ IDEA.
 * User: Joe Xie
 * Date: 2015/5/26
 * Time: 16:37
 */
public abstract class BaseController {

    @Autowired
    private SessionUtils sessionUtils;

    protected <T> T getCache(String key, T obj, T defaultObj){
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        if(obj == null){
            Object cache = session.getAttribute(key);
            if(cache != null){
                return (T)cache;
            }
            else {
                obj = defaultObj;
                session.setAttribute(key, obj);
            }
        }
        else {
            session.setAttribute(key, obj);
        }
        return obj;
    }
    protected <T> T getCache(String key){
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        Object cache = session.getAttribute(key);
        if(cache != null){
            return (T) cache;
        }
        return null;
    }

    protected void clearCache(String key) {
        HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
        session.removeAttribute(key);
    }

    protected void logger(String remark, JsonRequestBody jsonRequestBody){
        String param = null;
        if(ObjectUtils.isNotNull(jsonRequestBody)) {
            param = jsonRequestBody.toJSONString();
        }
        logger(remark, param);
    }

    protected void logger(String remark, String param){
        sessionUtils.logger(remark, param);
    }

    protected void logger(String remark){
        sessionUtils.logger(remark, null);
    }

    protected SysUser currentSysUser() {
        return sessionUtils.getCurrentUser();
    }

    protected String currentSysUserName() {
        return currentSysUser().getName();
    }

    protected Long currentSysUserId() {
        return currentSysUser().getId();
    }
}
