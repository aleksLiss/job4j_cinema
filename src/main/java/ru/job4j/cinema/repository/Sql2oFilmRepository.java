package ru.job4j.cinema.repository;

import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Film;

import java.util.Collection;

public class Sql2oFilmRepository implements FilmRepository {

    private final Sql2o sql2o;

    public Sql2oFilmRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Film findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM films WHERE id = :id")
                    .addParameter("id", id);
            return sql.executeAndFetchFirst(Film.class);
        }
    }

    @Override
    public Collection<Film> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM films");
            return sql.executeAndFetch(Film.class);
        }
    }

    @Override
    public Film create(Film film) {
        try (var connection = sql2o.open()) {
            String query = """
                    INSERT INTO films(name, description, year, genre_id, minimal_age, duration_in_time, file_id)
                    VALUES(:name, :description, :year, :genreId, :minimalAge, :durationInTime, :fileId)""";
            var sql = connection.createQuery(query);
            int generatedId = sql.executeUpdate().getResult();
            film.setId(generatedId);
            return film;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FROM films WHERE id = :id")
                    .addParameter("id", id);
            sql.executeUpdate();
            return true;
        }
    }

    @Override
    public boolean update(Film film) {
        try (var connection = sql2o.open()) {
            String query = """
                    UPDATE films
                    SET id = :id, name = :name, description = :description, year = :year, genre_id = :genreId,
                    minimal_age = :minimalAge, duration_in_time = :durationInTime, file_id = :fileId
                    where id = :id
                    """;
            var sql = connection.createQuery(query)
                    .addParameter("id", film.getId())
                    .addParameter("name", film.getName())
                    .addParameter("description", film.getDescription())
                    .addParameter("year", film.getYear())
                    .addParameter("genreId", film.getGenreId())
                    .addParameter("minimalAge", film.getMinimalAge())
                    .addParameter("durationInTime", film.getDurationInTime())
                    .addParameter("fileId", film.getFileId())
                    .addParameter("id", film.getId());
            sql.executeAndFetch(Film.class);
            return true;
        }
    }
}
