package ru.job4j.cinema.repository;

import org.junit.jupiter.api.*;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.*;

import javax.sql.DataSource;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2OTicketRepositoryTest {

    private static Sql2OTicketRepository sql2OTicketRepository;
    private static Sql2oUserRepository sql2oUserRepository;
    private static Sql2oFilmSessionRepository sql2oFilmSessionRepository;
    private static Sql2oFilmRepository sql2oFilmRepository;
    private static Sql2oHallsRepository sql2oHallsRepository;
    private static Sql2oGenreRepository sql2oGenreRepository;
    private static Sql2oFileRepository sql2oFileRepository;
    private static Genre genre;
    private static File file;
    private static Hall hall;
    private static Film film;
    private static User user1;
    private static User user2;
    private static FilmSession filmSession1;
    private static FilmSession filmSession2;

    @BeforeAll
    public static void initRepository() {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2OTicketRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");
        var configuration = new DatasourceConfiguration();
        DataSource dataSource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(dataSource);
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
        sql2OTicketRepository = new Sql2OTicketRepository(sql2o);
        sql2oFilmRepository = new Sql2oFilmRepository(sql2o);
        sql2oHallsRepository = new Sql2oHallsRepository(sql2o);
        sql2oFilmSessionRepository = new Sql2oFilmSessionRepository(sql2o);
        sql2oUserRepository = new Sql2oUserRepository(sql2o);
        genre = new Genre("action");
        sql2oGenreRepository.save(genre);
        file = new File("file123", "path123");
        sql2oFileRepository.save(file);
        hall = new Hall("small", 30, 30, "descr");
        sql2oHallsRepository.save(hall);
        user1 = new User("alex123", "alex123@mail.ru", "123");
        user2 = new User("alex456", "alex456@mail.ru", "123");
        sql2oUserRepository.save(user1);
        film = new Film("name", "description", 2000, genre.getId(), 18, 120, file.getId());
        sql2oFilmRepository.save(film);
        filmSession1 = new FilmSession(film.getId(), hall.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(2), 120);
        filmSession2 = new FilmSession(film.getId(), hall.getId(), LocalDateTime.now(), LocalDateTime.now().plusHours(1), 120);
        sql2oFilmSessionRepository.save(filmSession1);
        sql2oFilmSessionRepository.save(filmSession2);
    }


    @AfterAll
    public static void cleanRepositories() {
        sql2oFilmSessionRepository.deleteById(filmSession1.getId());
        sql2oFilmSessionRepository.deleteById(filmSession2.getId());
        sql2oFilmRepository.deleteById(film.getId());
        sql2oGenreRepository.deleteById(genre.getId());
        sql2oFileRepository.deleteById(file.getId());
        sql2oHallsRepository.deleteById(hall.getId());
        sql2oUserRepository.deleteById(user1.getId());
        sql2oUserRepository.deleteById(user2.getId());
    }

    @AfterEach
    public void cleanTicketRepository() {
        for (Ticket ticket : sql2OTicketRepository.getAll()) {
            sql2OTicketRepository.deleteById(ticket.getId());
        }
    }

    @Test
    public void whenDontSaveTicketAndGetByIdThenReturnEmptyOptional() {
        assertThat(sql2OTicketRepository.getById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenDontSaveTicketsAndGetAllThenReturnEmptyCollection() {
        assertThat(sql2OTicketRepository.getAll()).isEmpty();
    }

    @Test
    public void whenSaveTicketAndGetByIdThenReturnSavedTicket() {
        Ticket ticket = new Ticket(filmSession1.getId(), 3, 3, user1.getId());
        sql2OTicketRepository.save(ticket);
        assertThat(sql2OTicketRepository.getById(ticket.getId()).get()).isEqualTo(ticket);
    }

    @Test
    public void whenSaveTicketsAndGetAllThenReturnCollectionWithSavedTickets() {
        Ticket ticket1 = new Ticket(filmSession1.getId(), 3, 3, user1.getId());
        Ticket ticket2 = new Ticket(filmSession2.getId(), 5, 5, user2.getId());
        sql2OTicketRepository.save(ticket1);
        sql2OTicketRepository.save(ticket2);
        Collection<Ticket> tickets = sql2OTicketRepository.getAll();
        assertThat(tickets).isNotEmpty().hasSize(2).contains(ticket1, ticket2);
    }

    @Test
    public void whenSaveTicketAndDeleteItThenReturnTrue() {
        Ticket ticket1 = new Ticket(filmSession1.getId(), 3, 3, user1.getId());
        sql2OTicketRepository.save(ticket1);
        boolean isDeleted = sql2OTicketRepository.deleteById(ticket1.getId());
        assertThat(isDeleted).isTrue();
    }
}