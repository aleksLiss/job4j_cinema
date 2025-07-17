package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;
    private static User user;

    @BeforeAll
    public static void initRepository() {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oUserRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");
        var configuration = new DatasourceConfiguration();
        DataSource dataSource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(dataSource);
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
        user = new User("alex", "alex@mail.ru", "123");
    }

    @AfterEach
    public void clearRepository() {
        sql2oUserRepository.deleteById(user.getId());
    }

    @Test
    public void whenDontSaveUserAndFindByIdThenReturnEmptyOptional() {
        assertThat(sql2oUserRepository.getById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveUserAndFindByIdThenReturnSavedUser() {
        sql2oUserRepository.save(user);
        Optional<User> savedUser = sql2oUserRepository.getById(user.getId());
        assertThat(savedUser.get()).isEqualTo(user);
    }

    @Test
    public void whenSaveUserAndFindByEmailAndPasswordThenReturnSavedUser() {
        sql2oUserRepository.save(user);
        Optional<User> savedUser = sql2oUserRepository.findByEmailAndPassword(user);
        assertThat(savedUser.get()).isEqualTo(user);
    }

    @Test
    public void whenSaveUserAndDeleteThenReturnTrue() {
        sql2oUserRepository.save(user);
        boolean isDeleted = sql2oUserRepository.deleteById(user.getId());
        assertThat(isDeleted).isTrue();
    }
}