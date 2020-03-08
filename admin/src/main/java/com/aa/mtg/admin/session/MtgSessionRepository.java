package com.aa.mtg.admin.session;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MtgSessionRepository extends CrudRepository<MtgSession, String> {
}
