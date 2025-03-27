package service;

import ru.job4j.cinema.model.FilmSession;
import ru.job4j.cinema.repository.FilmSessionRepository;

import java.util.Collection;
import java.util.Optional;

public class FilmSessionService {

    private final FilmSessionRepository filmSessionRepository;

    public FilmSessionService(FilmSessionRepository filmSessionRepository) {
        this.filmSessionRepository = filmSessionRepository;
    }

    Optional<FilmSession> create(FilmSession filmSession) {
        return filmSessionRepository.create(filmSession);
    };

    Optional<FilmSession> findById(int id) {
        return filmSessionRepository.findById(id);
    };


    Collection<FilmSession> findAll() {
        return filmSessionRepository.findAll();
    };

    boolean update(FilmSession filmSession) {
        return filmSessionRepository.update(filmSession);
    };

    boolean deleteById(int id) {
        return filmSessionRepository.deleteById(id);
    };

}
