package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Hall;

import java.util.Collection;
import java.util.Optional;

public interface HallRepository {

    Collection<Hall> getAll();

    Optional<Hall> getById(int id);

    Optional<Hall> save(Hall hall);

    boolean update(Hall hall);

    boolean deleteById(int id);
}
