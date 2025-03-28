package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.File;

import java.util.Collection;
import java.util.Optional;

public interface FileRepository {

    Optional<File> create(File file);

    Optional<File> findById(int id);

    Collection<File> findAll();

    boolean deleteById(int id);

    boolean update(File file);

}
