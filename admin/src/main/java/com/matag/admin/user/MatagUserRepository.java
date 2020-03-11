package com.matag.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatagUserRepository extends CrudRepository<MatagUser, Long> {
  Optional<MatagUser> findByUsername(String username);
}
