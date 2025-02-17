package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.File;

import java.util.Collection;

public interface FileRepository {

    File create(File file);

    File findById(int id);

    Collection<File> findAll();

    boolean deleteById(int id);

    boolean update(File file);

}
