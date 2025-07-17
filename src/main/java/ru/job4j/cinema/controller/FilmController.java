package ru.job4j.cinema.controller;

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

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public String getFilms(Model model) {
        Collection<FilmDto> filmDtos = filmService.getAll();
        model.addAttribute("films", filmDtos);
        return "films/list";
    }

    @GetMapping("/{id}")
    public String getFilm(Model model, @PathVariable("id") int id) {
        Optional<FilmDto> filmDto = filmService.getOne(id);
        if (filmDto.isEmpty()) {
            model.addAttribute("message", "Такой фильм не найден");
            return "errors/404";
        }
        model.addAttribute("film", filmDto.get());
        return "films/one";
    }
}
