package com.aa.mtg.admin.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class AuthSessionFilter extends GenericFilterBean {
  public final static String SESSION_NAME = "session";
  public final static int SESSION_DURATION_TIME = 60 * 60;

  private final MtgSessionRepository mtgSessionRepository;
  private final Clock clock;

  @Autowired
  public AuthSessionFilter(MtgSessionRepository mtgSessionRepository, Clock clock) {
    this.mtgSessionRepository = mtgSessionRepository;
    this.clock = clock;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String sessionId = httpRequest.getHeader(SESSION_NAME);

    if (StringUtils.hasText(sessionId)) {
      Optional<MtgSession> mtgSession = mtgSessionRepository.findById(sessionId);
      mtgSession.ifPresent(session -> {
        if (LocalDateTime.now(clock).isBefore(session.getValidUntil())) {
          List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(session.getMtgUser(), session.getMtgUser().getPassword(), authorities);
          SecurityContextHolder.getContext().setAuthentication(authentication);

          if (LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME / 2).isAfter(session.getValidUntil())) {
            session.setValidUntil(LocalDateTime.now(clock).plusSeconds(SESSION_DURATION_TIME));
            mtgSessionRepository.save(session);
          }
        }
      });
    }

    filterChain.doFilter(request, response);
  }
}
