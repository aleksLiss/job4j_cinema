package ru.job4j.cinema.repository;

import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;

import java.util.Collection;

public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public User create(User user) {
        try (var connection = sql2o.open()) {
            String query = """
                    INSERT INTO users(full_name, email, password)
                    VALUES(:fullName, email, password)
                    """;
            int generatedId = connection.createQuery(query).executeUpdate().getResult();
            user.setId(generatedId);
            return user;
        }
    }

    @Override
    public User findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM users WHERE id = :id")
                    .addParameter("id", id);
            return sql.executeAndFetchFirst(User.class);
        }
    }

    @Override
    public boolean update(User user) {
        try (var connection = sql2o.open()) {
            String query = """
                    "UPDATE users 
                    SET full_name = :fullName, email = :email, password = :password"
                    WHERE id = :id
                    """;
            var sql = connection.createQuery(query)
                    .addParameter("name", user.getFullName())
                    .addParameter("email", user.getEmail())
                    .addParameter("password", user.getPassword());
            sql.executeUpdate();
            return true;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("DELETE FROM users WHERE id = :id")
                    .addParameter("id", id);
            sql.executeUpdate();
            return true;
        }
    }

    @Override
    public Collection<User> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM users");
            return sql.executeAndFetch(User.class);
        }
    }
}
