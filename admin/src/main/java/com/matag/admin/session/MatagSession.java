package com.matag.admin.session;

import com.matag.admin.user.MatagUser;
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
@Table(name = "matag_session")
public class MatagSession {
  @Id
  private String id;
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private MatagUser matagUser;
  private LocalDateTime createdAt;
  private LocalDateTime validUntil;
}
