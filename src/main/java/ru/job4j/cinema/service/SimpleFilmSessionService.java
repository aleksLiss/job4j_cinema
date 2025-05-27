package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.dto.FilmSessionDto;
import ru.job4j.cinema.dto.FilmSessionTwoDto;
import ru.job4j.cinema.model.*;
import ru.job4j.cinema.repository.*;

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
    private final FileRepository fileRepository;

    public SimpleFilmSessionService(GenreRepository genreRepository, FilmRepository filmRepository, HallRepository hallRepository, FilmSessionRepository filmSessionRepository, FileRepository fileRepository) {
        this.genreRepository = genreRepository;
        this.filmRepository = filmRepository;
        this.hallRepository = hallRepository;
        this.filmSessionRepository = filmSessionRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public Collection<FilmSessionDto> getAll() {
        List<FilmSessionDto> filmSessionDtos = new ArrayList<>();
        for (FilmSession filmSession : filmSessionRepository.getAll()) {
            Optional<Film> film = filmRepository.getById(filmSession.getFilmId());
            Optional<Hall> hall = hallRepository.getById(filmSession.getHallsId());
            Optional<Genre> genre = genreRepository.getById(film.get().getGenreId());
            FilmSessionDto filmSessionDto = new FilmSessionDto(film.get().getId(), film.get().getName(), genre.get().getName(), hall.get().getName(), filmSession.getStartTime());
            filmSessionDtos.add(filmSessionDto);
        }
        return filmSessionDtos;
    }

    @Override
    public Optional<FilmSessionTwoDto> getById(int id) {
        Optional<FilmSession> filmSession = filmSessionRepository.getById(id);
        Optional<Film> film = filmRepository.getById(filmSession.get().getFilmId());
        Optional<Hall> hall = hallRepository.getById(filmSession.get().getHallsId());
        Optional<Genre> genre = genreRepository.getById(film.get().getGenreId());
        Optional<File> file = fileRepository.getById(film.get().getFileId());
        FilmSessionTwoDto filmSessionTwoDto = FilmSessionTwoDto.builder()
                .setFilmName(film.get().getName())
                .setDescription(film.get().getDescription())
                .setMinimalAge(film.get().getMinimalAge())
                .setHall(hall.get())
                .setGenre(genre.get().getName())
                .setDurationInMinutes(film.get().getDurationInMinutes())
                .setPathToFile(file.get().getPath())
                .setStartTime(filmSession.get().getStartTime());
        return Optional.ofNullable(filmSessionTwoDto);
    }
}
