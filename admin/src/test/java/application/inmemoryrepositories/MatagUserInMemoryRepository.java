package application.inmemoryrepositories;

import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MatagUserInMemoryRepository implements MatagUserRepository {
  private final Map<Long, MatagUser> DATA = new HashMap<>();

  @Override
  public <S extends MatagUser> S save(S matagUser) {
    DATA.put(matagUser.getId(), matagUser);
    return matagUser;
  }

  @Override
  public <S extends MatagUser> Iterable<S> saveAll(Iterable<S> matagUsers) {
    matagUsers.forEach(this::save);
    return matagUsers;
  }

  @Override
  public Optional<MatagUser> findById(Long id) {
    return Optional.ofNullable(DATA.get(id));
  }

  @Override
  public boolean existsById(Long id) {
    return findById(id).isPresent();
  }

  @Override
  public Iterable<MatagUser> findAll() {
    return DATA.values();
  }

  @Override
  public Iterable<MatagUser> findAllById(Iterable<Long> ids) {
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
  public void deleteById(Long id) {
    DATA.remove(id);
  }

  @Override
  public void delete(MatagUser MatagUser) {
    deleteById(MatagUser.getId());
  }

  @Override
  public void deleteAll(Iterable<? extends MatagUser> matagUsers) {
    StreamSupport.stream(matagUsers.spliterator(), false)
      .map(MatagUser::getId)
      .forEach(this::deleteById);
  }

  @Override
  public void deleteAll() {
    DATA.clear();
  }

  @Override
  public Optional<MatagUser> findByUsername(String username) {
    return DATA.values().stream()
      .filter(matagUser -> matagUser.getUsername().equals(username))
      .findFirst();
  }
}
