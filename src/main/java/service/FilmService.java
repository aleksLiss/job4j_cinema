package service;

import ru.job4j.cinema.model.Film;
import ru.job4j.cinema.repository.FilmRepository;

import java.util.Collection;
import java.util.Optional;

public class FilmService {

    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    Optional<Film> findById(int id) {
        return filmRepository.findById(id);
    };

    Collection<Film> findAll() {
        return filmRepository.findAll();
    };

    Optional<Film> create(Film film) {
        return filmRepository.create(film);
    };

    boolean deleteById(int id) {
        return filmRepository.deleteById(id);
    };

    boolean update(Film film) {
        return filmRepository.update(film);
    };
}
