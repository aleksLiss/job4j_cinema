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

class Sql2oTicketRepositoryTest {

    private static Sql2oTicketRepository sql2oTicketRepository;
    private static Sql2oUserRepository sql2oUserRepository;
    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;
    private static Sql2oHallRepository sql2oHallRepository;
    private static Sql2oFilmRepository sql2oFilmRepository;
    private static Sql2oGenreRepository sql2oGenreRepository;
    private static Sql2oFileRepository sql2oFileRepository;


    @BeforeAll
    public static void initRepository() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oTicketRepositoryTest.class
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
        sql2oTicketRepository = new Sql2oTicketRepository(sql2o);
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
        sql2oHallRepository = new Sql2oHallRepository(sql2o);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @AfterEach
    public void cleanRepository() {
        Collection<Ticket> tickets = sql2oTicketRepository.findAll();
        for (Ticket ticket : tickets) {
            sql2oTicketRepository.deleteById(ticket.getId());
        }
        Collection<User> users = sql2oUserRepository.findAll();
        for (User user : users) {
            sql2oUserRepository.deleteById(user.getId());
        }
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
        for (File file : files) {
            sql2oFileRepository.deleteById(file.getId());
        }
    }

    @Test
    public void whenSaveTicketThenReturnOptionalWithSavedTicket() {
        User user = new User(1, "Aleks", "Al@gmail.com", "123");
        sql2oUserRepository.create(user);
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
        Ticket ticket = new Ticket(1, filmSession.getId(), 2, 2, user.getId());
        assertThat(sql2oTicketRepository.create(ticket).get()).isEqualTo(ticket);
    }

    @Test
    public void whenSaveTwoTicketsWithEqualSessionIdAndRowNumberAndPlaceNumberThenThrowException() {
        User user = new User(1, "Aleks", "Al@gmail.com", "123");
        sql2oUserRepository.create(user);
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
        Ticket ticket1 = new Ticket(1, filmSession.getId(), 2, 2, user.getId());
        Ticket ticket2 = new Ticket(1, filmSession.getId(), 2, 2, user.getId());
        sql2oTicketRepository.create(ticket1);
        assertThatThrownBy(() -> sql2oTicketRepository.create(ticket2));
    }

    @Test
    public void whenDontSaveTicketAndFindByIdThenReturnEmptyOptional() {
        assertThat(sql2oTicketRepository.findById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveTicketAndFindByIdThenReturnOptionalWithSavedTicket() {
        User user = new User(1, "Aleks", "Al@gmail.com", "123");
        sql2oUserRepository.create(user);
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
        Ticket ticket = new Ticket(1, filmSession.getId(), 2, 2, user.getId());
        sql2oTicketRepository.create(ticket);
        assertThat(sql2oTicketRepository.findById(ticket.getId()).get()).isEqualTo(ticket);
    }

    @Test
    public void whenDontSaveTicketAndUpdateThenReturnFalse() {
        User user = new User(1, "Aleks", "Al@gmail.com", "123");
        sql2oUserRepository.create(user);
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
        Ticket ticket = new Ticket(1, filmSession.getId(), 2, 2, user.getId());
        assertThat(sql2oTicketRepository.update(ticket)).isFalse();
    }

    @Test
    public void whenSaveTicketAndUpdateThenReturnTrue() {
        User user = new User(1, "Aleks", "Al@gmail.com", "123");
        sql2oUserRepository.create(user);
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
        Ticket ticket = new Ticket(1, filmSession.getId(), 2, 2, user.getId());
        sql2oTicketRepository.create(ticket);
        assertThat(sql2oTicketRepository.update(ticket)).isTrue();
    }

    @Test
    public void whenDontSaveTicketAndDeleteByIdThenReturnFalse() {
        assertThat(sql2oTicketRepository.deleteById(1)).isFalse();
    }

    @Test
    public void whenSaveTicketAndDeleteByIdThenReturnTrue() {
        User user = new User(1, "Aleks", "Al@gmail.com", "123");
        sql2oUserRepository.create(user);
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
        Ticket ticket = new Ticket(1, filmSession.getId(), 2, 2, user.getId());
        sql2oTicketRepository.create(ticket);
        assertThat(sql2oTicketRepository.deleteById(ticket.getId())).isTrue();
    }

    @Test
    public void whenDontSaveTicketsAndFindAllThenReturnEmptyCollection() {
        assertThat(sql2oTicketRepository.findAll()).isEmpty();
    }

    @Test
    public void whenSaveTicketsAndFindAllThenReturnSavedTickets() {
        User user = new User(1, "Aleks", "Al@gmail.com", "123");
        sql2oUserRepository.create(user);
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
        Ticket ticket1 = new Ticket(1, filmSession.getId(), 2, 2, user.getId());
        Ticket ticket2 = new Ticket(1, filmSession.getId(), 3, 2, user.getId());
        sql2oTicketRepository.create(ticket1);
        sql2oTicketRepository.create(ticket2);
        assertThat(sql2oTicketRepository.findAll())
                .isNotEmpty()
                .hasSize(2)
                .containsExactlyInAnyOrder(ticket1, ticket2);
    }
}