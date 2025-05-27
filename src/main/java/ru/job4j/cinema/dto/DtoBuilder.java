package ru.job4j.cinema.dto;

import ru.job4j.cinema.model.Hall;

import java.time.LocalDateTime;

public interface DtoBuilder {

    FilmSessionTwoDto setFilmName(String filmName);

    FilmSessionTwoDto setDescription(String description);

    FilmSessionTwoDto setGenre(String genre);

    FilmSessionTwoDto setMinimalAge(int minimalAge);

    FilmSessionTwoDto setDurationInMinutes(int durationInMinutes);

    FilmSessionTwoDto setFileId(int fileId);

    FilmSessionTwoDto setHall(Hall hall);

    FilmSessionTwoDto setStartTime(LocalDateTime startTime);

    FilmSessionTwoDto setPathToFile(String pathToFile);
}
