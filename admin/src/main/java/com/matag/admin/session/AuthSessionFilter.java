package com.matag.admin.session;

import com.matag.admin.config.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Component
@AllArgsConstructor
public class AuthSessionFilter extends GenericFilterBean {
  public final static String SESSION_NAME = "session";
  public final static String ADMIN_NAME = "admin";
  public final static int SESSION_DURATION_TIME = 60 * 60;

  private final ConfigService configService;
  private final MatagSessionRepository matagSessionRepository;
  private final Clock clock;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;

    String adminPassword = httpRequest.getHeader(ADMIN_NAME);
    String userSessionId = httpRequest.getHeader(SESSION_NAME);

    if (StringUtils.hasText(adminPassword)) {
      adminAuthentication(adminPassword);

    } else if (StringUtils.hasText(userSessionId)) {
      userAuthentication(userSessionId);
    }

    filterChain.doFilter(request, response);
  }

  private void adminAuthentication(String adminPassword) {
    if (Objects.equals(configService.getMatagAdminPassword(), adminPassword)) {
      List<SimpleGrantedAuthority> authorities = singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
      PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken("admin", configService.getMatagAdminPassword(), authorities);
      SecurityContextHolder.getContext().setAuthentication(authentication);
    }
  }

  private void userAuthentication(String sessionId) {
    Optional<MatagSession> matagSession = matagSessionRepository.findById(sessionId);

    matagSession.ifPresent(session -> {
      if (LocalDateTime.now(clock).isBefore(session.getValidUntil())) {
        List<SimpleGrantedAuthority> authorities = singletonList(new SimpleGrantedAuthority("ROLE_" + session.getMatagUser().getType().toString()));
        PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(session.getMatagUser(), session, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        if (LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME / 2).isAfter(session.getValidUntil())) {
          session.setValidUntil(LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME));
          matagSessionRepository.save(session);
        }
      }
    });
  }
}
