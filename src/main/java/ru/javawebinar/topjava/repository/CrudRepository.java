package ru.javawebinar.topjava.repository;

import java.util.List;

public interface CrudRepository<T, ID> {
    long count();

    void delete(T entity);

    void deleteAll();

    void deleteAll(Iterable<? extends T> entities);

    void deleteById(ID id);

    boolean existsById(ID id);

    List<T> findAll();

    List<T> findAllById(Iterable<ID> ids);

    T findById(ID id);

    <S extends T> S save(S entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

}
