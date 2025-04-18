package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Film;

import java.util.Collection;
import java.util.Optional;

public interface FilmRepository {

    Collection<Film> getAll();

    Optional<Film> getById(int id);

    Optional<Film> save(Film film);

    boolean update(Film film);

    boolean deleteById(int id);
}
