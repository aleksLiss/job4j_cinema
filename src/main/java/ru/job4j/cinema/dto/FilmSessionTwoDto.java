package ru.job4j.cinema.dto;

import ru.job4j.cinema.model.Hall;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FilmSessionTwoDto implements DtoBuilder {

    private String filmName;
    private String description;
    private String genre;
    private int minimalAge;
    private int durationInMinutes;
    private int fileId;
    private Hall hall;
    private LocalDateTime startTime;
    private String pathToFile;

    public FilmSessionTwoDto() {
    }

    public String getFilmName() {
        return filmName;
    }

    public String getDescription() {
        return description;
    }

    public String getGenre() {
        return genre;
    }

    public int getMinimalAge() {
        return minimalAge;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public int getFileId() {
        return fileId;
    }

    public Hall getHall() {
        return hall;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    @Override
    public FilmSessionTwoDto setFilmName(String filmName) {
        this.filmName = filmName;
        return this;
    }

    @Override
    public FilmSessionTwoDto setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilmSessionTwoDto setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    @Override
    public FilmSessionTwoDto setMinimalAge(int minimalAge) {
        this.minimalAge = minimalAge;
        return this;
    }

    @Override
    public FilmSessionTwoDto setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    @Override
    public FilmSessionTwoDto setFileId(int fileId) {
        this.fileId = fileId;
        return this;
    }

    @Override
    public FilmSessionTwoDto setHall(Hall hall) {
        this.hall = hall;
        return this;
    }

    @Override
    public FilmSessionTwoDto setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public FilmSessionTwoDto setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
        return this;
    }

    public static FilmSessionTwoDto builder() {
        return new FilmSessionTwoDto();
    }

}
