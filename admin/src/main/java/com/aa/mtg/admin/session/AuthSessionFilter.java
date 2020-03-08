package com.aa.mtg.admin.session;

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
import java.util.Collections;
import java.util.Optional;

@Component
public class AuthSessionFilter extends GenericFilterBean {
  public final static String SESSION_NAME = "session";

  private final MtgSessionRepository mtgSessionRepository;

  public AuthSessionFilter(MtgSessionRepository mtgSessionRepository) {
    this.mtgSessionRepository = mtgSessionRepository;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    String sessionId = httpRequest.getHeader(SESSION_NAME);

    if (StringUtils.hasText(sessionId)) {
      Optional<MtgSession> mtgSession = mtgSessionRepository.findById(sessionId);
      mtgSession.ifPresent(session -> SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(session.getMtgUser(), session.getMtgUser().getPassword(), Collections.singletonList(new SimpleGrantedAuthority("USER"))))
      );
    }

    filterChain.doFilter(request, response);
  }
}
