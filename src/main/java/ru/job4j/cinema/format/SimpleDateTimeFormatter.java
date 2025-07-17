package ru.job4j.cinema.format;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SimpleDateTimeFormatter implements CustomDateTimeFormatter {

    private static final String PATTERN = "dd MMMM yyyy 'года'";

    @Override
    public String format(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(PATTERN);
        return dateTime.format(formatter);
    }
}
