package com.matag.admin.user.profile;

import com.matag.admin.user.MatagUser;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProfileService {
  public CurrentUserProfileDto getProfile(MatagUser matagUser) {
    return CurrentUserProfileDto.builder()
      .username(matagUser.getUsername())
      .build();
  }
}
