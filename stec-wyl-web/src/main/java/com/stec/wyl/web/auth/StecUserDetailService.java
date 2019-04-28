package com.stec.wyl.web.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by fakers on 2018/5/8 0008.
 */
public interface StecUserDetailService {
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    public UserDetails loadUserByWechatmini(String username) throws UsernameNotFoundException;
}
