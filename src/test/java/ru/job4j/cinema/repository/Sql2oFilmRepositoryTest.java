package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oFilmRepositoryTest {

    private static Sql2oFilmRepository sql2oFilmRepository;
    private static Sql2oGenreRepository sql2oGenreRepository;
    private static Sql2oFileRepository sql2oFileRepository;

    @BeforeAll
    public static void initRepositories() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFilmRepositoryTest.class
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
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @AfterEach
    public void clearRepositories() {
        Collection<Film> films = sql2oFilmRepository.findAll();
        for (Film film : films) {
            sql2oFilmRepository.deleteById(film.getId());
        }

        Collection<Genre> genres = sql2oGenreRepository.findAll();
        for (Genre genre : genres) {
            sql2oGenreRepository.deleteById(genre.getId());
        }

        Collection<File> files = sql2oFileRepository.findAll();
        for (File file : files) {
            sql2oFileRepository.deleteById(file.getId());
        }
    }

    @Test
    public void whenDontSaveFilmAndFindByIdThenReturnEmptyOptional() {
        assertThat(sql2oFilmRepository.findById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveFilmAndFindByIdThenReturnSavedFilm() {
        Genre genre = new Genre(1, "horror");
        File file = new File("name", "path/to/file");
        sql2oGenreRepository.create(genre);
        sql2oFileRepository.create(file);
        Film film = new Film(
                1,
                "Jonh Wick",
                "Goog film",
                2020,
                sql2oGenreRepository.findById(genre.getId()).get().getId(),
                18,
                120,
                sql2oFileRepository.findById(file.getId()).get().getId());
        sql2oFilmRepository.create(film);
        assertThat(sql2oFilmRepository.findById(film.getId()).get().getName()).isEqualTo(film.getName());
    }

    @Test
    public void whenDontSaveFilmsAndFindAllThenReturnEmptyCollection() {
        assertThat(sql2oFilmRepository.findAll()).isEmpty();
    }

    @Test
    public void whenSaveTwoFilmsAndFindAllThenReturnCollectionWithSavedFilms() {
        Genre genre = new Genre(1, "horror");
        File file1 = new File("name", "path/to/file1");
        File file2 = new File("name", "path/to/file2");
        sql2oGenreRepository.create(genre);
        sql2oFileRepository.create(file1);
        sql2oFileRepository.create(file2);
        Film film1 = new Film(
                1,
                "Jonh Wick",
                "Goog film",
                2020,
                sql2oGenreRepository.findById(genre.getId()).get().getId(),
                18,
                120,
                sql2oFileRepository.findById(file1.getId()).get().getId());
        Film film2 = new Film(
                1,
                "Jonh Wick 2",
                "Goog film",
                2020,
                sql2oGenreRepository.findById(genre.getId()).get().getId(),
                18,
                120,
                sql2oFileRepository.findById(file2.getId()).get().getId());

        sql2oFilmRepository.create(film1);
        sql2oFilmRepository.create(film2);
        assertThat(sql2oFilmRepository.findAll())
                .isNotEmpty()
                .hasSize(2);
    }

    @Test
    public void whenSaveFilmThenReturnOptionalWithSavedFilm() {
        Genre genre = new Genre(1, "horror");
        File file = new File("name", "path/to/file");
        sql2oGenreRepository.create(genre);
        sql2oFileRepository.create(file);
        Film film = new Film(
                1,
                "Jonh Wick",
                "Goog film",
                2020,
                sql2oGenreRepository.findById(genre.getId()).get().getId(),
                18,
                120,
                sql2oFileRepository.findById(file.getId()).get().getId());
        sql2oFilmRepository.create(film);
        assertThat(sql2oFilmRepository.findById(film.getId()).get().getName()).isEqualTo(film.getName());
    }

    @Test
    public void whenDontSaveFilmAndDeleteByIdThenReturnFalse() {
        assertThat(sql2oFilmRepository.deleteById(1)).isFalse();
    }

    @Test
    public void whenSaveFilmAndDeleteByIdThenReturnTrue() {
        Genre genre = new Genre(1, "horror");
        File file = new File("name", "path/to/file");
        sql2oGenreRepository.create(genre);
        sql2oFileRepository.create(file);
        Film film = new Film(
                1,
                "Jonh Wick",
                "Goog film",
                2020,
                sql2oGenreRepository.findById(genre.getId()).get().getId(),
                18,
                120,
                sql2oFileRepository.findById(file.getId()).get().getId());
        sql2oFilmRepository.create(film);
        assertThat(sql2oFilmRepository.deleteById(film.getId())).isTrue();
    }

    @Test
    public void whenDontSaveFilmAndUpdateThenReturnFalse() {
        Genre genre = new Genre(1, "horror");
        File file = new File("name", "path/to/file");
        sql2oGenreRepository.create(genre);
        sql2oFileRepository.create(file);
        Film film = new Film(
                1,
                "Jonh Wick",
                "Goog film",
                2020,
                sql2oGenreRepository.findById(genre.getId()).get().getId(),
                18,
                120,
                sql2oFileRepository.findById(file.getId()).get().getId());
        assertThat(sql2oFilmRepository.update(film)).isFalse();
    }

    @Test
    public void whenSaveFilmAndUpdateThenReturnTrue() {
        Genre genre = new Genre(1, "horror");
        File file = new File("name", "path/to/file");
        sql2oGenreRepository.create(genre);
        sql2oFileRepository.create(file);
        Film film = new Film(
                1,
                "Jonh Wick",
                "Goog film",
                2020,
                sql2oGenreRepository.findById(genre.getId()).get().getId(),
                18,
                120,
                sql2oFileRepository.findById(file.getId()).get().getId());
        sql2oFilmRepository.create(film);
        assertThat(sql2oFilmRepository.update(film)).isTrue();
    }
}