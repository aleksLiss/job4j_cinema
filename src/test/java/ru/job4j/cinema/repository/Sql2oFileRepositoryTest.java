package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
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
    public static void init() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oFileRepository.class
                .getClassLoader()
                .getResourceAsStream("connection.properties")) {
            properties.load(inputStream);
        }
        String url = properties.getProperty("datasource.url");
        String username = properties.getProperty("datasource.username");
        String password = properties.getProperty("datasource.password");
        DatasourceConfiguration datasourceConfiguration = new DatasourceConfiguration();
        DataSource dataSource = datasourceConfiguration.connectionPool(url, username, password);
        Sql2o sql2o = datasourceConfiguration.databaseClient(dataSource);
        sql2oFileRepository = new Sql2oFileRepository(sql2o);
    }

    @AfterEach
    public void clearRepository() {
        Collection<File> files = sql2oFileRepository.findAll();
        for (File file : files) {
            sql2oFileRepository.deleteById(file.getId());
        }
    }

    @Test
    public void whenSavedFileInEmptyRepositoryThenReturnSavedFile() {
        File file = new File(1, "name", "/path/to/file");
        Optional<File> result = sql2oFileRepository.create(file);
        assertThat(result).contains(file);
    }

    @Test
    public void whenSavedTwoFilesWithDifferentIdInEmptyRepositoryThenReturnSavedFile() {
        File file1 = new File(1, "name1", "/path/to/file1");
        File file2 = new File(2, "name2", "/path/to/file2");
        sql2oFileRepository.create(file1);
        Optional<File> result = sql2oFileRepository.create(file2);
        assertThat(result).contains(file2);
    }

    @Test
    public void whenSavedTwoFilesWithEqualsPathInEmptyRepositoryThenThrowException() {
        File file1 = new File(1, "name1", "/path/to/file1");
        File file2 = new File(1, "name2", "/path/to/file1");
        sql2oFileRepository.create(file1);
        assertThatThrownBy(() -> sql2oFileRepository.create(file2));
    }

    @Test
    public void whenDontSavedFileAndFindByIdThenReturnEmptyOptional() {
        assertThat(sql2oFileRepository.findById(1)).isEmpty();
    }

    @Test
    public void whenSavedFileAndFindByIdThenReturnOptionalWithSavedFile() {
        File file = new File(1, "name", "path/to/file");
        Optional<File> savedFile = sql2oFileRepository.create(file);
        assertThat(sql2oFileRepository.findById(savedFile.get().getId())).isEqualTo(savedFile);
    }

    @Test
    public void whenDontSavedFilesThenReturnEmptyCollection() {
        assertThat(sql2oFileRepository.findAll()).isEmpty();
    }

    @Test
    public void whenSavedThreeFilesThenReturnCollectionWithSavedFiles() {
        File file1 = new File(1, "name", "path/to/file1");
        File file2 = new File(2, "name", "path/to/file2");
        File file3 = new File(3, "name", "path/to/file3");
        sql2oFileRepository.create(file1);
        sql2oFileRepository.create(file2);
        sql2oFileRepository.create(file3);
        assertThat(sql2oFileRepository.findAll())
                .isNotEmpty()
                .hasSize(3)
                .containsExactlyInAnyOrder(file1, file2, file3);
    }

    @Test
    public void whenDontSavedFileAndDeleteByIdThenReturnFalse() {
        assertThat(sql2oFileRepository.deleteById(1)).isFalse();
    }

    @Test
    public void whenSavedFileAndDeleteByIdThenReturnTrue() {
        File file1 = new File(1, "name", "path/to/file1");
        Optional<File> savedFile = sql2oFileRepository.create(file1);
        assertThat(sql2oFileRepository.deleteById(savedFile.get().getId())).isTrue();
    }

    @Test
    public void whenDontSavedFileAndUpdateThenReturnFalse() {
        File file = new File(1, "name", "path/to/file");
        assertThat(sql2oFileRepository.update(file)).isFalse();
    }

    @Test
    public void whenSavedFileAndUpdateThenReturnTrue() {
        File file = new File(1, "name", "path/to/file");
        Optional<File> saved = sql2oFileRepository.create(file);
        assertThat(sql2oFileRepository.update(saved.get())).isTrue();
    }
}