package com.matag.admin.session;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatagSessionRepository extends CrudRepository<MatagSession, String> {
}
