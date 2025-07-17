package ru.job4j.cinema.format;

import java.time.LocalDateTime;

public interface CustomDateTimeFormatter {

    public String format(LocalDateTime dateTime);
}
