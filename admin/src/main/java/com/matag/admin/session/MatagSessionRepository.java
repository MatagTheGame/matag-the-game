package com.matag.admin.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface MatagSessionRepository extends JpaRepository<MatagSession, String> {
  @Query("SELECT COUNT(id) FROM MatagSession WHERE validUntil > ?1")
  long countOnlineUsers(LocalDateTime now);

  @Modifying
  @Transactional
  @Query("DELETE FROM MatagSession WHERE validUntil < ?1")
  void deleteValidUntilBefore(LocalDateTime now);
}
