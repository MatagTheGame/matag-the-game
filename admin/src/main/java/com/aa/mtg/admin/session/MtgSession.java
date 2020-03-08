package com.aa.mtg.admin.session;

import com.aa.mtg.admin.user.MtgUser;
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
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private MtgUser mtgUser;
  private LocalDateTime validUntil;
}
