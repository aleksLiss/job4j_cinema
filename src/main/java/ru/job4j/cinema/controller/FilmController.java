package ru.job4j.cinema.controller;

import jakarta.servlet.http.HttpSession;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Controller
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserController userController;

    public FilmController(FilmService filmService, UserController userController) {
        this.filmService = filmService;
        this.userController = userController;
    }

    @GetMapping
    public String getFilms(Model model, HttpSession session) {
        Collection<FilmDto> filmDtos = filmService.getAll();
        model.addAttribute("films", filmDtos);
        userController.addUserToModel(session, model);
        return "films/list";
    }

    @GetMapping("/{id}")
    public String getFilm(Model model, @PathVariable("id") int id) {
        Optional<FilmDto> filmDto;
        try {
            filmDto = filmService.getOne(id);
        } catch (Exception ex) {
            return "errors/404";
        }
        model.addAttribute("film", filmDto.get());
        return "films/one";
    }
}
