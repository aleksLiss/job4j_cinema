package service;

import ru.job4j.cinema.model.Genre;
import ru.job4j.cinema.repository.GenreRepository;

import java.util.Collection;
import java.util.Optional;

public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    Optional<Genre> create(Genre genre) {
        return genreRepository.create(genre);
    };

    Optional<Genre> findById(int id) {
        return genreRepository.findById(id);
    };

    Collection<Genre> findAll() {
        return genreRepository.findAll();
    };

    boolean update(Genre genre) {
        return genreRepository.update(genre);
    };

    boolean deleteById(int id) {
        return genreRepository.deleteById(id);
    };
}
