package ru.job4j.cinema.model;

public interface Builder {

    Builder setId(int id);
    Builder setName(String name);
    Builder setDescription(String description);
    Builder setYear(int year);
    Builder setGenreId(int genreId);
    Builder setMinimalAge(int minimalAge);
    Builder setDurationInTime(int durationInTime);
    Builder setFileId(int fileId);
}
