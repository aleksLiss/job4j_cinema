package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.FilmSession;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oFilmSessionRepository implements FilmSessionRepository {

    private final Sql2o sql2o;

    public Sql2oFilmSessionRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<FilmSession> getAll() {
        String sql = "SELECT * FROM film_sessions";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                return query.setColumnMappings(FilmSession.COLUMN_MAPPING).executeAndFetch(FilmSession.class);
            }
        }
    }

    @Override
    public Optional<FilmSession> getById(int id) {
        String sql = "SELECT * FROM film_sessions WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                FilmSession foundFilmSession = query.addParameter("id", id)
                        .setColumnMappings(FilmSession.COLUMN_MAPPING)
                        .executeAndFetchFirst(FilmSession.class);
                return Optional.ofNullable(foundFilmSession);
            }
        }
    }

    @Override
    public Optional<FilmSession> save(FilmSession filmSession) {
        String sql = """
                INSERT INTO film_sessions(film_id, halls_id, start_time, end_time, price) 
                VALUES(:filmId, :hallsId, :startTime, :endTime, :price)
                """;
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query
                        .addParameter("filmId", filmSession.getFilmId())
                        .addParameter("hallsId", filmSession.getHallsId())
                        .addParameter("startTime", filmSession.getStartTime())
                        .addParameter("endTime", filmSession.getEndTime())
                        .addParameter("price", filmSession.getPrice());
                int generatedKey = query.executeUpdate().getKey(Integer.class);
                filmSession.setId(generatedKey);
                return Optional.ofNullable(filmSession);
            }
        }
    }

    @Override
    public boolean update(FilmSession filmSession) {
        String sql = """
                UPDATE film_sessions
                SET filmId = :filmId, hallsId = :hallsId, startTime = :startTime, endTime = :endTime, price = :price
                WHERE id = :id
                """;
        boolean isUpdated;
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query
                        .addParameter("filmId", filmSession.getFilmId())
                        .addParameter("hallsId", filmSession.getHallsId())
                        .addParameter("startTime", filmSession.getStartTime())
                        .addParameter("endTime", filmSession.getEndTime())
                        .addParameter("price", filmSession.getPrice())
                        .addParameter("id", filmSession.getId());
                isUpdated = query.executeUpdate().getResult() > 0;
            }
        }
        return isUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM film_sessions WHERE id = :id";
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
