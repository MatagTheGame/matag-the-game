package com.matag.admin.session;

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
import java.util.Optional;

import static java.util.Collections.singletonList;

@Component
@AllArgsConstructor
public class AuthSessionFilter extends GenericFilterBean {
  public final static String SESSION_NAME = "session";
  public final static int SESSION_DURATION_TIME = 60 * 60;

  private final MatagSessionRepository matagSessionRepository;
  private final Clock clock;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String sessionId = httpRequest.getHeader(SESSION_NAME);

    if (StringUtils.hasText(sessionId)) {
      Optional<MatagSession> matagSession = matagSessionRepository.findById(sessionId);

      matagSession.ifPresent(session -> {
        if (LocalDateTime.now(clock).isBefore(session.getValidUntil())) {
          List<SimpleGrantedAuthority> authorities = singletonList(new SimpleGrantedAuthority(session.getMatagUser().getType().toString()));
          PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(session.getMatagUser(), session, authorities);
          SecurityContextHolder.getContext().setAuthentication(authentication);

          if (LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME / 2).isAfter(session.getValidUntil())) {
            session.setValidUntil(LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME));
            matagSessionRepository.save(session);
          }
        }
      });
    }

    filterChain.doFilter(request, response);
  }
}
