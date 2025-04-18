package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreRepository {

    Collection<Genre> getAll();

    Optional<Genre> getById(int id);

    Optional<Genre> save(Genre genre);

    boolean update(Genre genre);

    boolean deleteById(int id);
}
