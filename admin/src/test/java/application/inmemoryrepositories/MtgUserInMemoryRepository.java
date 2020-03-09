package application.inmemoryrepositories;

import com.aa.mtg.admin.user.MtgUser;
import com.aa.mtg.admin.user.MtgUserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MtgUserInMemoryRepository implements MtgUserRepository {
  private final Map<Long, MtgUser> DATA = new HashMap<>();

  @Override
  public <S extends MtgUser> S save(S mtgUser) {
    DATA.put(mtgUser.getId(), mtgUser);
    return mtgUser;
  }

  @Override
  public <S extends MtgUser> Iterable<S> saveAll(Iterable<S> MtgUsers) {
    MtgUsers.forEach(this::save);
    return MtgUsers;
  }

  @Override
  public Optional<MtgUser> findById(Long id) {
    return Optional.ofNullable(DATA.get(id));
  }

  @Override
  public boolean existsById(Long id) {
    return findById(id).isPresent();
  }

  @Override
  public Iterable<MtgUser> findAll() {
    return DATA.values();
  }

  @Override
  public Iterable<MtgUser> findAllById(Iterable<Long> ids) {
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
  public void delete(MtgUser MtgUser) {
    deleteById(MtgUser.getId());
  }

  @Override
  public void deleteAll(Iterable<? extends MtgUser> MtgUsers) {
    StreamSupport.stream(MtgUsers.spliterator(), false)
      .map(MtgUser::getId)
      .forEach(this::deleteById);
  }

  @Override
  public void deleteAll() {
    DATA.clear();
  }

  @Override
  public Optional<MtgUser> findByUsername(String username) {
    return DATA.values().stream()
      .filter(mtgUser -> mtgUser.getUsername().equals(username))
      .findFirst();
  }
}
