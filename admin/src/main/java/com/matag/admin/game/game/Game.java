package com.matag.admin.game.game;

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
@Table(name = "game")
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDateTime createdAt;
  @Enumerated(EnumType.STRING)
  private GameType type;
  @Enumerated(EnumType.STRING)
  private GameStatusType status;
  @Enumerated(EnumType.STRING)
  private GameResultType result;
  private LocalDateTime finishedAt;
}
