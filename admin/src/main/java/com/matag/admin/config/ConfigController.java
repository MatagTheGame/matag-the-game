package com.matag.admin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static java.util.Collections.singletonMap;

@RestController
@RequestMapping("/config")
public class ConfigController {
  @Value("${matag.game.url}")
  private String matagGameUrl;

  @GetMapping()
  public Map<String, String> config() {
    return singletonMap("matagGameUrl", matagGameUrl);
  }
}
