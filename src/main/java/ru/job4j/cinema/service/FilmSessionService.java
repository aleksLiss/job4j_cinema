package ru.job4j.cinema.service;

import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.FilmSessionTwoDto;

import java.util.Collection;
import java.util.Optional;

public interface FilmSessionService {

    Collection<FilmSessionDto> getAll();

    Optional<FilmSessionTwoDto> getById(int id);
}
