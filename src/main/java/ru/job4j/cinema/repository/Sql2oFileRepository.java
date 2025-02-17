package ru.job4j.cinema.repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;

import java.sql.Connection;
import java.util.Collection;
import java.util.List;

public class Sql2oFileRepository implements FileRepository {

    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public File create(File file) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("INSERT INTO files(id, name, path) VALUES(:id, :name, :path)")
                    .addParameter("id", file.getId())
                    .addParameter("name", file.getName())
                    .addParameter("path", file.getPath());
            int generatedId = sql.executeUpdate().getResult();
            file.setId(generatedId);
            return file;
        }
    }

    @Override
    public File findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM files WHERE id = :id")
                    .addParameter("id", id);
            return sql.executeAndFetchFirst(File.class);
        }
    }

    @Override
    public Collection<File> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM files");
            return sql.executeAndFetch(File.class);
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FORM files WHERE id = :id")
                    .addParameter("id", id);
            sql.executeUpdate();
            return true;
        }
    }

    @Override
    public boolean update(File file) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("UPDATE files SET name = :name, path = :path WHERE id = :id")
                    .addParameter("name", file.getName())
                    .addParameter("path", file.getPath())
                    .addParameter("id", file.getId());
            sql.executeAndFetch(File.class);
            return true;
        }
    }
}
