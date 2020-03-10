package integration;

import com.aa.mtg.admin.MtgAdminWebSecurityConfiguration;
import org.junit.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordEncoderTest {
  @Test
  public void encodePassword() {
    MtgAdminWebSecurityConfiguration mtgAdminWebSecurityConfiguration = new MtgAdminWebSecurityConfiguration();
    PasswordEncoder passwordEncoder = mtgAdminWebSecurityConfiguration.passwordEncoder();
    String encodedPassword = passwordEncoder.encode("password");
    System.out.println(encodedPassword);
  }
}
