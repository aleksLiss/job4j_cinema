package ru.job4j.cinema.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class FilmSessionDto {

    private int id;
    private String filmName;
    private String genre;
    private String hall;
    private LocalDateTime startTime;

    public FilmSessionDto(int id, String filmName, String genre, String hall, LocalDateTime startTime) {
        this.id = id;
        this.filmName = filmName;
        this.genre = genre;
        this.hall = hall;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStartTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM");
        return startTime.format(dateTimeFormatter);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FilmSessionDto that = (FilmSessionDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
