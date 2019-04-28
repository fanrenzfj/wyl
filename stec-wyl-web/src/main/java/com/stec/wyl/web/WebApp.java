package com.stec.wyl.web;

import com.stec.micro.boot.web.MicroRestApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created with IntelliJ IDEA.
 *
 * @author joe.xie
 * Date: 2018/7/24 0024
 * Time: 11:09
 */
@MicroRestApplication
@EnableSwagger2
public class WebApp {
    public static void main(String[] args) {
        SpringApplication.run(WebApp.class, args);
    }


    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }

    /**
     * 跨域过滤器
     *
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", buildConfig()); // 4
        return new CorsFilter(source);

    }
}
