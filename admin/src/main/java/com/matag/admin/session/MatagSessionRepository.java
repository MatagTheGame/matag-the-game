package com.matag.admin.session;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MatagSessionRepository extends CrudRepository<MatagSession, String> {
  @Query("SELECT COUNT(id) FROM MatagSession WHERE validUntil > ?1")
  long countOnlineUsers(LocalDateTime now);
}
