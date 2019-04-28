package com.stec.wyl.web.auth;

import com.alibaba.fastjson.JSON;
import com.stec.framework.metadata.bean.ResultForm;
import com.stec.wyl.web.service.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private StecUserDetailService userDetailsService;

    @Autowired
    private SessionUtils sessionUtils;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/assets/**", "/app/**", "/webjars/**", "/v2/**", "/swagger**/**");
    }

    class TdAuthenticationFailureHandler implements AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                            AuthenticationException exception) throws IOException, ServletException {
            ResultForm<?> result = ResultForm.createErrorResultForm(null, exception.getMessage());
            String res = JSON.toJSONString(result);
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "x-auth-token,x-request-client,Content-Type");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(res);
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    class StecAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                            Authentication authentication) throws IOException, ServletException {

            sessionUtils.logger("用户登录", null);
            ResultForm<?> result = ResultForm.createSuccessResultForm(request.getSession().getId(), "登录成功");
            String res = JSON.toJSONString(result);
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "x-auth-token,x-request-client,Content-Type");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(res);
            response.getWriter().flush();
            response.getWriter().close();
        }
    }

    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return this.passwordEncoder = new BCryptPasswordEncoder();
    }


    @Bean
    public StecUsernamePasswordAuthenticationFilter stecUsernamePasswordAuthenticationFilter() {
        StecUsernamePasswordAuthenticationFilter filter = new StecUsernamePasswordAuthenticationFilter();
        try {
            filter.setAuthenticationManager(this.authenticationManagerBean());
            filter.setAuthenticationFailureHandler(new TdAuthenticationFailureHandler());
            filter.setAuthenticationSuccessHandler(new StecAuthenticationSuccessHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }

    @Bean
    public StecAuthenticationProvider stecAuthenticationProvider() {
        StecAuthenticationProvider provider = new StecAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ProviderManager providerManager = (ProviderManager) this.authenticationManager();
        for (AuthenticationProvider provider : providerManager.getProviders()) {
            if (provider instanceof DaoAuthenticationProvider) {
                ((DaoAuthenticationProvider) provider).setHideUserNotFoundExceptions(false);
            }
        }
        http.csrf().disable()
                .addFilterBefore(stecUsernamePasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/login").permitAll()
                .antMatchers("/rest/historicalDefect/**").permitAll()
                .antMatchers("/rest/emergencyEvent/**").permitAll()
                .antMatchers("/rest/workOrder/**").permitAll()
                .antMatchers("/rest/data/**").permitAll()
                .antMatchers("/rest/doc/**").permitAll()
                .antMatchers("/rest/road/**").permitAll()
                .antMatchers("/rest/defect/**").permitAll()
                .antMatchers("/rest/historicalDefect/**").permitAll()
                .antMatchers("/rest/evaluateReport/**").permitAll()
                .antMatchers("/rest/evaluateReportItem/**").permitAll()
                .antMatchers("/rest/traction/**").permitAll()
                .anyRequest().authenticated()
                .and().formLogin().permitAll().and().exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    ResultForm<?> result = ResultForm.createErrorResultForm(null, "您没有权限");
                    result.setStatus(1002);
                    String res = JSON.toJSONString(result);
                    response.setHeader("Content-type", "text/html;charset=UTF-8");
                    response.setHeader("Access-Control-Allow-Origin", "*");
                    response.setHeader("Access-Control-Allow-Headers", "x-auth-token,x-request-client,Content-Type");
                    response.setCharacterEncoding("utf-8");
                    response.getWriter().write(res);
                    response.getWriter().flush();
                    response.getWriter().close();
                }).authenticationEntryPoint((request, response, authException) -> {
            ResultForm<?> result = ResultForm.createErrorResultForm(null, "您没有登录");
            result.setStatus(1001);
            String res = JSON.toJSONString(result);
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "x-auth-token,x-request-client,Content-Type");
            response.setCharacterEncoding("utf-8");
            response.getWriter().write(res);
            response.getWriter().flush();
            response.getWriter().close();
        }).and();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(stecAuthenticationProvider());
    }

}
