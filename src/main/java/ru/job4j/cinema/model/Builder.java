package ru.job4j.cinema.model;

public interface Builder {

    void setId(int id);

    void setName(String name);

    void setDescription(String description);

    void setYear(int year);

    void setGenreId(int genreId);

    void setMinimalAge(int minimalAge);

    void setDurationInMinutes(int durationInMinutes);

    void setFileId(int fileId);

    Film build();
}
