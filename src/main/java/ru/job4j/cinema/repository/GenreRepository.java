package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Genre;

import java.util.Collection;

public interface GenreRepository {

    Genre create(Genre genre);

    boolean update(Genre genre);

    boolean deleteById(int id);

    Genre findById(int id);

    Collection<Genre> findAll();
}
