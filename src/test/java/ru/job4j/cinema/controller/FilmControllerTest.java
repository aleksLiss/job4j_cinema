package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.ConcurrentModel;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.service.FilmService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FilmControllerTest {

    private FilmService filmService;
    private FilmController filmController;


    @BeforeEach
    public void initServices() {
        filmService = mock(FilmService.class);
        filmController = new FilmController(filmService);
    }

    @Test
    public void whenGetAllFilmsThenReturnListThatContainsFilmsDto() {
        FilmDto filmDto1 = new FilmDto(1, "name1", "horror", "path1", "descr");
        FilmDto filmDto2 = new FilmDto(2, "name2", "comedy", "path2", "descr");
        FilmDto filmDto3 = new FilmDto(3, "name3", "action", "path3", "descr");
        List<FilmDto> expectedFilms = List.of(filmDto1, filmDto2, filmDto3);
        Mockito.when(filmService.getAll()).thenReturn(expectedFilms);
        var model = new ConcurrentModel();
        String view = filmController.getFilms(model);
        var actualFilms = model.getAttribute("films");
        assertThat(view).isEqualTo("films/list");
        assertThat(actualFilms).isEqualTo(expectedFilms);
    }

    @Test
    public void whenDontFoundFilmByIdThenThrowException() {
        var expectedException = new RuntimeException("Такой фильм не найден");
        when(filmService.getOne(0)).thenThrow(expectedException);
        var model = new ConcurrentModel();
        var view = filmController.getFilm(model, 1);
        var actualExceptionMessage = model.getAttribute("message");
        assertThat(view).isEqualTo("errors/404");
        assertThat(actualExceptionMessage).isEqualTo(expectedException.getMessage());
    }

    @Test
    public void whenFoundFilmThenReturnThisFilmDto() {
        FilmDto filmDto = new FilmDto(1, "film1", "horror", "/path/to/file", "descr");
        when(filmService.getOne(filmDto.getId())).thenReturn(Optional.of(filmDto));
        var model = new ConcurrentModel();
        var view = filmController.getFilm(model, filmDto.getId());
        var actualFilmDto = model.getAttribute("film");
        assertThat(view).isEqualTo("films/one");
        assertThat(actualFilmDto).isEqualTo(filmDto);
    }
}