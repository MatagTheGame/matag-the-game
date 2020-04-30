package com.matag.game.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ConfigService {
  @Value("${matag.admin.url}")
  private String matagAdminUrl;

  @Value("${matag.admin.password}")
  private String adminPassword;
}
