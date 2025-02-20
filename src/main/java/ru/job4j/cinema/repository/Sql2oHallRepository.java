package ru.job4j.cinema.repository;

import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Hall;

import java.util.Collection;
import java.util.Optional;

public class Sql2oHallRepository implements HallRepository {

    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<Hall> create(Hall hall) {
        try (var connection = sql2o.open()) {
            String query = """
                    INSERT INTO halls (name, row_count, place_count, description)
                    VALUES (:name, :rowCount, :placeCount, :description)
                    """;
            var sql = connection.createQuery(query)
                    .addParameter("name", hall.getName())
                    .addParameter("rowCount", hall.getRowCount())
                    .addParameter("placeCount", hall.getPlaceCount())
                    .addParameter("description", hall.getDescription());
            int generatedId = sql.executeUpdate().getKey(Integer.class);
            hall.setId(generatedId);
            return Optional.ofNullable(hall);
        }
    }

    @Override
    public Optional<Hall> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM halls WHERE id = :id")
                    .addParameter("id", id);
            Hall foundHall = sql.setColumnMappings(Hall.COLUMN_MAPPING).executeAndFetchFirst(Hall.class);
            return Optional.ofNullable(foundHall);
        }
    }

    @Override
    public boolean update(Hall hall) {
        boolean isUpdated;
        try (var connection = sql2o.open()) {
            String query = """
                    UPDATE halls
                    SET name = :name, row_count = :rowCount, place_count = :placeCount, description = :description
                    WHERE id = :id
                    """;
            var sql = connection.createQuery(query)
                    .addParameter("name", hall.getName())
                    .addParameter("rowCount", hall.getRowCount())
                    .addParameter("placeCount", hall.getPlaceCount())
                    .addParameter("description", hall.getDescription())
                    .addParameter("id", hall.getId());
            isUpdated = sql.executeUpdate().getResult() > 0;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        boolean isDeleted;
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FROM halls WHERE id = :id")
                    .addParameter("id", id);
            isDeleted = sql.executeUpdate().getResult() > 0;
        }
        return isDeleted;
    }

    @Override
    public Collection<Hall> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM halls");
            return sql.setColumnMappings(Hall.COLUMN_MAPPING).executeAndFetch(Hall.class);
        }
    }
}
