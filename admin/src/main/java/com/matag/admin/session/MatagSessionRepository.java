package com.matag.admin.session;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MatagSessionRepository extends CrudRepository<MatagSession, String> {
  @Query("SELECT COUNT(id) FROM MatagSession WHERE validUntil > ?1")
  long countOnlineUsers(LocalDateTime now);

  @Modifying
  @Transactional
  @Query("DELETE FROM MatagSession WHERE validUntil < ?1")
  void deleteValidUntilBefore(LocalDateTime now);
}
