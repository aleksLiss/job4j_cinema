package service;

import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Collection;
import java.util.Optional;

public class HallService {

    private final HallRepository hallRepository;

    public HallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    Optional<Hall> create(Hall hall) {
        return hallRepository.create(hall);
    };

    Optional<Hall> findById(int id) {
        return hallRepository.findById(id);
    };

    Collection<Hall> findAll() {
        return hallRepository.findAll();
    };

    boolean update(Hall hall) {
        return hallRepository.update(hall);
    };

    boolean deleteById(int id) {
        return hallRepository.deleteById(id);
    };
}
