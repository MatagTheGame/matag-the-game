package com.matag.game.adminclient;

import static java.util.Collections.singletonList;

import com.matag.adminentities.DeckInfo;
import com.matag.adminentities.FinishGameRequest;
import com.matag.adminentities.PlayerInfo;
import com.matag.game.config.ConfigService;
import com.matag.game.player.Player;
import com.matag.game.security.SecurityToken;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Profile("!test")
@AllArgsConstructor
@Component
public class AdminClient {
  private static final Logger LOGGER = LoggerFactory.getLogger(AdminClient.class);

  private final ConfigService configService;

  public DeckInfo getDeckInfo(SecurityToken token) {
    return get(token, "/game/active-deck", DeckInfo.class);
  }

  public PlayerInfo getPlayerInfo(SecurityToken token) {
    return get(token, "/player/info", PlayerInfo.class);
  }

  public void finishGame(String gameId, Player winner) {
    try {
      adminPost("/game/" + gameId + "/finish", new FinishGameRequest(winner.getToken().getAdminToken()), Object.class);
    } catch (Exception e) {
      LOGGER.error("Could not finish the game.", e);
    }
  }

  private <T> T get(SecurityToken token, String url, Class<T> responseType) {
    return exchange(token, url, HttpMethod.GET, null, responseType).getBody();
  }

  private <T> T adminGet(String url, Class<T> responseType) {
    return exchange(null, url, HttpMethod.GET, null, responseType).getBody();
  }

  private <T> T post(SecurityToken token, String url, Object body, Class<T> responseType) {
    return exchange(token, url, HttpMethod.POST, body, responseType).getBody();
  }

  private <T> T adminPost(String url, Object body, Class<T> responseType) {
    return exchange(null, url, HttpMethod.POST, body, responseType).getBody();
  }

  private <T> ResponseEntity<T> exchange(SecurityToken token, String url, HttpMethod method, Object request, Class<T> responseType) {
    var restTemplate = new RestTemplate();
    var headers = new HttpHeaders();
    headers.setAccept(singletonList(MediaType.APPLICATION_JSON));

    if (token != null) {
      headers.set("session", token.getAdminToken());
    } else {
      headers.set("admin", configService.getAdminPassword());
    }

    var entity = new HttpEntity<>(request, headers);

    return restTemplate.exchange(configService.getMatagAdminUrl() + url, method, entity, responseType);
  }
}
