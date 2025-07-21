package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.TicketDto;
import ru.job4j.cinema.format.CustomDateTimeFormatter;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.FilmSessionService;
import ru.job4j.cinema.service.TicketService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Controller
@RequestMapping("/sessions")
public class FilmSessionController {

    private final FilmSessionService filmSessionService;
    private final TicketService ticketService;
    private final CustomDateTimeFormatter customDateTimeFormatter;

    public FilmSessionController(FilmSessionService filmSessionService, TicketService ticketService, CustomDateTimeFormatter customDateTimeFormatter) {
        this.filmSessionService = filmSessionService;
        this.ticketService = ticketService;
        this.customDateTimeFormatter = customDateTimeFormatter;
    }

    @GetMapping
    public String getFilmSessions(Model model) {
        Collection<FilmSessionDto> filmSessionDtos = filmSessionService.getAll();
        model.addAttribute("sessions", filmSessionDtos);
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable("id") int id) {
        Optional<FilmSessionDto> filmSessionDtoOptional;
        try {
            filmSessionDtoOptional = filmSessionService.getById(id);
        } catch (Exception ex) {
            model.addAttribute("message", "Такой сессии не существует");
            return "errors/404";
        }
        List<Integer> seats = new ArrayList<>();
        seats.add(filmSessionDtoOptional.get().getHall().getRowCount());
        seats.add(filmSessionDtoOptional.get().getHall().getPlaceCount());
        model.addAttribute("seats", seats);
        model.addAttribute("ses", filmSessionDtoOptional.get());
        model.addAttribute("id", id);
        return "sessions/one";
    }

    @PostMapping("/{id}")
    public String getTicket(Model model,
                            @PathVariable("id") int id,
                            @ModelAttribute("ticket") Ticket ticket,
                            HttpSession session) {
        model.addAttribute("id", id);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }
        Collection<Ticket> tickets = ticketService.getAll();
        ticket.setUserId(user.getId());
        for (Ticket tick : tickets) {
            if (isEqualTicket(ticket, tick)) {
                model.addAttribute("message", "Не удалось приобрести билет на заданное место. Вероятно оно уже занято. Перейдите на страницу бронирования билетов и попробуйте снова.");
                return "errors/401";
            }
        }
        Ticket savedTicket = ticketService.save(ticket).get();
        Optional<FilmSessionDto> filmSessionDto = filmSessionService.getById(savedTicket.getSessionId());
        TicketDto ticketDto = new TicketDto(
                filmSessionDto.get().getFilmName(),
                filmSessionDto.get().getHall().getName(),
                customDateTimeFormatter.format(filmSessionDto.get().getStartTime()),
                savedTicket.getRowNumber(),
                savedTicket.getPlaceNumber());
        model.addAttribute("ticket", ticketDto);
        return "tickets/one";
    }

    private static boolean isEqualTicket(Ticket oldTicket, Ticket newTicket) {
        return oldTicket.getPlaceNumber() == newTicket.getPlaceNumber()
                && oldTicket.getRowNumber() == newTicket.getRowNumber()
                && oldTicket.getSessionId() == newTicket.getSessionId();
    }
}
