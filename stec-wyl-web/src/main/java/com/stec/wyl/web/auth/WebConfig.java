package com.stec.wyl.web.auth;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 
 * 支持跨域请求
 * 
 * @author wesley
 
 */
//@Configuration
public class WebConfig {
//	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/login");
				registry.addMapping("/rest/**");
				registry.addMapping("/manage/**");
			}
		};
	}
}