package com.matag.game.adminclient;

import com.matag.adminentities.DeckInfo;
import com.matag.adminentities.PlayerInfo;
import com.matag.game.config.ConfigService;
import com.matag.game.security.SecurityToken;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;

@Profile("!test")
@AllArgsConstructor
@Component
public class AdminClient {
  private final ConfigService configService;

  public DeckInfo getDeckInfo(SecurityToken token) {
    return get(token, "/game/active-deck", DeckInfo.class);
  }

  public PlayerInfo getPlayerInfo(SecurityToken token) {
    return get(token, "/player/info", PlayerInfo.class);
  }

  private <T> T get(SecurityToken token, String url, Class<T> responseType) {
    return exchange(token, url, HttpMethod.GET, null, responseType).getBody();
  }

  private <T> T post(SecurityToken token, String url, Object body, Class<T> responseType) {
    return exchange(token, url, HttpMethod.POST, body, responseType).getBody();
  }

  private <T> ResponseEntity<T> exchange(SecurityToken token, String url, HttpMethod method, Object request, Class<T> responseType) {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(singletonList(MediaType.APPLICATION_JSON));
    headers.set("session", token.getAdminToken());

    HttpEntity<Object> entity = new HttpEntity<>(request, headers);

    return restTemplate.exchange(configService.getMatagAdminUrl() + url, method, entity, responseType);
  }
}
