package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.File;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.Properties;
import static org.assertj.core.api.Assertions.*;

class Sql2oFileRepositoryTest {

    private static Sql2oFileRepository sql2oFileRepository;

    @BeforeAll
    public static void initRepository() {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFileRepositoryTest.class.getClassLoader().getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");
        var configuration = new DatasourceConfiguration();
        DataSource dataSource = configuration.connectionPool(url, username, password);
        var sql2o = configuration.databaseClient(dataSource);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @AfterEach
    public void cleanRepository() {
        for (File file : sql2oFileRepository.getAll()) {
            sql2oFileRepository.deleteById(file.getId());
        }
    }

    @Test
    public void whenSaveFileAndGetByIdThenReturnSaveFile() {
        File file = new File(1, "file", "path/to/file");
        Optional<File> savedFile = sql2oFileRepository.save(file);
        assertThat(sql2oFileRepository.getById(savedFile.get().getId()).get()).isEqualTo(file);
    }

    @Test
    public void whenSaveTwoFilesAndGetAllThenReturnCollectionsWithTwoFiles() {
        File file1 = new File(1, "file1", "path/to/file1");
        File file2 = new File(2, "file2", "path/to/file2");
        sql2oFileRepository.save(file1);
        sql2oFileRepository.save(file2);
        Collection<File> files = sql2oFileRepository.getAll();
        assertThat(files).isNotEmpty().hasSize(2).contains(file1, file2);
    }

    @Test
    public void whenDontSaveFileThenReturnEmptyOptional() {
        Optional<File> fileOptional = sql2oFileRepository.getById(1);
        assertThat(fileOptional).isEqualTo(Optional.empty());
    }

    @Test
    public void whenDontSaveFilesThenReturnEmptyCollection() {
        assertThat(sql2oFileRepository.getAll())
                .isEmpty();
    }

    @Test
    public void whenSaveFileAndUpdateItThenReturnUpdatedFile() {
        File file = new File(1, "file", "path/to/file");
        Optional<File> savedFile = sql2oFileRepository.save(file);
        file.setName("new file");
        sql2oFileRepository.update(file);
        Optional<File> foundFile = sql2oFileRepository.getById(savedFile.get().getId());
        assertThat(foundFile.get()).isEqualTo(file);
    }

    @Test
    public void whenSaveFileAndDeleteItThenReturnEmptyOptional() {
        File file = new File(1, "file", "path/to/file");
        Optional<File> savedFile = sql2oFileRepository.save(file);
        sql2oFileRepository.deleteById(savedFile.get().getId());
        assertThat(sql2oFileRepository.getById(savedFile.get().getId())).isEqualTo(Optional.empty());
    }
}