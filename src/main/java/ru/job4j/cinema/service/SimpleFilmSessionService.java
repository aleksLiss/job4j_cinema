package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.FilmRepository;
import ru.job4j.cinema.repository.FilmSessionRepository;
import ru.job4j.cinema.repository.GenreRepository;
import ru.job4j.cinema.repository.HallRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@ThreadSafe
@Service
public class SimpleFilmSessionService implements FilmSessionService {

    private final GenreRepository genreRepository;
    private final FilmRepository filmRepository;
    private final HallRepository hallRepository;
    private final FilmSessionRepository filmSessionRepository;

    public SimpleFilmSessionService(GenreRepository genreRepository, FilmRepository filmRepository, HallRepository hallRepository, FilmSessionRepository filmSessionRepository) {
        this.genreRepository = genreRepository;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
        this.filmSessionRepository = filmSessionRepository;
    }

    @Override
    public Collection<FilmSessionDto> getAll() {
        List<FilmSessionDto> filmSessionDtos = new ArrayList<>();
        for (FilmSession filmSession : filmSessionRepository.getAll()) {
            Optional<Film> film = filmRepository.getById(filmSession.getFilmId());
            Optional<Hall> hall = hallRepository.getById(filmSession.getHallsId());
            Optional<Genre> genre = genreRepository.getById(film.get().getGenreId());
            FilmSessionDto filmSessionDto = new FilmSessionDto(film.get().getName(), genre.get().getName(), hall.get().getName(), filmSession.getStartTime());
            filmSessionDtos.add(filmSessionDto);
        }
        return filmSessionDtos;
    }

    @Override
    public Optional<FilmSessionDto> getOne() {
        Optional<FilmSession> filmSession = filmSessionRepository.getById(1);
        Optional<Film> film = filmRepository.getById(filmSession.get().getFilmId());
        Optional<Hall> hall = hallRepository.getById(filmSession.get().getHallsId());
        Optional<Genre> genre = genreRepository.getById(film.get().getGenreId());
        FilmSessionDto filmSessionDto = new FilmSessionDto(film.get().getName(), genre.get().getName(), hall.get().getName(), filmSession.get().getStartTime());
        return Optional.ofNullable(filmSessionDto);
    }
}
