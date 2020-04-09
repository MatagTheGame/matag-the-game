package application.inmemoryrepositories;

import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.matag.admin.user.MatagUserStatus.ACTIVE;
import static java.util.stream.Collectors.toList;

public class MatagUserInMemoryRepository implements MatagUserRepository {
  private final Map<Long, MatagUser> DATA = new HashMap<>();

  @Override
  public List<MatagUser> findAll() {
    return new ArrayList<>(DATA.values());
  }

  @Override
  public List<MatagUser> findAll(Sort sort) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Page<MatagUser> findAll(Pageable pageable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public List<MatagUser> findAllById(Iterable<Long> ids) {
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
  public void deleteById(Long id) {
    DATA.remove(id);
  }

  @Override
  public void delete(MatagUser matagUser) {
    deleteById(matagUser.getId());
  }

  @Override
  public void deleteAll(Iterable<? extends MatagUser> matagUsers) {
    matagUsers.forEach(this::delete);
  }

  @Override
  public void deleteAll() {
    DATA.clear();
  }

  @Override
  public <S extends MatagUser> S save(S matagUser) {
    DATA.put(matagUser.getId(), matagUser);
    return matagUser;
  }

  @Override
  public <S extends MatagUser> List<S> saveAll(Iterable<S> matagUsers) {
    return StreamSupport.stream(matagUsers.spliterator(), false)
      .peek(this::save)
      .collect(Collectors.toList());
  }

  @Override
  public Optional<MatagUser> findById(Long id) {
    return Optional.ofNullable(DATA.get(id));
  }

  @Override
  public boolean existsById(Long id) {
    return DATA.containsKey(id);
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends MatagUser> S saveAndFlush(S s) {
    return save(s);
  }

  @Override
  public void deleteInBatch(Iterable<MatagUser> iterable) {
    deleteAll(iterable);
  }

  @Override
  public void deleteAllInBatch() {
    DATA.clear();
  }

  @Override
  public MatagUser getOne(Long id) {
    return findById(id).orElse(null);
  }

  @Override
  public <S extends MatagUser> Optional<S> findOne(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends MatagUser> List<S> findAll(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends MatagUser> List<S> findAll(Example<S> example, Sort sort) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends MatagUser> Page<S> findAll(Example<S> example, Pageable pageable) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends MatagUser> long count(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public <S extends MatagUser> boolean exists(Example<S> example) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<MatagUser> findByEmailAddress(String email) {
    return DATA.values().stream()
      .filter(matagUser -> matagUser.getEmailAddress().equals(email))
      .findFirst();
  }

  @Override
  public long countActiveUsers() {
    return DATA.values().stream().filter(u -> u.getStatus() == ACTIVE).count();
  }
}
