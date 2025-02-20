package ru.job4j.cinema.repository;

import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Film;

import java.util.Collection;
import java.util.Optional;

public class Sql2oFilmRepository implements FilmRepository {

    private final Sql2o sql2o;

    public Sql2oFilmRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Film> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM films WHERE id = :id")
                    .addParameter("id", id);
            Film foundFilm = sql.setColumnMappings(Film.COLUMN_MAPPING).executeAndFetchFirst(Film.class);
            return Optional.ofNullable(foundFilm);
        }
    }

    @Override
    public Collection<Film> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM films");
            return sql.setColumnMappings(Film.COLUMN_MAPPING).executeAndFetch(Film.class);
        }
    }

    @Override
    public Optional<Film> create(Film film) {
        try (var connection = sql2o.open()) {
            String sql = """
                    INSERT INTO films (name, description, "year", genre_id, minimal_age, duration_in_minutes, file_id)
                    VALUES (:name, :description, :year, :genreId, :minimalAge, :durationInMinutes, :fileId)""";
            Query query = connection.createQuery(sql)
                    .addParameter("name", film.getName())
                    .addParameter("description", film.getDescription())
                    .addParameter("year", film.getYear())
                    .addParameter("genreId", film.getGenreId())
                    .addParameter("minimalAge", film.getMinimalAge())
                    .addParameter("durationInMinutes", film.getDurationInTime())
                    .addParameter("fileId", film.getFileId());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            film.setId(generatedId);
            return Optional.ofNullable(film);
        }
    }

    @Override
    public boolean deleteById(int id) {
        boolean isDeleted;
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FROM films WHERE id = :id")
                    .addParameter("id", id);
            isDeleted = sql.executeUpdate().getResult() > 0;
        }
        return isDeleted;
    }

    @Override
    public boolean update(Film film) {
        boolean isUpdated;
        try (var connection = sql2o.open()) {
            String query = """
                    UPDATE films
                    SET name = :name, description = :description, "year" = :year, genre_id = :genreId,
                    minimal_age = :minimalAge, duration_in_minutes = :durationInMinutes, file_id = :fileId
                    WHERE id = :id
                    """;
            var sql = connection.createQuery(query)
                    .addParameter("name", film.getName())
                    .addParameter("description", film.getDescription())
                    .addParameter("year", film.getYear())
                    .addParameter("genreId", film.getGenreId())
                    .addParameter("minimalAge", film.getMinimalAge())
                    .addParameter("durationInMinutes", film.getDurationInTime())
                    .addParameter("fileId", film.getFileId())
                    .addParameter("id", film.getId());
            isUpdated = sql.executeUpdate().getResult() > 0;
        }
        return isUpdated;
    }
}
