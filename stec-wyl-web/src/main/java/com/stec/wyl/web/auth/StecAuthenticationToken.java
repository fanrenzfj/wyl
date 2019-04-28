package com.stec.wyl.web.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

/**
 * Created by fakers on 2018/5/8 0008.
 */
public class StecAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private Object loginWay;

    public StecAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public StecAuthenticationToken(Object principal, Object credentials, Object loginWay) {
        super(principal, credentials);
        this.loginWay = loginWay;
    }
    public Object getLoginWay() {
        return loginWay;
    }
}
