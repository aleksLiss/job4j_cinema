package ru.job4j.cinema.dto;

import ru.job4j.cinema.model.Hall;

import java.time.LocalDateTime;

public class FilmSessionDto implements DtoBuilder {

    private int id;
    private String filmName;
    private String description;
    private String genre;
    private int minimalAge;
    private int durationInMinutes;
    private int fileId;
    private Hall hall;
    private LocalDateTime startTime;
    private String pathToFile;

    public FilmSessionDto() {
    }

    public int getId() {
        return id;
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
    public FilmSessionDto setId(int id) {
        this.id = id;
        return this;
    }

    @Override
    public FilmSessionDto setFilmName(String filmName) {
        this.filmName = filmName;
        return this;
    }

    @Override
    public FilmSessionDto setDescription(String description) {
        this.description = description;
        return this;
    }

    @Override
    public FilmSessionDto setGenre(String genre) {
        this.genre = genre;
        return this;
    }

    @Override
    public FilmSessionDto setMinimalAge(int minimalAge) {
        this.minimalAge = minimalAge;
        return this;
    }

    @Override
    public FilmSessionDto setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
        return this;
    }

    @Override
    public FilmSessionDto setFileId(int fileId) {
        this.fileId = fileId;
        return this;
    }

    @Override
    public FilmSessionDto setHall(Hall hall) {
        this.hall = hall;
        return this;
    }

    @Override
    public FilmSessionDto setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
        return this;
    }

    @Override
    public FilmSessionDto setPathToFile(String pathToFile) {
        this.pathToFile = pathToFile;
        return this;
    }

    public static FilmSessionDto builder() {
        return new FilmSessionDto();
    }

}
