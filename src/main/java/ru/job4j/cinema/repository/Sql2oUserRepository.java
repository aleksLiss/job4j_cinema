package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@ThreadSafe
public class Sql2oUserRepository implements UserRepository {

    private final Sql2o sql2o;

    public Sql2oUserRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Optional<User> getById(int id) {
        String sql = "SELECT * FROM users WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                User user = query
                        .setColumnMappings(User.COLUMN_MAPPING)
                        .addParameter("id", id).executeAndFetchFirst(User.class);
                return Optional.ofNullable(user);
            }
        }
    }

    @Override
    public Optional<User> save(User user) {
        String sql = "INSERT INTO users(full_name, email, password) VALUES(:fullName, :email, :password)";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query
                        .addParameter("fullName", user.getFullName())
                        .addParameter("email", user.getEmail())
                        .addParameter("password", user.getPassword());
                int generatedKey = query.executeUpdate().getKey(Integer.class);
                user.setId(generatedKey);
                return Optional.ofNullable(user);
            }
        }
    }

    @Override
    public boolean deleteById(int id) {
        String sql = "DELETE FROM users WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query.addParameter("id", id);
                return query.executeUpdate().getResult() > 0;
            }
        }
    }

    @Override
    public Optional<User> findByEmailAndPassword(User user) {
        String sql = "SELECT * FROM users WHERE email = :email AND password = :password";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query
                        .addParameter("email", user.getEmail())
                        .addParameter("password", user.getPassword());
                var foundUser = query
                        .setColumnMappings(User.COLUMN_MAPPING)
                        .executeAndFetchFirst(User.class);
                return Optional.ofNullable(foundUser);
            }
        }
    }
}
