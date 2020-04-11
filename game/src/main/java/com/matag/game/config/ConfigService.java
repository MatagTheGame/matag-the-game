package com.matag.game.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

import static java.util.Collections.singletonMap;

@Component
@Getter
public class ConfigService {
  @Value("${matag.admin.url}")
  private String matagAdminUrl;

}
