package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.File;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oFileRepository implements FileRepository {

    private final Sql2o sql2o;

    public Sql2oFileRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<File> getAll() {
        String sql = "SELECT * FROM files";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                return query.executeAndFetch(File.class);
            }
        }
    }

    @Override
    public Optional<File> getById(int id) {
        String sql = "SELECT * FROM files WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                File file = query.addParameter("id", id)
                        .executeAndFetchFirst(File.class);
                return Optional.ofNullable(file);
            }
        }
    }

    @Override
    public Optional<File> save(File file) {
        String sql = "INSERT INTO files(name, path) VALUES(:name, :path)";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query.addParameter("name", file.getName())
                        .addParameter("path", file.getPath());
                int generatedKey = query.executeUpdate().getKey(Integer.class);
                file.setId(generatedKey);
                return Optional.ofNullable(file);
            }
        }
    }

    @Override
    public boolean update(File file) {
        String sql = "UPDATE files SET name = :name, path = :path WHERE id = :id";
        boolean isUpdated;
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query.addParameter("name", file.getName())
                        .addParameter("path", file.getPath())
                        .addParameter("id", file.getId());
                isUpdated = query.executeUpdate().getResult() > 0;
            }
        }
        return isUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM files WHERE id = :id";
        boolean isDeleted;
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query.addParameter("id", id);
                isDeleted = query.executeUpdate().getResult() > 0;
            }
        }
        return isDeleted;
    }
}
