package application.inmemoryrepositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

public abstract class AbstractInMemoryRepository<T, ID> implements CrudRepository<T, ID> {
  protected final Map<ID, T> DATA = new HashMap<>();

  protected abstract ID getId(T t);

  protected abstract void generateId(T t);

  @NonNull
  @Override
  public List<T> findAll() {
    return new ArrayList<>(DATA.values());
  }

  @NonNull
  @Override
  public List<T> findAllById(Iterable<ID> ids) {
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
  public void deleteById(@NonNull ID id) {
    DATA.remove(id);
  }

  @Override
  public void delete(@NonNull T t) {
    deleteById(getId(t));
  }

  @Override
  public void deleteAll(Iterable<? extends T> ss) {
    ss.forEach(this::delete);
  }

  @Override
  public void deleteAll() {
    DATA.clear();
  }

  @NonNull
  @Override
  public <S extends T> S save(@NonNull S s) {
    ID id = getId(s);
    if (id == null) {
      generateId(s);
    }
    DATA.put(getId(s), s);
    return s;
  }

  @NonNull
  @Override
  public <S extends T> List<S> saveAll(@NonNull Iterable<S> ss) {
    return StreamSupport.stream(ss.spliterator(), false)
      .peek(this::save)
      .collect(Collectors.toList());
  }

  @NonNull
  @Override
  public Optional<T> findById(@NonNull ID id) {
    return Optional.ofNullable(DATA.get(id));
  }

  @Override
  public boolean existsById(@NonNull ID id) {
    return DATA.containsKey(id);
  }
}
