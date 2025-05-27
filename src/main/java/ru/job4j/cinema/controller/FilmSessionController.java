package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.FilmSessionTwoDto;
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
    private final UserController userController;
    private final TicketService ticketService;

    public FilmSessionController(FilmSessionService filmSessionService, UserController userController, TicketService ticketService) {
        this.filmSessionService = filmSessionService;
        this.userController = userController;
        this.ticketService = ticketService;
    }

    @GetMapping
    public String getFilmSessions(Model model, HttpSession session) {
        Collection<FilmSessionDto> filmSessionDtos = filmSessionService.getAll();
        userController.addUserToModel(session, model);
        model.addAttribute("sessions", filmSessionDtos);
        return "sessions/list";
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable("id") int id, HttpSession session) {
        Optional<FilmSessionTwoDto> filmSessionDtoOptional;
        try {
            filmSessionDtoOptional = filmSessionService.getById(id);
        } catch (Exception ex) {
            model.addAttribute("message", "Такой сессии не существует");
            return "errors/404";
        }
        userController.addUserToModel(session, model);
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
                            @RequestParam("seatPlace") int place,
                            @RequestParam("seat-row") int row,
                            HttpSession session) {
        model.addAttribute("id", id);
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/users/login";
        }
        System.out.println(place);
        System.out.println(row);
        return "redirect:/sessions";
    }
}
