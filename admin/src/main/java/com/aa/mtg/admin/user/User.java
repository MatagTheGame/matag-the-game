package com.aa.mtg.admin.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "mtg_user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String username;
  private String password;
  private String emailAddress;
  private UserStatus status;

  public User(String username, String password, String emailAddress, UserStatus status) {
    this.username = username;
    this.password = password;
    this.emailAddress = emailAddress;
    this.status = status;
  }
}
