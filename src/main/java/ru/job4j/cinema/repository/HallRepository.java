package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Hall;

import java.util.Collection;

public interface HallRepository {

    Hall create(Hall hall);

    Hall findById(int id);

    boolean update(Hall hall);

    boolean deleteById(int id);

    Collection<Hall> findAll();
}
