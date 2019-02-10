package com.aa.mtg.user;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserRepository {

  private Set<User> users = new HashSet<>();

  public Set<User> getAllUsers() {
    return users;
  }

  public Optional<User> findByUsername(String username) {
    return users.stream()
        .filter((user) -> user.getUsername().equals(username))
        .findFirst();
  }

  public Optional<User> findByToken(String token) {
    return users.stream()
        .filter((user) -> user.getToken().equals(token))
        .findFirst();
  }

  public void addUser(User user) {
    users.add(user);
  }

  public void removeUser(User user) {
    users.remove(user);
  }

  public void removeAllUsers() {
    users.clear();
  }
}
