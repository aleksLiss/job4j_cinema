package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

class Sql2oGenreRepositoryTest {

    private static Sql2oGenreRepository sql2oGenreRepository;
    private static Genre genre;

    @BeforeAll
    public static void initRepository() {
        Properties properties = new Properties();
        try (var inputStream = Sql2oGenreRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
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
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
    }

    @AfterEach
    public void deleteGenres() {
        Collection<Genre> genres = sql2oGenreRepository.getAll();
        for (Genre genre : genres) {
            sql2oGenreRepository.deleteById(genre.getId());
        }
    }

    @Test
    public void whenSaveGenreThenReturnSavedGenre() {
        genre = new Genre(1, "action");
        Optional<Genre> savedGenre = sql2oGenreRepository.save(genre);
        assertThat(savedGenre.get()).isEqualTo(genre);
    }

    @Test
    public void whenSaveGenreAndGetByIdThenReturnSavedGenre() {
        genre = new Genre(1, "action");
        Optional<Genre> savedGenre = sql2oGenreRepository.save(genre);
        Optional<Genre> foundGenre = sql2oGenreRepository.getById(savedGenre.get().getId());
        assertThat(foundGenre.get()).isEqualTo(genre);
    }

    @Test
    public void whenSaveTwoGenresThenReturnCollectionsOfTwoGenres() {
        Genre genre1 = new Genre(1, "action");
        sql2oGenreRepository.save(genre1);
        Genre genre2 = new Genre(2, "horror");
        sql2oGenreRepository.save(genre2);
        Collection<Genre> genres = sql2oGenreRepository.getAll();
        assertThat(genres)
                .isNotEmpty()
                .hasSize(2)
                .contains(genre1, genre2);
    }

    @Test
    public void whenDontSaveGenresThenReturnEmptyCollection() {
        Collection<Genre> genres = sql2oGenreRepository.getAll();
        assertThat(genres).isEmpty();
    }

    @Test
    public void whenSaveGenreAndDeleteItThenReturnEmptyOptional() {
        sql2oGenreRepository.save(new Genre(1, "action"));
        Optional<Genre> foundGenre = sql2oGenreRepository.getById(1);
        assertThat(foundGenre).isEqualTo(Optional.empty());
    }
}