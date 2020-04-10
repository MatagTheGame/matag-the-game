package com.matag.admin.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatagUserRepository extends CrudRepository<MatagUser, Long> {
  Optional<MatagUser> findByEmailAddress(String username);

  @Query("SELECT COUNT(id) FROM MatagUser WHERE status = ?1")
  long countUsersByStatus(MatagUserStatus userStatus);
}
