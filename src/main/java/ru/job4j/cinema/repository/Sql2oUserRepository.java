package ru.job4j.cinema.repository;

import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> create(User user) {
        try (var connection = sql2o.open()) {
            String sql = """
                    INSERT INTO users (full_name, email, password)
                    VALUES (:fullName, :email, :password)
                    """;
            Query query = connection.createQuery(sql)
                    .addParameter("fullName", user.getFullName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            int generatedId = query.executeUpdate().getKey(Integer.class);
            user.setId(generatedId);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM users WHERE id = :id")
                    .addParameter("id", id);
            User foundUser = sql.setColumnMappings(User.COLUMN_MAPPING).executeAndFetchFirst(User.class);
            return Optional.ofNullable(foundUser);
        }
    }

    @Override
    public boolean update(User user) {
        boolean isUpdated;
        try (var connection = sql2o.open()) {
            String query = """
                    UPDATE users
                    SET full_name = :fullName, email = :email, password = :password
                    WHERE id = :id
                    """;
            var sql = connection.createQuery(query)
                    .addParameter("fullName", user.getFullName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword())
                    .addParameter("id", user.getId());
            isUpdated = sql.executeUpdate().getResult() > 0;
        }
        return isUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        boolean isDeleted;
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FROM users WHERE id = :id")
                    .addParameter("id", id);
            isDeleted = sql.executeUpdate().getResult() > 0;
        }
        return isDeleted;
    }

    @Override
    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM users");
            return sql.setColumnMappings(User.COLUMN_MAPPING).executeAndFetch(User.class);
        }
    }
}
