package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oGenreRepository implements GenreRepository {

    private final Sql2o sql2o;

    public Sql2oGenreRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Genre> getAll() {
        String sql = "SELECT * FROM genres";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                return query.executeAndFetch(Genre.class);
            }
        }
    }

    @Override
    public Optional<Genre> getById(int id) {
        String sql = "SELECT * FROM genres WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                Genre genre = query.addParameter("id", id).executeAndFetchFirst(Genre.class);
                return Optional.ofNullable(genre);
            }
        }
    }

    @Override
    public Optional<Genre> save(Genre genre) {
        String sql = "INSERT INTO genres(name) VALUES(:name)";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query.addParameter("name", genre.getName());
                int generatedKey = query.executeUpdate().getKey(Integer.class);
                genre.setId(generatedKey);
                return Optional.ofNullable(genre);
            }
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM genres WHERE id = :id";
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
