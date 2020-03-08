package com.aa.mtg.admin.auth.logout;

import com.aa.mtg.admin.session.MtgSessionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.aa.mtg.admin.session.AuthSessionFilter.SESSION_NAME;

@Component
public class MtgLogoutSuccessHandler implements LogoutSuccessHandler {
  private final MtgSessionRepository mtgSessionRepository;

  public MtgLogoutSuccessHandler(MtgSessionRepository mtgSessionRepository) {
    this.mtgSessionRepository = mtgSessionRepository;
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    String sessionId = request.getHeader(SESSION_NAME);
    if (StringUtils.hasText(sessionId)) {
      mtgSessionRepository.deleteById(sessionId);
    }

    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().flush();
  }
}
