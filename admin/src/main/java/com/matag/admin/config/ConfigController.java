package com.matag.admin.config;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/config")
@AllArgsConstructor
public class ConfigController {
  private final ConfigService configService;

  @GetMapping()
  public Map<String, String> config() {
    return configService.getConfig();
  }
}
