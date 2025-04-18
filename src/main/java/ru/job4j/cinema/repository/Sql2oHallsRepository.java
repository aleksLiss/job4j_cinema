package ru.job4j.cinema.repository;

import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Hall;

import java.util.Collection;
import java.util.Optional;

@Repository
public class Sql2oHallsRepository implements HallRepository {

    private final Sql2o sql2o;

    public Sql2oHallsRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Hall> getAll() {
        String sql = "SELECT * FROM halls";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                return query.setColumnMappings(Hall.COLUMN_MAPPING).executeAndFetch(Hall.class);
            }
        }
    }

    @Override
    public Optional<Hall> getById(int id) {
        String sql = "SELECT * FROM halls WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                Hall foundHall = query.addParameter("id", id).setColumnMappings(Hall.COLUMN_MAPPING).executeAndFetchFirst(Hall.class);
                return Optional.ofNullable(foundHall);
            }
        }
    }

    @Override
    public Optional<Hall> save(Hall hall) {
        String sql = "INSERT INTO halls(name, row_count, place_count, description) VALUES (:name, :rowCount, :placeCount, :description)";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                int generatedKey = query
                        .addParameter("name", hall.getName())
                        .addParameter("rowCount", hall.getRowCount())
                        .addParameter("placeCount", hall.getPlaceCount())
                        .addParameter("description", hall.getDescription())
                        .executeUpdate().getKey(Integer.class);
                hall.setId(generatedKey);
                return Optional.ofNullable(hall);
            }
        }
    }

    @Override
    public boolean update(Hall hall) {
        String sql = """
                UPDATE halls
                SET name = :name, row_count = :rowCount, place_count = :placeCount, description = :description
                WHERE id = :id
                """;
        boolean isUpdated;
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query
                        .addParameter("name", hall.getName())
                        .addParameter("rowCount", hall.getRowCount())
                        .addParameter("placeCount", hall.getPlaceCount())
                        .addParameter("description", hall.getDescription())
                        .addParameter("id", hall.getId());
                isUpdated = query.executeUpdate().getResult() > 0;
            }
        }
        return isUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM halls WHERE id = :id";
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
