package com.matag.game.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ConfigService {
  @Value("${matag.admin.url}")
  private String matagAdminUrl;

  @Value("${matag.admin.password}")
  private String adminPassword;
}
