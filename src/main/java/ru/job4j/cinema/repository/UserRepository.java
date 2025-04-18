package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Collection<User> getAll();

    Optional<User> getById(int id);

    Optional<User> save(User user);

    boolean update(User user);

    boolean deleteById(int id);

}
