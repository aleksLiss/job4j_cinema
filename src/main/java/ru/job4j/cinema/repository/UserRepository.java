package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.User;

import java.util.Collection;

public interface UserRepository {

    User create(User user);

    User findById(int id);

    boolean update(User user);

    boolean deleteById(int id);

    Collection<User> findAll();

}
