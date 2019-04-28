package com.stec.wyl.web.auth;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

/**
 * Created by fakers on 2018/5/8 0008.
 */
public class StecAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    // ~ Static fields/initializers
    // =====================================================================================

    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    // ~ Instance fields
    // ================================================================================================

    private PasswordEncoder passwordEncoder;

    private String userNotFoundEncodedPassword;

    private SaltSource saltSource;

    private StecUserDetailService userDetailsService;

    public StecAuthenticationProvider() {
        setPasswordEncoder(new BCryptPasswordEncoder());
        // 隐藏
        this.hideUserNotFoundExceptions = false;
    }

    // ~ Methods
    // ========================================================================================================

    @Override
    @SuppressWarnings("deprecation")
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        Object loginWay = null;
        Object bindWechat = null;
        if (userDetails instanceof StecSecurityUser) {
            bindWechat = userDetails.getUsername();
        }
        if (authentication instanceof StecAuthenticationToken) {
            loginWay = ((StecAuthenticationToken) authentication).getLoginWay();
        }
        if ("wechatmini".equals(loginWay)) {
            if (!authentication.getPrincipal().equals(bindWechat)) {
                logger.debug("Authentication failed: no credentials provided");

                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "Bad credentials"));
            }
        } else {
            Object salt = null;
            if (this.saltSource != null) {
                salt = this.saltSource.getSalt(userDetails);
            }

            if (authentication.getCredentials() == null) {
                logger.debug("Authentication failed: no credentials provided");

                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "认证错误！"));
            }

            String presentedPassword = authentication.getCredentials().toString();

            if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
                logger.debug("Authentication failed: password does not match stored value");

                throw new BadCredentialsException(messages.getMessage(
                        "AbstractUserDetailsAuthenticationProvider.badCredentials",
                        "密码错误"));
            }
        }
    }
    @Override
    protected void doAfterPropertiesSet() throws Exception {
        Assert.notNull(this.userDetailsService, "A UserDetailsService must be set");
    }
    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
        UserDetails loadedUser;
        Object loginWay = null;
        if(authentication instanceof StecAuthenticationToken) {
            loginWay = ((StecAuthenticationToken) authentication).getLoginWay();
        }
        try {
            if("wechatmini".equals(loginWay)) {
                loadedUser = this.getUserDetailsService().loadUserByWechatmini(username);
            } else {
                loadedUser = this.getUserDetailsService().loadUserByUsername(username);
            }
        }
        catch (UsernameNotFoundException notFound) {
            if (authentication.getCredentials() != null) {
                String presentedPassword = authentication.getCredentials().toString();
                passwordEncoder.matches(presentedPassword, userNotFoundEncodedPassword);
            }
            throw notFound;
        }
        catch (Exception repositoryProblem) {
            throw new InternalAuthenticationServiceException(
                    repositoryProblem.getMessage(), repositoryProblem);
        }

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(
                    "UserDetailsService returned null, which is an interface contract violation");
        }
        return loadedUser;
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared as plain text.
     * <p>
     * For systems which are already using salted password which are encoded with a
     * previous release, the encoder should be of type
     * {@code org.springframework.security.authentication.encoding.PasswordEncoder}.
     * Otherwise, the recommended approach is to use
     * {@code org.springframework.security.crypto.password.PasswordEncoder}.
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     * types.
     */
    public void setPasswordEncoder(Object passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        if (passwordEncoder instanceof PasswordEncoder) {
            setPasswordEncoder((PasswordEncoder) passwordEncoder);
            return;
        }

        throw new IllegalArgumentException(
                "passwordEncoder must be a PasswordEncoder instance");
    }

    private void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");

        this.userNotFoundEncodedPassword = passwordEncoder.encode(
                USER_NOT_FOUND_PASSWORD);
        this.passwordEncoder = passwordEncoder;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * The source of salts to use when decoding passwords. <code>null</code> is a valid
     * value, meaning the <code>DaoAuthenticationProvider</code> will present
     * <code>null</code> to the relevant <code>PasswordEncoder</code>.
     * <p>
     * Instead, it is recommended that you use an encoder which uses a random salt and
     * combines it with the password field. This is the default approach taken in the
     * {@code org.springframework.security.crypto.password} package.
     *
     * @param saltSource to use when attempting to decode passwords via the
     * <code>PasswordEncoder</code>
     */
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    protected SaltSource getSaltSource() {
        return saltSource;
    }

    public void setUserDetailsService(StecUserDetailService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected StecUserDetailService getUserDetailsService() {
        return userDetailsService;
    }
}
