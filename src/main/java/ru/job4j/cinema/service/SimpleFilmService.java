package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmDto;
import ru.job4j.cinema.model.File;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.FileRepository;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
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
    private final FilmSessionRepository filmSessionRepository;

    public SimpleFilmService(FilmRepository filmRepository, FileRepository fileRepository, GenreRepository genreRepository, FilmSessionRepository filmSessionRepository) {
        this.filmRepository = filmRepository;
        this.fileRepository = fileRepository;
        this.genreRepository = genreRepository;
        this.filmSessionRepository = filmSessionRepository;
    }

    @Override
    public Collection<FilmDto> getAll() {
        List<FilmDto> filmDtos = new ArrayList<>();
        for (Film film : filmRepository.getAll()) {
            Optional<Genre> genre = genreRepository.getById(filmRepository.getById(film.getId()).get().getGenreId());
            Optional<File> file = fileRepository.getById(filmRepository.getById(film.getId()).get().getFileId());
            FilmDto filmDto = new FilmDto(film.getId(), film.getName(), genre.get().getName(), file.get().getPath(), film.getDescription());
            filmDtos.add(filmDto);
        }
        return filmDtos;
    }

    @Override
    public Optional<FilmDto> getOne(int id) {
        Optional<Genre> genre = genreRepository.getById(filmRepository.getById(id).get().getGenreId());
        Optional<File> file = fileRepository.getById(filmRepository.getById(id).get().getFileId());
        Optional<Film> film = filmRepository.getById(id);
        FilmDto filmDto = new FilmDto(film.get().getId(), film.get().getName(), genre.get().getName(), file.get().getPath(), film.get().getDescription());
        return Optional.ofNullable(filmDto);
    }
}
