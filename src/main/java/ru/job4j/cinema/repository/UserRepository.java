package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {

    Optional<User> create(User user);

    Optional<User> findById(int id);

    boolean update(User user);

    boolean deleteById(int id);

    Collection<User> findAll();

}
