package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oHallRepositoryTest {

    private static Sql2oHallRepository sql2oHallRepository;

    @BeforeAll
    public static void initRepository() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oHallRepositoryTest.class
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
        sql2oHallRepository = new Sql2oHallRepository(sql2o);
    }

    @AfterEach
    public void clearRetository() {
        Collection<Hall> halls = sql2oHallRepository.findAll();
        for (Hall hall : halls) {
            sql2oHallRepository.deleteById(hall.getId());
        }
    }

    @Test
    public void whenSaveHallThenReturnOptionalWithSavedHall() {
        Hall hall = new Hall(1, "big hall", 10, 10, "Very big hall");
        assertThat(sql2oHallRepository.create(hall).get()).isEqualTo(hall);
    }

    @Test
    public void whenSaveTwoHallsWithDifferentNamesThenReturnOptionalWithSavedHall() {
        Hall hall1 = new Hall(1, "big hall", 10, 10, "Very big hall");
        Hall hall2 = new Hall(1, "small hall", 3, 5, "Very small hall");
        sql2oHallRepository.create(hall1);
        assertThat(sql2oHallRepository.create(hall2).get()).isEqualTo(hall2);
    }

    @Test
    public void whenDontSaveHallAndFindByIdThenReturnEmptyOptional() {
        assertThat(sql2oHallRepository.findById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveHallAndFindByIdThenReturnSavedHall() {
        Hall hall1 = new Hall(1, "big hall", 10, 10, "Very big hall");
        assertThat(sql2oHallRepository.create(hall1).get()).isEqualTo(sql2oHallRepository.findById(hall1.getId()).get());
    }

    @Test
    public void whenDontSaveHallAndUpdateThenReturnFalse() {
        Hall hall1 = new Hall(1, "big hall", 10, 10, "Very big hall");
        assertThat(sql2oHallRepository.update(hall1)).isFalse();
    }

    @Test
    public void whenSaveHallAndUpdateThenReturnTrue() {
        Hall hall1 = new Hall(1, "big hall", 10, 10, "Very big hall");
        sql2oHallRepository.create(hall1);
        assertThat(sql2oHallRepository.update(hall1)).isTrue();
    }

    @Test
    public void whenDontSaveHallAndDeleteByIdThenReturnFalse() {
        Hall hall1 = new Hall(1, "big hall", 10, 10, "Very big hall");
        assertThat(sql2oHallRepository.deleteById(1)).isFalse();
    }

    @Test
    public void whenSaveHallAndDeleteThenReturnTrue() {
        Hall hall1 = new Hall(1, "big hall", 10, 10, "Very big hall");
        sql2oHallRepository.create(hall1);
        assertThat(sql2oHallRepository.deleteById(hall1.getId())).isTrue();
    }

    @Test
    public void whenDontSaveHallsAndFindAllThenReturnEmptyCollection() {
        assertThat(sql2oHallRepository.findAll()).isEmpty();
    }

    @Test
    public void whenSaveThreeHallsAndFindAllThenReturnCollectionWithSavedHalls() {
        Hall hall1 = new Hall(1, "big hall", 10, 10, "Very big hall");
        Hall hall2 = new Hall(1, "big hall", 10, 10, "Very big hall");
        Hall hall3 = new Hall(1, "big hall", 10, 10, "Very big hall");
        sql2oHallRepository.create(hall1);
        sql2oHallRepository.create(hall2);
        sql2oHallRepository.create(hall3);
        assertThat(sql2oHallRepository.findAll())
                .isNotEmpty()
                .hasSize(3)
                .containsExactlyInAnyOrder(hall1, hall2, hall3);
    }

}