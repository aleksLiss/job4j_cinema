package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Hall;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

class Sql2oHallsRepositoryTest {

    private static Sql2oHallsRepository sql2oHallsRepository;

    @BeforeAll
    public static void initRepository() {
        Properties properties = new Properties();
        try (var inputStream = Sql2oHallsRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
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
        sql2oHallsRepository = new Sql2oHallsRepository(sql2o);
    }

    @AfterEach
    public void clearRepository() {
        Collection<Hall> halls = sql2oHallsRepository.getAll();
        for (Hall hall : halls) {
            sql2oHallsRepository.deleteById(hall.getId());
        }
    }

    @Test
    public void whenSaveHallAndGetByIdThenReturnSavedHall() {
        Hall hall = new Hall(1, "big", 3, 3, "descr");
        Optional<Hall> savedHall = sql2oHallsRepository.save(hall);
        assertThat(sql2oHallsRepository.getById(savedHall.get().getId()).get()).isEqualTo(hall);
    }

    @Test
    public void whenSaveTwoHallsAngGetAllThenReturnCollectionsThatContainsSavedHalls() {
        Hall hall1 = new Hall(1, "big", 30, 30, "descr");
        Hall hall2 = new Hall(2, "small", 3, 3, "descr");
        sql2oHallsRepository.save(hall1);
        sql2oHallsRepository.save(hall2);
        Collection<Hall> halls = sql2oHallsRepository.getAll();
        assertThat(halls).isNotEmpty()
                .hasSize(2)
                .contains(hall1, hall2);
    }

    @Test
    public void whenDontSaveHallsAndGetAllThenReturnEmptyCollection() {
        assertThat(sql2oHallsRepository.getAll()).isEmpty();
    }

    @Test
    public void whenDontSaveHallAndGetByIdThenReturnEmptyOptional() {
        assertThat(sql2oHallsRepository.getById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveHallAndDeleteItThenReturnEmptyOptional() {
        Hall hall = new Hall(1, "big", 3, 3, "descr");
        sql2oHallsRepository.save(hall);
        sql2oHallsRepository.deleteById(1);
        assertThat(sql2oHallsRepository.getById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveHallAndUpdateItThenReturnUpdatedHall() {
        Hall hall = new Hall(1, "big", 3, 3, "descr");
        Optional<Hall> savedHall = sql2oHallsRepository.save(hall);
        hall.setPlaceCount(300);
        hall.setRowCount(300);
        sql2oHallsRepository.update(hall);
        assertThat(sql2oHallsRepository.getById(savedHall.get().getId()).get()).isEqualTo(hall);
    }
}