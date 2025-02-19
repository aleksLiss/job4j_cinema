package ru.job4j.cinema.repository;

import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;

import java.util.Collection;
import java.util.Optional;

public class Sql2oGenreRepository implements GenreRepository {

    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Genre> create(Genre genre) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("INSERT INTO genres (name) VALUES (:name)")
                    .addParameter("name", genre.getName());
            int generatedId = sql.executeUpdate().getKey(Integer.class);
            genre.setId(generatedId);
            return Optional.of(genre);
        }
    }

    @Override
    public boolean update(Genre genre) {
        boolean isUpdated;
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("UPDATE genres SET name = :name WHERE id = :id")
                    .addParameter("name", genre.getName())
                    .addParameter("id", genre.getId());
            isUpdated = sql.executeUpdate().getResult() > 0;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        boolean isDeleted;
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FROM genres WHERE id = :id")
                    .addParameter("id", id);
            isDeleted = sql.executeUpdate().getResult() > 0;
        }
        return isDeleted;
    }

    @Override
    public Optional<Genre> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM genres WHERE id = :id")
                    .addParameter("id", id);
            Genre foundGenre = sql.setColumnMappings(Genre.COLUMN_MAPPING).executeAndFetchFirst(Genre.class);
            return Optional.of(foundGenre);
        }
    }

    @Override
    public Collection<Genre> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM genres");
            return sql.setColumnMappings(Genre.COLUMN_MAPPING).executeAndFetch(Genre.class);
        }
    }
}
