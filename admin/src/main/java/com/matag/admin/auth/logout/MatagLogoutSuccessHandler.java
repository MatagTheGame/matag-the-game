package com.matag.admin.auth.logout;

import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.session.AuthSessionFilter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.matag.admin.session.AuthSessionFilter.SESSION_NAME;

@Component
public class MatagLogoutSuccessHandler implements LogoutSuccessHandler {
  private final MatagSessionRepository matagSessionRepository;

  public MatagLogoutSuccessHandler(MatagSessionRepository matagSessionRepository) {
    this.matagSessionRepository = matagSessionRepository;
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    String sessionId = request.getHeader(SESSION_NAME);
    if (StringUtils.hasText(sessionId)) {
      matagSessionRepository.deleteById(sessionId);
    }

    response.setStatus(HttpServletResponse.SC_OK);
    response.getWriter().flush();
  }
}
