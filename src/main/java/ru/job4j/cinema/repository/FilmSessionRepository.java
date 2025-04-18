package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionRepository {

    Collection<FilmSession> getAll();

    Optional<FilmSession> getById(int id);

    Optional<FilmSession> save(FilmSession filmSession);

    boolean update(FilmSession filmSession);

    boolean deleteById(int id);

}
