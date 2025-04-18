package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.File;

import java.util.Collection;
import java.util.Optional;

public interface FileRepository {

    Collection<File> getAll();

    Optional<File> getById(int id);

    Optional<File> save(File file);

    boolean update(File file);

    boolean deleteById(int id);

}
