package ru.job4j.cinema.dto;

import java.time.LocalDateTime;

public class FilmSessionDto {

    private String filmName;
    private String genre;
    private String hall;
    private LocalDateTime startTime;

    public FilmSessionDto(String filmName, String genre, String hall, LocalDateTime startTime) {
        this.filmName = filmName;
        this.genre = genre;
        this.hall = hall;
        this.startTime = startTime;
    }

    public String getFilmName() {
        return filmName;
    }

    public String getGenre() {
        return genre;
    }

    public String getHall() {
        return hall;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }
}
