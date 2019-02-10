package com.aa.mtg.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(of = "username")
@Builder
public class User {
  private String username;
  private String token;
}
