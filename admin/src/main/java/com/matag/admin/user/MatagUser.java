package com.matag.admin.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "matag_user")
public class MatagUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String username;
  private String password;
  private String emailAddress;
  @Enumerated(EnumType.STRING)
  private MatagUserStatus status;
  @Enumerated(EnumType.STRING)
  private MatagUserType type;
  private LocalDateTime createdAt;
}
