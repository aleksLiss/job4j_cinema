package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreRepository {

    Optional<Genre> create(Genre genre);

    boolean update(Genre genre);

    boolean deleteById(int id);

    Optional<Genre> findById(int id);

    Collection<Genre> findAll();
}
