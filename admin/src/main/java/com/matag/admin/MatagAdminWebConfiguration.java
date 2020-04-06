package com.matag.admin;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MatagAdminWebConfiguration implements WebMvcConfigurer {
  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/").setViewName("redirect:/ui/admin");
    registry.addViewController("/ui").setViewName("redirect:/ui/admin");
    registry.addViewController("/ui/admin").setViewName("forward:/admin.html");
    registry.addViewController("/ui/admin/**").setViewName("forward:/admin.html");
  }
}
