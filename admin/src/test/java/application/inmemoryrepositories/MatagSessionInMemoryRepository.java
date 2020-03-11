package application.inmemoryrepositories;

import com.matag.admin.session.MatagSession;
import com.matag.admin.session.MatagSessionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MatagSessionInMemoryRepository implements MatagSessionRepository {
  private final Map<String, MatagSession> DATA = new HashMap<>();

  @Override
  public <S extends MatagSession> S save(S matagSession) {
    DATA.put(matagSession.getId(), matagSession);
    return matagSession;
  }

  @Override
  public <S extends MatagSession> Iterable<S> saveAll(Iterable<S> matagSessions) {
    matagSessions.forEach(this::save);
    return matagSessions;
  }

  @Override
  public Optional<MatagSession> findById(String id) {
    return Optional.ofNullable(DATA.get(id));
  }

  @Override
  public boolean existsById(String id) {
    return findById(id).isPresent();
  }

  @Override
  public Iterable<MatagSession> findAll() {
    return DATA.values();
  }

  @Override
  public Iterable<MatagSession> findAllById(Iterable<String> ids) {
    return StreamSupport.stream(ids.spliterator(), false)
      .map(this::findById)
      .map(Optional::get)
      .collect(Collectors.toList());
  }

  @Override
  public long count() {
    return DATA.size();
  }

  @Override
  public void deleteById(String id) {
    DATA.remove(id);
  }

  @Override
  public void delete(MatagSession matagSession) {
    deleteById(matagSession.getId());
  }

  @Override
  public void deleteAll(Iterable<? extends MatagSession> matagSessions) {
    StreamSupport.stream(matagSessions.spliterator(), false)
      .map(MatagSession::getId)
      .forEach(this::deleteById);
  }

  @Override
  public void deleteAll() {
    DATA.clear();
  }
}
