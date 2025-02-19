package ru.job4j.cinema.repository;

import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;

import java.util.Collection;
import java.util.Optional;

public class Sql2oFileRepository implements FileRepository {

    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<File> create(File file) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("INSERT INTO files (name, path) VALUES (:name, :path)", true)
                    .addParameter("name", file.getName())
                    .addParameter("path", file.getPath());
            int generatedId = sql.executeUpdate().getKey(Integer.class);
            file.setId(generatedId);
            return Optional.of(file);
        }
    }

    @Override
    public Optional<File> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM files WHERE id = :id")
                    .addParameter("id", id);
            File foundFile = sql.setColumnMappings(File.COLUMN_MAPPING).executeAndFetchFirst(File.class);
            return Optional.ofNullable(foundFile);
        }
    }

    @Override
    public Collection<File> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM files");
            return sql.setColumnMappings(File.COLUMN_MAPPING).executeAndFetch(File.class);
        }
    }

    @Override
    public boolean deleteById(int id) {
        boolean isDeleted;
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FROM files WHERE id = :id")
                    .addParameter("id", id);
            sql.executeUpdate();
            isDeleted = connection.getResult() != 0;
        }
        return isDeleted;
    }

    @Override
    public boolean update(File file) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("UPDATE files SET name = :name, path = :path WHERE id = :id")
                    .addParameter("name", file.getName())
                    .addParameter("path", file.getPath())
                    .addParameter("id", file.getId());
            int affectedRows = sql.executeUpdate().getResult();
            return affectedRows > 0;
        }
    }
}
