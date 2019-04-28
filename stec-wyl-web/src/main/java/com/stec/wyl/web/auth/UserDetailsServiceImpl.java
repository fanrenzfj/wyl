package com.stec.wyl.web.auth;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stec.framework.metadata.exceptions.ServiceException;
import com.stec.masterdata.entity.auth.SysUser;
import com.stec.masterdata.handler.auth.AuthHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements StecUserDetailService {
	
	private static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
	
	@Reference
	private AuthHandler authHandler;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		StecSecurityUser securityUser = null;
		SysUser user = null;
		try {
			//普通用户使用findUserByUsername 
			user = authHandler.selectEnabledUserByUsername(username);
			if(user == null) {
				throw new UsernameNotFoundException("用户不存在");
			}
			securityUser = new StecSecurityUser(username, user.getPassword(), "",getGrantedAuthorities(user), user);
			System.err.println(user.getPassword());
			logger.debug(MessageFormat.format("用户:{0} - 登陆成功", username));
		} catch (ServiceException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new UsernameNotFoundException(e.getMessage());
		}
		return securityUser;
	}

	@Override
    public UserDetails loadUserByWechatmini(String username) throws UsernameNotFoundException {
        StecSecurityUser securityUser = null;
		SysUser user = null;
        try {
            user = authHandler.selectUserByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException("用户不存在");
            }
            securityUser = new StecSecurityUser(username, user.getPassword(), "",getGrantedAuthorities(user), user);
            System.err.println(user.getPassword());
            logger.debug(MessageFormat.format("用户:{0} - 登陆成功", username));
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            throw new UsernameNotFoundException(e.getMessage());
        }
        return securityUser;
    }
	
	/**
	 * 获取用户的权限信息
	 * @param user
	 * @return
	 */
	private List<GrantedAuthority> getGrantedAuthorities(SysUser user){
        List<GrantedAuthority> authorities = new ArrayList<>();
//        for(Property property : user.getProperties()) {
//        	logger.debug(MessageFormat.format("用户:{0} - 角色:{1} - role:{2} ", user.getUsername(), property.getName(), property.getRole()));
//        	//"ROLE_"
//        	//拥有角色添加
//        	authorities.add(new SimpleGrantedAuthority("ROLE_" + property.getRole()));
//        	for(Authority authority : property.getAuthorities()) {
//        		authorities.add(new SimpleGrantedAuthority(authority.getName()));
//        	}
//        }
        return authorities;
    }

}
