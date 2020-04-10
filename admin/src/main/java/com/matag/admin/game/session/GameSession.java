package com.matag.admin.game.session;

import com.matag.admin.game.Game;
import com.matag.admin.session.MatagSession;
import com.matag.admin.user.MatagUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "game_session")
public class GameSession {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private Game game;
  private short sessionNum;
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private MatagSession session;
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private MatagUser player;

}
