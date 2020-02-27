package application;

import com.aa.mtg.admin.user.User;

import static com.aa.mtg.admin.user.UserStatus.ACTIVE;

public class TestUtils {
  public static User user1() {
    User user = new User();
    user.setId(1L);
    user.setUsername("user1");
    user.setPassword("password1");
    user.setEmailAddress("user1@mtg.com");
    user.setStatus(ACTIVE);
    return user;
  }
}
