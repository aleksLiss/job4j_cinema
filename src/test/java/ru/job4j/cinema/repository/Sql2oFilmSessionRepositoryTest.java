package ru.job4j.cinema.repository;

import org.junit.jupiter.api.*;
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
    private static Sql2oFilmRepository sql2oFilmRepository;
    private static Sql2oHallsRepository sql2oHallsRepository;
    private static Sql2oGenreRepository sql2oGenreRepository;
    private static Sql2oFileRepository sql2oFileRepository;
    private static Genre genre;
    private static File file;
    private static Hall hall;
    private static Film film;

    @BeforeAll
    public static void initRepository() {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFilmSessionRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");
        var configuration = new DatasourceConfiguration();
        DataSource dataSource = configuration.connectionPool(url, username, password);
        Sql2o sql2o = configuration.databaseClient(dataSource);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oHallsRepository = new Sql2oHallsRepository(sql2o);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
        genre = new Genre("horror");
        file = new File("file", "path");
        sql2oGenreRepository.save(genre);
        sql2oFileRepository.save(file);
        hall = new Hall("small", 30, 30, "descr");
        sql2oHallsRepository.save(hall);
        film = new Film("film name", "descr", 2000, genre.getId(), 18, 120, file.getId());
        sql2oFilmRepository.save(film);
    }

    @AfterAll
    public static void cleanRepositories() {
        sql2oFilmRepository.deleteById(film.getId());
        sql2oGenreRepository.deleteById(genre.getId());
        sql2oFileRepository.deleteById(file.getId());
        sql2oHallsRepository.deleteById(hall.getId());
    }

    @AfterEach
    public void cleanFilmSessionRepository() {
        for (FilmSession filmSession : sql2oFilmSessionRepository.getAll()) {
            sql2oFilmSessionRepository.deleteById(filmSession.getId());
        }
    }

    @Test
    public void whenDontSaveFilmSessionAndGetByIdThenReturnEmptyOptional() {
        assertThat(sql2oFilmSessionRepository.getById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenDontSaveFilmSessionsAndGetAllThenReturnEmptyCollection() {
        assertThat(sql2oFilmSessionRepository.getAll()).isEmpty();
    }

    @Test
    public void whenSaveFilmSessionAndGetByIdThenReturnSavedFilmSession() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now();
        FilmSession filmSession = new FilmSession(
                film.getId(),
                hall.getId(),
                startTime,
                endTime,
                20
        );
        sql2oFilmSessionRepository.save(filmSession);
        Optional<FilmSession> savedFilmSession = sql2oFilmSessionRepository.getById(filmSession.getId());
        assertThat(savedFilmSession.get()).isEqualTo(filmSession);
    }

    @Test
    public void whenSaveFilmSessionsAndGetAllThenReturnCollectionWithSavedFilmSessions() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now();
        FilmSession filmSession1 = new FilmSession(
                film.getId(),
                hall.getId(),
                startTime,
                endTime,
                20
        );
        FilmSession filmSession2 = new FilmSession(
                film.getId(),
                hall.getId(),
                startTime,
                endTime,
                200
        );
        sql2oFilmSessionRepository.save(filmSession1);
        sql2oFilmSessionRepository.save(filmSession2);
        Collection<FilmSession> filmSessions = sql2oFilmSessionRepository.getAll();
        assertThat(filmSessions).isNotEmpty().hasSize(2).contains(filmSession1, filmSession2);
    }

    @Test
    public void whenSaveFilmSessionAndUpdateItThenReturnUpdatedFilmSession() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now();
        FilmSession filmSession1 = new FilmSession(
                film.getId(),
                hall.getId(),
                startTime,
                endTime,
                20
        );
        sql2oFilmSessionRepository.save(filmSession1);
        filmSession1.setPrice(100);
        boolean isUpdated = sql2oFilmSessionRepository.update(filmSession1);
        assertThat(isUpdated).isTrue();
    }

    @Test
    public void whenSaveFilmSessionAndDeleteItThenReturnEmptyOptional() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(2);
        LocalDateTime endTime = LocalDateTime.now();
        FilmSession filmSession1 = new FilmSession(
                film.getId(),
                hall.getId(),
                startTime,
                endTime,
                20
        );
        sql2oFilmSessionRepository.save(filmSession1);
        boolean isDeleted = sql2oFilmSessionRepository.deleteById(filmSession1.getId());
        assertThat(isDeleted).isTrue();
    }
}