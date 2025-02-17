package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;

public interface FilmSessionRepository {

    FilmSession create(FilmSession filmSession);

    FilmSession findById(int id);

    boolean update(FilmSession filmSession);

    boolean deleteById(int id);

    Collection<FilmSession> findAll();
}
