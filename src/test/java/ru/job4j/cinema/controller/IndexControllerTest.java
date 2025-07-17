package ru.job4j.cinema.controller;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class IndexControllerTest {

    private static IndexController indexController;

    @BeforeAll
    public static void initController() {
        indexController = new IndexController();
    }

    @Test
    public void whenGetIndexPageThenReturnIndexPage() {
        var view = indexController.getIndex();
        assertThat(view).isEqualTo("index");
    }

}