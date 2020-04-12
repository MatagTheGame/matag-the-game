package com.matag.game.adminclient;

import com.matag.game.config.ConfigService;
import com.matag.game.deck.DeckInfo;
import com.matag.game.security.SecurityToken;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static java.util.Collections.singletonList;

@AllArgsConstructor
@Component
public class AdminClient {
  private final ConfigService configService;

  public DeckInfo getDeckInfo(SecurityToken token) {
    return get(token, "/game/active-deck", DeckInfo.class);
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
    headers.set("session", token.getToken());

    HttpEntity<Object> entity = new HttpEntity<>(request, headers);

    return restTemplate.exchange(configService.getMatagAdminUrl() + url, method, entity, responseType);
  }
}
