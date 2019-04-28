package com.stec.wyl.web.auth;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

//@Configuration
public class SwaggerConfig extends WebMvcConfigurerAdapter {
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/null/swagger-resources/").addResourceLocations("/swagger-resources/");
	}

}
