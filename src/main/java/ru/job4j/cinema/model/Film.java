package ru.job4j.cinema.model;

public class Film implements Builder {

    private int id;
    private String name;
    private String description;
    private int year;
    private int genreId;
    private int minimalAge;
    private int durationInTime;
    private int fileId;
    private final Film film = new Film();

    public Film build() {
        return film;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }

    public int getGenreId() {
        return genreId;
    }

    public int getMinimalAge() {
        return minimalAge;
    }

    public int getDurationInTime() {
        return durationInTime;
    }

    public int getFileId() {
        return fileId;
    }

    @Override
    public Builder setId(int id) {
        film.fileId = id;
        return this;
    }

    @Override
    public Builder setName(String name) {
        film.name = name;
        return this;
    }

    @Override
    public Builder setDescription(String description) {
        film.description = description;
        return this;
    }

    @Override
    public Builder setYear(int year) {
        film.year = year;
        return this;
    }

    @Override
    public Builder setGenreId(int genreId) {
        film.genreId = genreId;
        return this;
    }

    @Override
    public Builder setMinimalAge(int minimalAge) {
        film.minimalAge = minimalAge;
        return this;
    }

    @Override
    public Builder setDurationInTime(int durationInTime) {
        film.durationInTime = durationInTime;
        return this;
    }

    @Override
    public Builder setFileId(int fileId) {
        film.fileId = fileId;
        return this;
    }
}