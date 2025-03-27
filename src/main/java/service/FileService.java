package service;

import ru.job4j.cinema.model.File;
import ru.job4j.cinema.repository.FileRepository;

import java.util.Collection;
import java.util.Optional;

public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }
    Optional<File> create(File file) {
        return fileRepository.create(file);
    };

    Optional<File> findById(int id) {
        return fileRepository.findById(id);
    };

    Collection<File> findAll() {
        return fileRepository.findAll();
    };

    boolean deleteById(int id) {
        return fileRepository.deleteById(id);
    };

    boolean update(File file) {
        return fileRepository.update(file);
    };
}
