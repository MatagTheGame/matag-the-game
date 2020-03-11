package com.matag.admin.stats;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stats")
public class StatsController {

  @GetMapping
  public StatsResponse stats() {
    return new StatsResponse(0, 0);
  }

}
