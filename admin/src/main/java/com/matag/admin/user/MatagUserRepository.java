package com.matag.admin.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatagUserRepository extends CrudRepository<MatagUser, Long> {
  Optional<MatagUser> findByEmailAddress(String username);

  @Query("SELECT COUNT(id) FROM MatagUser WHERE status = 'ACTIVE'")
  long countActiveUsers();
}
