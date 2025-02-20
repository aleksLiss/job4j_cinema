package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oUserRepositoryTest {

    private static Sql2oUserRepository sql2oUserRepository;

    @BeforeAll
    public static void initRepository() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oUserRepositoryTest.class
                .getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");
        DatasourceConfiguration datasourceConfiguration = new DatasourceConfiguration();
        DataSource dataSource = datasourceConfiguration.connectionPool(url, username, password);
        Sql2o sql2o = datasourceConfiguration.databaseClient(dataSource);
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
    }

    @AfterEach
    public void clearRepository() {
        Collection<User> users = sql2oUserRepository.findAll();
        for (User user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
    }

    @Test
    public void whenSaveUserThenReturnOptionalThatContainsSavedUser() {
        User user1 = new User(1, "Aleks Liss", "aleks@gmail.com", "123");
        assertThat(sql2oUserRepository.create(user1).get()).isEqualTo(user1);
    }

    @Test
    public void whenSaveTwoUsersWithDiffEmailThenReturnOptionalThatContainsSavedUsers() {
        User user1 = new User(1, "Aleks Liss", "aleks@gmail.com", "123");
        User user2 = new User(1, "Aleks Liss", "aleks@yandex.ru", "123");
        sql2oUserRepository.create(user1);
        assertThat(sql2oUserRepository.create(user2).get()).isEqualTo(user2);
    }

    @Test
    public void whenSaveTwoUsersWithEqualEmailThenThrowsException() {
        User user1 = new User(1, "Aleks Liss", "aleks@gmail.com", "123");
        sql2oUserRepository.create(user1);
        assertThatThrownBy(() -> sql2oUserRepository.create(user1));
    }

    @Test
    public void whenDontSaveUserAndFindByIdThenReturnEmptyOptional() {
        User user1 = new User(1, "Aleks Liss", "aleks@gmail.com", "123");
        assertThat(sql2oUserRepository.findById(user1.getId())).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveUserAndFindByIdThenReturnOptionalThatContainsFoundUser() {
        User user1 = new User(1, "Aleks Liss", "aleks@gmail.com", "123");
        sql2oUserRepository.create(user1);
        assertThat(sql2oUserRepository.findById(user1.getId()).get()).isEqualTo(user1);
    }

    @Test
    public void whenDontSaveUserAndUpdateThenReturnFalse() {
        User user1 = new User(1, "Aleks Liss", "aleks@gmail.com", "123");
        assertThat(sql2oUserRepository.update(user1)).isFalse();
    }

    @Test
    public void whenSaveUserAndUpdateThenReturnTrue() {
        User user1 = new User(1, "Aleks Liss", "aleks@gmail.com", "123");
        sql2oUserRepository.create(user1);
        assertThat(sql2oUserRepository.update(user1)).isTrue();
    }

    @Test
    public void whenDontSaveUserAndDeleteByIdThenReturnFalse() {
        User user1 = new User(1, "Aleks Liss", "aleks@gmail.com", "123");
        assertThat(sql2oUserRepository.deleteById(user1.getId())).isFalse();
    }

    @Test
    public void whenSaveUserAndDeleteByIdThenReturnTrue() {
        User user1 = new User(1, "Aleks Liss", "aleks@gmail.com", "123");
        sql2oUserRepository.create(user1);
        assertThat(sql2oUserRepository.deleteById(user1.getId())).isTrue();
    }

    @Test
    public void whenDontSaveUserAndFindAllThenReturnEmptyCollection() {
        assertThat(sql2oUserRepository.findAll()).isEmpty();
    }

    @Test
    public void whenSaveTwoUsersAndFindAllThenReturnCollectionThatContainsSavedUsers() {
        User user1 = new User(1, "Aleks Liss", "aleks@gmail.com", "123");
        User user2 = new User(1, "Aleks Neliss", "aleks@yandex.ru", "123");
        sql2oUserRepository.create(user1);
        sql2oUserRepository.create(user2);
        assertThat(sql2oUserRepository.findAll())
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(user1, user2);
    }
}