package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Film;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oFilmRepository implements FilmRepository {

    private final Sql2o sql2o;

    public Sql2oFilmRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT * FROM films";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                return query.setColumnMappings(Film.COLUMN_MAPPING).executeAndFetch(Film.class);
            }
        }
    }

    @Override
    public Optional<Film> getById(int id) {
        String sql = "SELECT * FROM films WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                Film foundFilm = query.addParameter("id", id).setColumnMappings(Film.COLUMN_MAPPING).executeAndFetchFirst(Film.class);
                return Optional.ofNullable(foundFilm);
            }
        }
    }

    @Override
    public Optional<Film> save(Film film) {
        String sql =
                """
                INSERT INTO films(name, description, "year", genre_id, minimal_age, duration_in_minutes, file_id)
                VALUES (:name, :description, :year, :genreId, :minimalAge, :durationInMinutes, :fileId)
                """;
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query
                        .addParameter("name", film.getName())
                        .addParameter("description", film.getDescription())
                        .addParameter("year", film.getYear())
                        .addParameter("genreId", film.getGenreId())
                        .addParameter("minimalAge", film.getMinimalAge())
                        .addParameter("durationInMinutes", film.getDurationInMinutes())
                        .addParameter("fileId", film.getFileId());
                int generatedKey = query.executeUpdate().getKey(Integer.class);
                film.setId(generatedKey);
                return Optional.ofNullable(film);
            }
        }
    }

    @Override
    public boolean update(Film film) {
        String sql = """
                UPDATE films
                SET name = :name, description = :description, "year" = :year, genre_id = :genreId, minimal_age = :minimalAge, duration_in_minutes = :durationInMinutes, file_id = :fileId
                where id = :id
                """;
        boolean isUpdated;
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query
                        .addParameter("name", film.getName())
                        .addParameter("description", film.getDescription())
                        .addParameter("year", film.getYear())
                        .addParameter("genreId", film.getGenreId())
                        .addParameter("minimalAge", film.getMinimalAge())
                        .addParameter("durationInMinutes", film.getDurationInMinutes())
                        .addParameter("fileId", film.getFileId())
                        .addParameter("id", film.getId());
                isUpdated = query.executeUpdate().getResult() > 0;
            }
        }
        return isUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM films WHERE id = :id";
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
