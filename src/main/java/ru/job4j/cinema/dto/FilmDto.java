package ru.job4j.cinema.dto;

public class FilmDto {

    private String name;
    private String genre;
    private String pathToFile;

    public FilmDto(String name, String genre, String pathToFile) {
        this.name = name;
        this.genre = genre;
        this.pathToFile = pathToFile;
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
}
