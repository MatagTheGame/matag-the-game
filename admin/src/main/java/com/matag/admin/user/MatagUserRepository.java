package com.matag.admin.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatagUserRepository extends JpaRepository<MatagUser, Long> {
  Optional<MatagUser> findByEmailAddress(String username);

  @Query("SELECT COUNT(id) FROM MatagUser WHERE status = 'ACTIVE'")
  long countActiveUsers();
}
