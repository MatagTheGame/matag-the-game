package com.aa.mtg.admin.session;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mtg_session")
public class MtgSession {
  @Id
  private String id;
  private Long mtgUserId;
  private LocalDateTime validUntil;
}
