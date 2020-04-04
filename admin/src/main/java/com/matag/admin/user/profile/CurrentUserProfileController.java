package com.matag.admin.user.profile;

import com.matag.admin.auth.SecurityContextHolderHelper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/profile")
public class CurrentUserProfileController {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final CurrentUserProfileService currentUserProfileService;

  @GetMapping
  public CurrentUserProfileDto getProfile() {
    return currentUserProfileService.getProfile(securityContextHolderHelper.getUser());
  }
}
