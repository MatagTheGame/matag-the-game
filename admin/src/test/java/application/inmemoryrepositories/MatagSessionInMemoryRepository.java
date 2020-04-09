package application.inmemoryrepositories;

import com.matag.admin.session.MatagSession;
import com.matag.admin.session.MatagSessionRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public class MatagSessionInMemoryRepository implements MatagSessionRepository {
  private final Map<String, MatagSession> DATA = new HashMap<>();

  @Override
  public List<MatagSession> findAll() {
    return new ArrayList<>(DATA.values());
  }

  @Override
  public List<MatagSession> findAll(Sort sort) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Page<MatagSession> findAll(Pageable pageable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<MatagSession> findAllById(Iterable<String> ids) {
    return StreamSupport.stream(ids.spliterator(), false)
      .map(this::findById)
      .map(Optional::get)
      .collect(toList());
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
    matagSessions.forEach(this::delete);
  }

  @Override
  public void deleteAll() {
    DATA.clear();
  }

  @Override
  public <S extends MatagSession> S save(S matagSession) {
    DATA.put(matagSession.getId(), matagSession);
    return matagSession;
  }

  @Override
  public <S extends MatagSession> List<S> saveAll(Iterable<S> matagSessions) {
    return StreamSupport.stream(matagSessions.spliterator(), false)
      .peek(this::save)
      .collect(Collectors.toList());
  }

  @Override
  public Optional<MatagSession> findById(String id) {
    return Optional.ofNullable(DATA.get(id));
  }

  @Override
  public boolean existsById(String id) {
    return DATA.containsKey(id);
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends MatagSession> S saveAndFlush(S s) {
    return save(s);
  }

  @Override
  public void deleteInBatch(Iterable<MatagSession> iterable) {
    deleteAll(iterable);
  }

  @Override
  public void deleteAllInBatch() {
    DATA.clear();
  }

  @Override
  public MatagSession getOne(String id) {
    return findById(id).orElse(null);
  }

  @Override
  public <S extends MatagSession> Optional<S> findOne(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends MatagSession> List<S> findAll(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends MatagSession> List<S> findAll(Example<S> example, Sort sort) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends MatagSession> Page<S> findAll(Example<S> example, Pageable pageable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends MatagSession> long count(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends MatagSession> boolean exists(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public long countOnlineUsers(LocalDateTime now) {
    return DATA.values().stream().filter(s -> s.getValidUntil().isAfter(now)).count();
  }

  @Override
  public void deleteValidUntilBefore(LocalDateTime now) {
    List<String> keysToRemove = DATA.entrySet().stream()
      .filter(e -> e.getValue().getValidUntil().isAfter(now))
      .map(Map.Entry::getKey)
      .collect(toList());

    keysToRemove.forEach(DATA::remove);
  }
}
