package ru.job4j.cinema.dto;

import ru.job4j.cinema.model.Hall;

import java.time.LocalDateTime;

public interface DtoBuilder {

    FilmSessionDto setId(int id);

    FilmSessionDto setFilmName(String filmName);

    FilmSessionDto setDescription(String description);

    FilmSessionDto setGenre(String genre);

    FilmSessionDto setMinimalAge(int minimalAge);

    FilmSessionDto setDurationInMinutes(int durationInMinutes);

    FilmSessionDto setFileId(int fileId);

    FilmSessionDto setHall(Hall hall);

    FilmSessionDto setStartTime(LocalDateTime startTime);

    FilmSessionDto setPathToFile(String pathToFile);
}
