package com.matag.game.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ConfigService {
  @Value("${matag.admin.path}")
  private String matagAdminPath;

  @Value("${matag.admin.internal.url}")
  private String matagAdminInternalUrl;

  @Value("${matag.admin.password}")
  private String adminPassword;
}
