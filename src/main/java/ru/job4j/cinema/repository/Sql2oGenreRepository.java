package ru.job4j.cinema.repository;

import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;

import java.util.Collection;

public class Sql2oGenreRepository implements GenreRepository {

    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Genre create(Genre genre) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("INSERT INTO genres(name) VALUES(:name)")
                    .addParameter("name", genre.getName());

            int generatedId = sql.executeUpdate().getResult();
            genre.setId(generatedId);
            return genre;
        }
    }

    @Override
    public boolean update(Genre genre) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("UPDATE genres SET name = :name WHERE id = :id")
                    .addParameter("name", genre.getName())
                    .addParameter("id", genre.getId());
            sql.executeUpdate();
            return true;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FROM genres WHERE id = :id")
                    .addParameter("id", id);
            sql.executeUpdate();
            return true;
        }
    }

    @Override
    public Genre findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM genres WHERE id = :id")
                    .addParameter("id", id);
            return sql.executeAndFetchFirst(Genre.class);
        }
    }

    @Override
    public Collection<Genre> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM genres");
            return sql.executeAndFetch(Genre.class);
        }
    }
}
