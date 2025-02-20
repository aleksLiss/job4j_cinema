package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.*;

import javax.sql.DataSource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oFilmSessionRepositoryTest {

    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;
    private static Sql2oHallRepository sql2oHallRepository;
    private static Sql2oFilmRepository sql2oFilmRepository;
    private static Sql2oGenreRepository sql2oGenreRepository;
    private static Sql2oFileRepository sql2oFileRepository;

    @BeforeAll
    public static void initRepository() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFilmSessionRepositoryTest.class
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
        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
        sql2oHallRepository = new Sql2oHallRepository(sql2o);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @AfterEach
    public void clearRepository() {
        Collection<FilmSession> filmSessions = sql2oFilmSessionRepository.findAll();
        for (FilmSession filmSession : filmSessions) {
            sql2oFilmSessionRepository.deleteById(filmSession.getId());
        }
        Collection<Film> films = sql2oFilmRepository.findAll();
        for (Film film : films) {
            sql2oFilmRepository.deleteById(film.getId());
        }
        Collection<Hall> halls = sql2oHallRepository.findAll();
        for (Hall hall : halls) {
            sql2oHallRepository.deleteById(hall.getId());
        }
        Collection<Genre> genres = sql2oGenreRepository.findAll();
        for (Genre genre : genres) {
            sql2oGenreRepository.deleteById(genre.getId());
        }
        Collection<File> files = sql2oFileRepository.findAll();
        for(File file : files) {
            sql2oFileRepository.deleteById(file.getId());
        }
    }

    @Test
    public void whenSaveFilmSessionThenReturnOptionalWithSavedFilmSession() {
        Genre genre = new Genre(1, "horror");
        sql2oGenreRepository.create(genre);
        File file = new File("name", "path/to/file");
        sql2oFileRepository.create(file);
        Film film = new Film(1, "Jonh", "Good film", 2019, genre.getId(), 18, 120, file.getId());
        sql2oFilmRepository.create(film);
        Hall hall = new Hall(1, "big", 10, 10, "big hall");
        sql2oHallRepository.create(hall);
        FilmSession filmSession = new FilmSession(1, film.getId(), hall.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), 120);
        sql2oFilmSessionRepository.create(filmSession);
        assertThat(sql2oFilmSessionRepository.findById(filmSession.getId()).get()).isEqualTo(filmSession);
    }

    @Test
    public void whenDontSaveFilmSessionAndFindByIdThenReturnEmptyOptional() {
        assertThat(sql2oFilmSessionRepository.findById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveFilmSessionAndFindByIdThenReturnOptionalThatContainsSavedFilmSession() {
        Genre genre = new Genre(1, "horror");
        sql2oGenreRepository.create(genre);
        File file = new File("name", "path/to/file");
        sql2oFileRepository.create(file);
        Film film = new Film(1, "Jonh", "Good film", 2019, genre.getId(), 18, 120, file.getId());
        sql2oFilmRepository.create(film);
        Hall hall = new Hall(1, "big", 10, 10, "big hall");
        sql2oHallRepository.create(hall);
        FilmSession filmSession = new FilmSession(1, film.getId(), hall.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), 120);
        sql2oFilmSessionRepository.create(filmSession);
        assertThat(sql2oFilmSessionRepository.findById(filmSession.getId()).get()).isEqualTo(filmSession);
    }

    @Test
    public void whenDontSaveFilmSessionAndUpdateThenReturnFalse() {
        Genre genre = new Genre(1, "horror");
        sql2oGenreRepository.create(genre);
        File file = new File("name", "path/to/file");
        sql2oFileRepository.create(file);
        Film film = new Film(1, "Jonh", "Good film", 2019, genre.getId(), 18, 120, file.getId());
        sql2oFilmRepository.create(film);
        Hall hall = new Hall(1, "big", 10, 10, "big hall");
        sql2oHallRepository.create(hall);
        FilmSession filmSession = new FilmSession(1, film.getId(), hall.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), 120);
        assertThat(sql2oFilmSessionRepository.update(filmSession)).isFalse();
    }

    @Test
    public void whenSaveFilmSessionAndUpdateThenReturnTrue() {
        Genre genre = new Genre(1, "horror");
        sql2oGenreRepository.create(genre);
        File file = new File("name", "path/to/file");
        sql2oFileRepository.create(file);
        Film film = new Film(1, "Jonh", "Good film", 2019, genre.getId(), 18, 120, file.getId());
        sql2oFilmRepository.create(film);
        Hall hall = new Hall(1, "big", 10, 10, "big hall");
        sql2oHallRepository.create(hall);
        FilmSession filmSession = new FilmSession(1, film.getId(), hall.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), 120);
        sql2oFilmSessionRepository.create(filmSession);
        assertThat(sql2oFilmSessionRepository.update(filmSession)).isTrue();
    }

    @Test
    public void whenDontSaveFilmSessionAndDeleteThenReturnFalse() {
        Genre genre = new Genre(1, "horror");
        sql2oGenreRepository.create(genre);
        File file = new File("name", "path/to/file");
        sql2oFileRepository.create(file);
        Film film = new Film(1, "Jonh", "Good film", 2019, genre.getId(), 18, 120, file.getId());
        sql2oFilmRepository.create(film);
        Hall hall = new Hall(1, "big", 10, 10, "big hall");
        sql2oHallRepository.create(hall);
        FilmSession filmSession = new FilmSession(1, film.getId(), hall.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), 120);
        assertThat(sql2oFilmSessionRepository.deleteById(filmSession.getId())).isFalse();
    }

    @Test
    public void whenSaveFilmSessionAndDeleteThenReturnTrue() {
        Genre genre = new Genre(1, "horror");
        sql2oGenreRepository.create(genre);
        File file = new File("name", "path/to/file");
        sql2oFileRepository.create(file);
        Film film = new Film(1, "Jonh", "Good film", 2019, genre.getId(), 18, 120, file.getId());
        sql2oFilmRepository.create(film);
        Hall hall = new Hall(1, "big", 10, 10, "big hall");
        sql2oHallRepository.create(hall);
        FilmSession filmSession = new FilmSession(1, film.getId(), hall.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), 120);
        sql2oFilmSessionRepository.create(filmSession);
        assertThat(sql2oFilmSessionRepository.deleteById(filmSession.getId())).isTrue();
    }

    @Test
    public void whenDontSaveFilmSessionAndFindAllThenReturnEmptyCollection() {
        assertThat(sql2oFilmSessionRepository.findAll()).isEmpty();
    }

    @Test
    public void whenSaveTwoFilmSessionsAndFindAllThenReturnCollectionThatContainsTwoSavedFilmSessions() {
        Genre genre = new Genre(1, "horror");
        sql2oGenreRepository.create(genre);
        File file = new File("name", "path/to/file");
        sql2oFileRepository.create(file);
        Film film = new Film(1, "Jonh", "Good film", 2019, genre.getId(), 18, 120, file.getId());
        sql2oFilmRepository.create(film);
        Hall hall = new Hall(1, "big", 10, 10, "big hall");
        sql2oHallRepository.create(hall);
        FilmSession filmSession1 = new FilmSession(1, film.getId(), hall.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), 120);
        FilmSession filmSession2 = new FilmSession(1, film.getId(), hall.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), 120);
        sql2oFilmSessionRepository.create(filmSession1);
        sql2oFilmSessionRepository.create(filmSession2);
        assertThat(sql2oFilmSessionRepository.findAll())
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(filmSession1, filmSession2);
    }
}