package com.dk.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.rmi.registry.Registry;
import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Value("#{'${cors.allowed-methods}'.split(',')}")
  private List<String> allowedMethods;

  @Value("#{'${cors.allowed-origins}'.split(',')}")
  private List<String> allowedOrigins;

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    CorsRegistration corsRegistration = registry.addMapping("/api/**");
    allowedMethods.forEach(corsRegistration::allowedMethods);
    allowedOrigins.forEach(corsRegistration::allowedOrigins);
  }

}
