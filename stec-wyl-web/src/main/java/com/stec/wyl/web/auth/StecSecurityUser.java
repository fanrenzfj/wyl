/**
 * 
 */
package com.stec.wyl.web.auth;

import com.stec.masterdata.entity.auth.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author Leonid
 *
 */
public class StecSecurityUser extends User {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2687485995613412371L;
	public SysUser loginUser;

    public String bindWechat;

	public StecSecurityUser(String username, String password, Collection<? extends GrantedAuthority> authorities, SysUser user) {
		super(username, password, authorities);
		this.setLoginUser(user);
	}

    public StecSecurityUser(String username, String password, String bindWechat, Collection<? extends GrantedAuthority> authorities, SysUser user) {
        super(username, password, authorities);
        this.setLoginUser(user);
        this.setBindWechat(bindWechat);
    }

	public StecSecurityUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
							Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
	}

	public SysUser getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(SysUser loginUser) {
		this.loginUser = loginUser;
	}


    public String getBindWechat() {
        return bindWechat;
    }

    public void setBindWechat(String bindWechat) {
        this.bindWechat = bindWechat;
    }
}
