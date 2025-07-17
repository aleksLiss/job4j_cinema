package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.format.CustomDateTimeFormatter;
import ru.job4j.cinema.model.*;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.SimpleFilmSessionService;
import ru.job4j.cinema.service.TicketService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FilmSessionControllerTest {

    private FilmSessionController filmSessionController;
    private FilmSessionService filmSessionService;
    private TicketService ticketService;
    private CustomDateTimeFormatter customDateTimeFormatter;
    private static Film film;
    private static Hall hall;
    private static Genre genre;
    private static File testFile;
    private static HttpSession httpSession;

    @BeforeEach
    public void iniServices() {
        customDateTimeFormatter = mock(CustomDateTimeFormatter.class);
        ticketService = mock(TicketService.class);
        filmSessionService = mock(SimpleFilmSessionService.class);
        filmSessionController = new FilmSessionController(filmSessionService, ticketService, customDateTimeFormatter);
    }

    @BeforeAll
    public static void init() {
        film = mock(Film.class);
        hall = mock(Hall.class);
        genre = mock(Genre.class);
        testFile = new File("file", "path/to/file");
        httpSession = new MockHttpSession();
    }

    @Test
    public void whenGetFilmSessionsThenReturnSavedFilmSessions() {
        FilmSessionDto filmSessionDto1 = FilmSessionDto.builder()
                .setFilmName(film.getName())
                .setHall(hall)
                .setGenre(genre.getName())
                .setPathToFile(testFile.getPath());
        FilmSessionDto filmSessionDto2 = FilmSessionDto.builder()
                .setFilmName(film.getName())
                .setHall(hall)
                .setGenre(genre.getName());
        FilmSessionDto filmSessionDto3 = FilmSessionDto.builder()
                .setFilmName(film.getName())
                .setHall(hall);
        Collection<FilmSessionDto> expectedFilmSessionsList = List.of(
                filmSessionDto1, filmSessionDto2, filmSessionDto3
        );
        when(filmSessionService.getAll()).thenReturn(expectedFilmSessionsList);
        var model = new ConcurrentModel();
        var view = filmSessionController.getFilmSessions(model);
        var actualFilmSessionsList = model.getAttribute("sessions");
        assertThat(view).isEqualTo("sessions/list");
        assertThat(actualFilmSessionsList).isEqualTo(expectedFilmSessionsList);
    }

    @Test
    public void whenGetFilmSessionByIdThenReturnSavedFilmSessionWithInputId() {
        Optional<FilmSessionDto> savedFilmSessionDto = Optional.ofNullable(FilmSessionDto.builder()
                .setFilmName(film.getName())
                .setHall(hall)
                .setGenre(genre.getName()));
        int id = 1;
        when(filmSessionService.getById(id)).thenReturn(savedFilmSessionDto);
        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, id);
        var actualSavedFilmSession = model.getAttribute("ses");
        assertThat(view).isEqualTo("sessions/one");
        assertThat(actualSavedFilmSession).isEqualTo(savedFilmSessionDto.get());
    }

    @Test
    public void whenDontSaveFilmSessionAndGetItByIdThenReturnError() {
        var expectedException = new RuntimeException("Такой сессии не существует");
        when(filmSessionService.getById(1)).thenThrow(expectedException);
        var model = new ConcurrentModel();
        var view = filmSessionController.getById(model, 1);
        var actualException = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(expectedException.getMessage()).isEqualTo(actualException);
    }

    @Test
    public void whenGetTicketByIdThenOk() {
        int id = 123;
        User user = new User("aleks", "email", "psw");
        Ticket ticket = new Ticket(1, 1, 1, 1);
        httpSession.setAttribute("user", user);
        httpSession.setAttribute("id", id);
        FilmSessionDto filmSessionDto = FilmSessionDto.builder()
                .setFilmName("film")
                .setHall(hall)
                .setStartTime(LocalDateTime.now().minusHours(1))
                .setGenre(genre.getName());
        when(ticketService.getAll()).thenReturn(List.of());
        when(ticketService.save(ticket)).thenReturn(Optional.of(ticket));
        when(filmSessionService.getById(1)).thenReturn(Optional.of(filmSessionDto));
        var model = new ConcurrentModel();
        var view = filmSessionController.getTicket(model, 1, ticket, httpSession);
        assertThat(view).isEqualTo("tickets/one");
    }

    @Test
    public void wheGetTicketAndFailItThenRedirectToRegisterUser() {
        Optional<Ticket> ticket = Optional.of(new Ticket(1, 1, 1, 1));
        when(filmSessionService.getById(1)).thenReturn(Optional.empty());
        var model = new ConcurrentModel();
        var view = filmSessionController.getTicket(model, 1, ticket.get(), httpSession);
        assertThat(view).isEqualTo("redirect:/users/login");
    }

    @Test
    public void wheGetTicketAndFailItThenThrowError() {
        var expectedException = new RuntimeException(
                "Не удалось приобрести билет на заданное место. Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова."
        );
        Optional<User> user = Optional.of(new User("aleks", "email", "psw"));
        Optional<Ticket> ticket = Optional.of(new Ticket(1, 1, 1, 1));
        httpSession.setAttribute("user", user.get());
        Collection<Ticket> tickets = List.of(ticket.get());
        when(ticketService.getAll()).thenReturn(tickets);
        var model = new ConcurrentModel();
        var view = filmSessionController.getTicket(model, 1, ticket.get(), httpSession);
        assertThat(view).isEqualTo("errors/401");
        assertThat(model.getAttribute("message")).isEqualTo(expectedException.getMessage());
    }
}