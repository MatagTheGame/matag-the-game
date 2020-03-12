package application;

import com.matag.admin.user.MatagUser;

import static com.matag.admin.user.MatagUserStatus.ACTIVE;
import static com.matag.admin.user.MatagUserStatus.INACTIVE;

public class TestUtils {
  public static String PASSWORD_ENCODED = "{argon2}$argon2id$v=19$m=65536,t=4,p=8$kfWxCBLq0XIjaaG8LxfrQg$FkuvunHdrO2m+Dw85b33OUSY7uONpyVCgppJg+BYjsM";

  public static MatagUser user1() {
    MatagUser matagUser = new MatagUser();
    matagUser.setId(1L);
    matagUser.setUsername("user1");
    matagUser.setPassword(PASSWORD_ENCODED);
    matagUser.setEmailAddress("user1@matag.com");
    matagUser.setStatus(ACTIVE);
    return matagUser;
  }

  public static MatagUser user2() {
    MatagUser matagUser = new MatagUser();
    matagUser.setId(2L);
    matagUser.setUsername("user2");
    matagUser.setPassword(PASSWORD_ENCODED);
    matagUser.setEmailAddress("user2@matag.com");
    matagUser.setStatus(ACTIVE);
    return matagUser;
  }

  public static MatagUser inactive() {
    MatagUser matagUser = new MatagUser();
    matagUser.setId(10L);
    matagUser.setUsername("inactive");
    matagUser.setPassword(PASSWORD_ENCODED);
    matagUser.setEmailAddress("inactive@matag.com");
    matagUser.setStatus(INACTIVE);
    return matagUser;
  }
}
