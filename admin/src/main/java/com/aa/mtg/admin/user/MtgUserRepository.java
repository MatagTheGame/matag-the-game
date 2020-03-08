package com.aa.mtg.admin.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MtgUserRepository extends CrudRepository<MtgUser, Long> {
  Optional<MtgUser> findByUsername(String username);
}
