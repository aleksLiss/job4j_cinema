package ru.job4j.cinema.service;

import ru.job4j.cinema.model.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getById(int id);

    Optional<User> save(User user);

    Optional<User> findByEmailAndPassword(User user);
}
