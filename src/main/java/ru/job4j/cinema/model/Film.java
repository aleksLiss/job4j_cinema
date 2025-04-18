package ru.job4j.cinema.model;

import java.util.Objects;

public class Film implements Builder {

    private int id;
    private String name;
    private String description;
    private int year;
    private int genreId;
    private int minimalAge;
    private int durationInMinutes;
    private int fileId;

    public Film() {
    }

    public Film(String name, String description, int year, int genreId, int minimalAge, int durationInMinutes, int fileId) {
        this.name = name;
        this.description = description;
        this.year = year;
        this.genreId = genreId;
        this.minimalAge = minimalAge;
        this.durationInMinutes = durationInMinutes;
        this.fileId = fileId;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    @Override
    public void setMinimalAge(int minimalAge) {
        this.minimalAge = minimalAge;
    }

    @Override
    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }

    @Override
    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    @Override
    public Film build() {
        return new Film(name, description, year, genreId, minimalAge, durationInMinutes, fileId);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Film film = (Film) o;
        return id == film.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
