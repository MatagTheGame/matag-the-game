package com.matag.admin.auth.login;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailRegexMatcher {
  private final static String REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
  private final static Pattern pattern = Pattern.compile(REGEX);

  public boolean isValidEmail(String email) {
    return pattern.matcher(email).matches();
  }
}
