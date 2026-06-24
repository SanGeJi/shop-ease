package com.shop.config;

import com.shop.common.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/**", "/api/products", "/api/products/**", "/api/categories");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String base = System.getProperty("user.dir", ".");
        if (!base.endsWith("/") && !base.endsWith("\\")) base += "/";
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + base + "uploads/");
    }
}
