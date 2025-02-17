package ru.job4j.cinema.repository;

import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Hall;

import java.util.Collection;

public class Sql2oHallRepository implements HallRepository {

    private final Sql2o sql2o;

    public Sql2oHallRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Hall create(Hall hall) {
        try (var connection = sql2o.open()) {
            String query = """
                    INSERT INTO halls(name, row_count, place_count, description)
                    VALUES(:name, :rowCount, :placeCount, :description)
                    """;
            var sql = connection.createQuery(query);
            int generatedId = sql.executeUpdate().getResult();
            hall.setId(generatedId);
            return hall;
        }
    }

    @Override
    public Hall findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM halls WHERE id = :id")
                    .addParameter("id", id);

            return sql.executeAndFetchFirst(Hall.class);
        }
    }

    @Override
    public boolean update(Hall hall) {
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
                    .addParameter("description", hall.getDescription());
            sql.executeUpdate();
            return true;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FROM halls WHERE id = :id")
                    .addParameter("id", id);
            sql.executeUpdate();
            return true;
        }
    }

    @Override
    public Collection<Hall> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM halls");
            return sql.executeAndFetch(Hall.class);
        }
    }
}
