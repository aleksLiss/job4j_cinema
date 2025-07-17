package ru.job4j.cinema.repository;

import org.junit.jupiter.api.*;
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
    private static Genre genre;
    private static File file;

    @BeforeAll
    public static void initRepository() {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFilmRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
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
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        genre = new Genre("g");
        file = new File("ff", "path3");
        sql2oFileRepository.save(file);
        sql2oGenreRepository.save(genre);
    }

    @AfterAll
    public static void cleanGenreAndFileRepo() {
        sql2oGenreRepository.deleteById(genre.getId());
        sql2oFileRepository.deleteById(file.getId());
    }

    @AfterEach
    public void cleanRepository() {
        for (Film film : sql2oFilmRepository.getAll()) {
            sql2oFilmRepository.deleteById(film.getId());
        }
    }

    @Test
    public void whenDontSaveFilmAndGetItThenReturnEmptyOptional() {
        assertThat(sql2oFilmRepository.getById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenDontSaveFilmsAndGetAllThenReturnEmptyCollection() {
        assertThat(sql2oFilmRepository.getAll()).isEmpty();
    }

    @Test
    public void whenSaveFilmAndGetItThenReturnSavedFilm() {
        Film film = new Film("film",
                "descr",
                2002,
                genre.getId(),
                18,
                180,
                file.getId());
        sql2oFilmRepository.save(film);
        assertThat(sql2oFilmRepository.getById(film.getId()).get()).isEqualTo(film);
    }

    @Test
    public void whenSaveFilmsAndGetAllThenReturnCollectionWithSavedFilms() {
        Film film1 = new Film("film1", "descr", 2002, genre.getId(), 18, 180, file.getId());
        Film film2 = new Film("film2", "descr", 2000, genre.getId(), 14, 140, file.getId());
        sql2oFilmRepository.save(film1);
        sql2oFilmRepository.save(film2);
        Collection<Film> films = sql2oFilmRepository.getAll();
        assertThat(films).isNotEmpty().hasSize(2).contains(film1, film2);
    }

    @Test
    public void whenSaveFilmAndUpdateItThenReturnUpdatedFilm() {
        Film film = new Film("film", "descr", 2002, genre.getId(), 18, 180, file.getId());
        Optional<Film> savedFilm = sql2oFilmRepository.save(film);
        film.setName("new film");
        sql2oFilmRepository.update(savedFilm.get());
        assertThat(sql2oFilmRepository.getById(savedFilm.get().getId()).get()).isEqualTo(film);
    }

    @Test
    public void whenSaveFilmAndDeleteItThenReturnEmptyOptional() {
        Film film = new Film("film", "descr", 2002, genre.getId(), 18, 180, file.getId());
        Optional<Film> savedFilm = sql2oFilmRepository.save(film);
        sql2oFilmRepository.deleteById(savedFilm.get().getId());
        assertThat(sql2oFilmRepository.getById(savedFilm.get().getId())).isEqualTo(Optional.empty());
    }
}