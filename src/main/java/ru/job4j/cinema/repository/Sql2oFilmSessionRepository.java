package ru.job4j.cinema.repository;

import org.sql2o.Sql2o;
import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

public class Sql2oFilmSessionRepository implements FilmSessionRepository {

    private final Sql2o sql2o;

    public Sql2oFilmSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<FilmSession> create(FilmSession filmSession) {
        try (var connection = sql2o.open()) {
            String query = """
                    INSERT INTO film_sessions(film_id, halls_id, start_time, end_time, price)
                    VALUES(:filmId, :hallsId, :startTime, :endTime, :price)
                    """;
            int generatedId = connection.createQuery(query).executeUpdate().getKey(Integer.class);
            filmSession.setId(generatedId);
            return Optional.ofNullable(filmSession);
        }
    }

    @Override
    public Optional<FilmSession> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM film_sessions WHERE id = :id")
                    .addParameter("id", id);
            FilmSession foundFilmSession = sql.setColumnMappings(FilmSession.COLUMN_MAPPING).executeAndFetchFirst(FilmSession.class);
            return Optional.ofNullable(foundFilmSession);
        }
    }

    @Override
    public boolean update(FilmSession filmSession) {
        boolean isUpdated;
        try (var connection = sql2o.open()) {
            String query = """
                    UPDATE film_sessions
                    SET film_id = :filmId, halls_id = :hallsId, start_time = :startTime, end_time = :endTime, price = :price
                    WHERE id = :id
                    """;
            var sql = connection.createQuery(query)
                    .addParameter("filmId", filmSession.getFilmId())
                    .addParameter("hallsId", filmSession.getHallsId())
                    .addParameter("startTime", filmSession.getStartTime())
                    .addParameter("endTime", filmSession.getEndTime())
                    .addParameter("price", filmSession.getPrice())
                    .addParameter("id", filmSession.getId());
            isUpdated = sql.executeUpdate().getResult() > 0;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        boolean isDeleted;
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FROM film_sessions WHERE id = :id")
                    .addParameter("id", id);
            isDeleted = sql.executeUpdate().getResult() > 0;
        }
        return isDeleted;
    }

    @Override
    public Collection<FilmSession> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM film_sessions");
            return sql.setColumnMappings(FilmSession.COLUMN_MAPPING).executeAndFetch(FilmSession.class);
        }
    }
}
