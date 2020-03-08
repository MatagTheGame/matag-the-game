package application;

import com.aa.mtg.admin.user.MtgUser;

import static com.aa.mtg.admin.user.MtgUserStatus.ACTIVE;
import static com.aa.mtg.admin.user.MtgUserStatus.INACTIVE;

public class TestUtils {
  public static String PASSWORD_ENCODED = "{argon2}$argon2id$v=19$m=65536,t=4,p=8$kfWxCBLq0XIjaaG8LxfrQg$FkuvunHdrO2m+Dw85b33OUSY7uONpyVCgppJg+BYjsM";

  public static MtgUser user1() {
    MtgUser mtgUser = new MtgUser();
    mtgUser.setId(1L);
    mtgUser.setUsername("user1");
    mtgUser.setPassword(PASSWORD_ENCODED);
    mtgUser.setEmailAddress("user1@mtg.com");
    mtgUser.setStatus(ACTIVE);
    return mtgUser;
  }

  public static MtgUser inactive() {
    MtgUser mtgUser = new MtgUser();
    mtgUser.setId(10L);
    mtgUser.setUsername("inactive");
    mtgUser.setPassword(PASSWORD_ENCODED);
    mtgUser.setEmailAddress("inactive@mtg.com");
    mtgUser.setStatus(INACTIVE);
    return mtgUser;
  }
}
