package application.inmemoryrepositories;

import com.aa.mtg.admin.session.MtgSession;
import com.aa.mtg.admin.session.MtgSessionRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MtgSessionInMemoryRepository implements MtgSessionRepository {
  private final Map<String, MtgSession> DATA = new HashMap<>();

  @Override
  public <S extends MtgSession> S save(S mtgSession) {
    DATA.put(mtgSession.getId(), mtgSession);
    return mtgSession;
  }

  @Override
  public <S extends MtgSession> Iterable<S> saveAll(Iterable<S> mtgSessions) {
    mtgSessions.forEach(this::save);
    return mtgSessions;
  }

  @Override
  public Optional<MtgSession> findById(String id) {
    return Optional.ofNullable(DATA.get(id));
  }

  @Override
  public boolean existsById(String id) {
    return findById(id).isPresent();
  }

  @Override
  public Iterable<MtgSession> findAll() {
    return DATA.values();
  }

  @Override
  public Iterable<MtgSession> findAllById(Iterable<String> ids) {
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
  public void delete(MtgSession mtgSession) {
    deleteById(mtgSession.getId());
  }

  @Override
  public void deleteAll(Iterable<? extends MtgSession> mtgSessions) {
    StreamSupport.stream(mtgSessions.spliterator(), false)
      .map(MtgSession::getId)
      .forEach(this::deleteById);
  }

  @Override
  public void deleteAll() {
    DATA.clear();
  }
}
