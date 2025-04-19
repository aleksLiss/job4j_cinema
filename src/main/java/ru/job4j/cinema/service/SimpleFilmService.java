package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FileRepository;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleFilmService implements FilmService {

    private final FilmRepository filmRepository;
    private final FileRepository fileRepository;
    private final GenreRepository genreRepository;

    public SimpleFilmService(FilmRepository filmRepository, FileRepository fileRepository, GenreRepository genreRepository) {
        this.filmRepository = filmRepository;
        this.fileRepository = fileRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Collection<FilmDto> getAll() {
        List<FilmDto> filmDtos = new ArrayList<>();
        for (Film film : filmRepository.getAll()) {
            Optional<Genre> genre = genreRepository.getById(filmRepository.getById(1).get().getGenreId());
            Optional<File> file = fileRepository.getById(filmRepository.getById(1).get().getFileId());
            FilmDto filmDto = new FilmDto(film.getName(), genre.get().getName(), file.get().getPath());
            filmDtos.add(filmDto);
        }
        return filmDtos;
    }

    @Override
    public Optional<FilmDto> getOne() {
        Optional<Genre> genre = genreRepository.getById(filmRepository.getById(1).get().getGenreId());
        Optional<File> file = fileRepository.getById(filmRepository.getById(1).get().getFileId());
        Optional<Film> film = filmRepository.getById(1);
        FilmDto filmDto = new FilmDto(film.get().getName(), genre.get().getName(), file.get().getPath());
        return Optional.ofNullable(filmDto);
    }
}
