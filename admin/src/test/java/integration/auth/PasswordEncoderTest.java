package integration.auth;

import com.matag.admin.MatagAdminWebSecurityConfiguration;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
  @Test
  public void encodePassword() {
    MatagAdminWebSecurityConfiguration matagAdminWebSecurityConfiguration = new MatagAdminWebSecurityConfiguration();
    PasswordEncoder passwordEncoder = matagAdminWebSecurityConfiguration.passwordEncoder();
    String encodedPassword = passwordEncoder.encode("password");
    System.out.println(encodedPassword);
  }
}
