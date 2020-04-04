package com.matag.admin.user.profile;

import com.matag.admin.user.MatagUser;
import org.springframework.stereotype.Component;

@Component
public class MatagCurrentUserProfileService {
  public MatagCurrentUserProfileDto getProfile(MatagUser matagUser) {
    return MatagCurrentUserProfileDto.builder()
      .username(matagUser.getUsername())
      .build();
  }
}
