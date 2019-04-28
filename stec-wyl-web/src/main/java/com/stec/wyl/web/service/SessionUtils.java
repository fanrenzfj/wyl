package com.stec.wyl.web.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.framework.form.JsonRequestBody;
import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.wyl.web.auth.StecSecurityUser;
import com.stec.masterdata.entity.auth.SysLog;
import com.stec.masterdata.entity.auth.SysOrg;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.handler.auth.AuthHandler;
import com.stec.masterdata.handler.auth.SysLogHandler;
import com.stec.masterdata.handler.auth.SysOrgHandler;
import com.stec.masterdata.handler.auth.SysUserHandler;
import com.stec.utils.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Leonid
 *
 */
@Component
public class SessionUtils {

	@Reference
	private AuthHandler authHandler;

	@Reference
	private SysUserHandler sysUserHandler;
	
	@Reference
	private SysOrgHandler sysOrgHandler;

	@Reference
	private SysLogHandler sysLogHandler;

	// 给出当前登录账号
	public SysUser getCurrentUser() throws ServiceException {
		System.err.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		StecSecurityUser userdetail = (StecSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userdetail.getLoginUser();
	}
	
	public SysUser getCurrentUser(int i) throws ServiceException {
		Object object = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (object instanceof org.springframework.security.core.userdetails.User) {
			org.springframework.security.core.userdetails.User userdetail = (org.springframework.security.core.userdetails.User) object;
			return authHandler.selectUserByUsername(userdetail.getUsername());
		} else {
			throw new ServiceException("您没有登录");
		}
	}
	
	//支持两种方式登录请求
	public SysUser getCurrentUser(JsonRequestBody body) throws ServiceException {
		Long id = body.getLong("id");
		if(id!=null){
			return sysUserHandler.selectByPrimaryKey(id);
		}
		StecSecurityUser userdetail = (StecSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userdetail.getLoginUser();
	}
	
	// 给出当前和数据库同步的账号信息
	public SysUser getSyncCurrentUser() throws ServiceException {
		StecSecurityUser userdetail = null;
		userdetail = (StecSecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return sysUserHandler.selectByPrimaryKey(userdetail.getLoginUser().getId());
	}

	// 给出当前企业
	public SysOrg getCurrentOrg() throws ServiceException {
		SysUser user = getCurrentUser();
		SysOrg org = null;
		if (user != null) {
			org = sysOrgHandler.selectByPrimaryKey(user.getOrgId());
			return org;
		} else {
			return null;
		}
	}

	public String requestToken(){
		return requestHeader("x-auth-token");
	}

	public String requestHeader(String header){
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request.getHeader(header);
	}

	public void logger(String remark, String param) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String path = request.getRequestURI();
		String ip = request.getHeader("X-Real-IP");
		if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Forwarded-For");
			if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
				// 多次反向代理后会有多个IP值，第一个为真实IP。
				int index = ip.indexOf(',');
				if (index != -1) {
					ip = ip.substring(0, index);
				}
			} else {
				ip = request.getRemoteAddr();
			}
		}

		SysUser loginUser = getCurrentUser();
		if(loginUser == null) {
			loginUser = new SysUser();
		}
		SysLog log = new SysLog(loginUser.getName(), loginUser.getUsername(), loginUser.getId(), path, ip, remark, param, requestHeader("x-request-client"));
		sysLogHandler.save(log);
	}
//
//	// 给出当前有权限的区域
//	public List<ProjectStructure> getCurrentStruc() throws ServiceException {
//		User user = getCurrentUser();
//		if (user != null) {
//			List<ProjectStructure> listo = postFunctionHandler.getStrucs(user.getId(),null);
//			return listo;
//		} else {
//			return null;
//		}
//	}
//
//	public Long getUserInfo() {
//		Long followSourceId = null;
//		try {
//			User user = getSyncCurrentUser();
//			if(user.getIsManager() != 1){
//				followSourceId = user.getFollowSourceId();
//				if(followSourceId < 1)
//					return null;
//			}
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//		return followSourceId;
//	}
//
//	public User getSysUser() {
//		User user = new User();
//		Long followSourceId = null;
//		try {
//			user = getSyncCurrentUser();
//			if(user.getIsManager() == 2){
//				followSourceId = user.getFollowSourceId();
//				if(followSourceId < 1 || followSourceId == null)
//					followSourceId = 0L;
//				user.setFollowSourceId(followSourceId);
//			}
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//		return user;
//	}
//
//	public Location getOrgLocation() {
//		Location location = new Location();
//		Org org = new Org();
//		Long followSourceId = null;
//		try {
//			User user = getSyncCurrentUser();
//			if(user.getIsManager() != 1){
//				followSourceId = user.getFollowSourceId();
//				org = orgAPIHandler.getOrgById(followSourceId);
//				if(org != null){
//					location.setId(org.getLocationId());
//				}
//				if(followSourceId < 1)
//					return null;
//			}
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//		return location;
//	}
//
//	public Location getProjectLocation(Long projectId) {
//		Location location = new Location();
//		Project project = new Project();
//		try {
//			//TODO
////			project = null;//orgAPIHandler.getOrgById(projectId);
//			if(project != null){
//				location.setId(project.getLocationId());
//			}
//			if(projectId < 1)
//				return null;
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//		return location;
//	}
//
//	//获取所有设备集合
//	public Map<String, Object> getSitesByUser(Long projectId) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		try {
//			User user = getSyncCurrentUser();
//			List<Long> list1 =new LinkedList<Long>();
//			if(projectId <1 || projectId == null){
//				list1.add(0L);
//			}else{
//				if(user.getIsManager() > 2){
//					List<ProjectStructure> listo = postFunctionHandler.getProjectStrucsByPost(projectId,user.getId());
//					for(ProjectStructure p : listo){
//						if(p != null && p.getId() != null)
//							list1.add(p.getId());
//					}
//				}else if(user.getIsManager() == 2 || user.getIsManager() == 1){
////					List<ProjectStructure> listo = postFunctionHandler.getProjectStrucs(projectId);
////					if(listo != null && listo.size() >0){
////						for(ProjectStructure p : listo){
////							if(p != null && p.getId() != null)
////								list1.add(p.getId());
////						}
////					}
//				}
//			}
//			if(list1.size() < 1)
//				list1 = null;
//			params.put("siteIds", list1);
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//		return params;
//	}
//
//	//获得所有的项目集合
//	public Map<String, Object> getProjectsByUser() {
//		HashMap<String, Object> params = new HashMap<String, Object>();
//		try {
//			User user = getSyncCurrentUser();
//			List<Long> list1 =new LinkedList<Long>();
//			if(user.getIsManager() > 2){
//				List<ProjectStructure> listo = postFunctionHandler.getStrucs(user.getId(),null);
//				for(ProjectStructure p : listo){
//					if(p != null && p.getProjectId() != null)
//						list1.add(p.getProjectId());
//				}
//				params.put("projectIds", list1);
//			}else if(user.getIsManager() == 2){
//				List<Project> listo = postFunctionHandler.getProjects(user.getFollowSourceId());
//				for(Project p : listo){
//					if(p != null && p.getId() != null)
//						list1.add(p.getId());
//				}
//				params.put("projectIds", list1);
//			}else if(user.getIsManager() == 1){
//
//			}
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
//		return params;
//	}
}
