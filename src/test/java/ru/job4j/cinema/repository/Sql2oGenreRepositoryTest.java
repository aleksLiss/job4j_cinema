package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.sql2o.Sql2o;
import ru.job4j.cinema.configuration.DatasourceConfiguration;
import ru.job4j.cinema.model.Genre;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

class Sql2oGenreRepositoryTest {

    private static Sql2oGenreRepository sql2oGenreRepository;

    @BeforeAll
    public static void initRepository() throws Exception {
        Properties properties = new Properties();
        try (InputStream inputStream = Sql2oGenreRepositoryTest.class
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
        sql2oGenreRepository = new Sql2oGenreRepository(sql2o);
    }

    @AfterEach
    public void clearRepository() {
        Collection<Genre> genres = sql2oGenreRepository.findAll();
        for (Genre genre : genres) {
            sql2oGenreRepository.deleteById(genre.getId());
        }
    }

    @Test
    public void whenCreateGenreInRepositoryThenReturnCreatedGenre() {
        Genre genre = new Genre(1, "horror");
        Optional<Genre> result = sql2oGenreRepository.create(genre);
        assertThat(result.get()).isEqualTo(genre);
    }

    @Test
    public void whenSaveTwoGenresInRepositoryThenReturnSecondGenre() {
        Genre genre1 = new Genre(1, "horror");
        Genre genre2 = new Genre(2, "comedy");
        sql2oGenreRepository.create(genre1);
        Optional<Genre> result = sql2oGenreRepository.create(genre2);
        assertThat(result.get()).isEqualTo(genre2);
    }

    @Test
    public void whenSaveTwoGenresWithEqualNameThenThrowException() {
        Genre genre1 = new Genre(1, "horror");
        Genre genre2 = new Genre(2, "horror");
        sql2oGenreRepository.create(genre1);
        assertThatThrownBy(() -> sql2oGenreRepository.create(genre2));
    }

    @Test
    public void whenDontSaveGenreAndUpdateItThenReturnFalse() {
        assertThat(sql2oGenreRepository.update(new Genre(1, "horror"))).isFalse();
    }

    @Test
    public void whenSaveGenreAndUpdateItThenReturnTrue() {
        Genre genre = new Genre(1, "horror");
        sql2oGenreRepository.create(genre);
        assertThat(sql2oGenreRepository.update(genre)).isTrue();
    }

    @Test
    public void whenDontSaveGenreAndDeleteByIdThenReturnFalse() {
        assertThat(sql2oGenreRepository.deleteById(1)).isFalse();
    }

    @Test
    public void whenSaveGenreAndDeleteByIdThenReturnTrue() {
        Genre genre = new Genre(1, "horror");
        sql2oGenreRepository.create(genre);
        assertThat(sql2oGenreRepository.deleteById(genre.getId())).isTrue();
    }

    @Test
    public void whenDontSaveGenreAndFindByIdThenReturnEmptyOptional() {
        assertThat(sql2oGenreRepository.findById(1)).isEqualTo(Optional.empty());
    }

    @Test
    public void whenSaveGenreAndFindByIdThenReturnSavedGenre() {
        Genre genre = new Genre(1, "horror");
        sql2oGenreRepository.create(genre);
        assertThat(genre).isEqualTo(sql2oGenreRepository.findById(genre.getId()).get());
    }

    @Test
    public void whenDontSaveGenreAndFindAllThenReturnEmptyCollection() {
        assertThat(sql2oGenreRepository.findAll()).isEqualTo(List.of());
    }

    @Test
    public void whenSaveThreeGenresAndFindAllThenReturnCollectionWithSavedGenres() {
        Genre genre1 = new Genre(1, "horror");
        Genre genre2 = new Genre(2, "comedy");
        Genre genre3 = new Genre(3, "fantasy");
        sql2oGenreRepository.create(genre1);
        sql2oGenreRepository.create(genre2);
        sql2oGenreRepository.create(genre3);
        assertThat(sql2oGenreRepository.findAll())
                .isNotEmpty()
                .hasSize(3)
                .containsExactlyInAnyOrder(genre1, genre2, genre3);
    }
}