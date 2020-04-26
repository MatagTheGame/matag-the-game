package com.matag.admin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Collections.singletonMap;

@Component
@Getter
public class ConfigService {
  @Value("${matag.game.url}")
  private String matagGameUrl;

  @Value("${matag.admin.password}")
  private String matagAdminPassword;

  public Map<String, String> getConfig() {
    return singletonMap("matagGameUrl", matagGameUrl);
  }
}
