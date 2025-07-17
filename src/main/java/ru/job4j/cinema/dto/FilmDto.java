package ru.job4j.cinema.dto;

public class FilmDto {

    private int id;
    private String name;
    private int year;
    private String genre;
    private String pathToFile;
    private String description;

    public FilmDto(String name, int year, String genre, String pathToFile, String description) {
        this.name = name;
        this.year = year;
        this.genre = genre;
        this.pathToFile = pathToFile;
        this.description = description;
    }

    public FilmDto(int id, String name, String genre, String pathToFile, String description) {
        this.id = id;
        this.name = name;
        this.genre = genre;
        this.pathToFile = pathToFile;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getPathToFile() {
        return pathToFile;
    }

    public String getDescription() {
        return description;
    }

    public int getYear() {
        return year;
    }
}
