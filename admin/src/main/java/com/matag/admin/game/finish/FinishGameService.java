package com.matag.admin.game.finish;

import com.matag.adminentities.FinishGameRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class FinishGameService {
  @Transactional
  public void finish(Long gameId, FinishGameRequest request) {
    System.out.println("ciao");
  }
}
