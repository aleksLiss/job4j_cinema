package service;

import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    Optional<User> create(User user) {
        return userRepository.create(user);
    };

    Optional<User> findById(int id) {
        return userRepository.findById(id);
    };

    Collection<User> findAll() {
        return userRepository.findAll();
    }

    boolean update(User user) {
        return userRepository.update(user);
    };

    boolean deleteById(int id) {
        return userRepository.deleteById(id);
    };

}
