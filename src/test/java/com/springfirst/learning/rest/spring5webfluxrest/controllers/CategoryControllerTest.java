package com.springfirst.learning.rest.spring5webfluxrest.controllers;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Category;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.CategoryRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryController categoryController;
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {

        categoryRepository = Mockito.mock(CategoryRepository.class);
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build();
    }

    @Test
    public void getCategories() throws Exception {

        BDDMockito.given(categoryRepository.findAll())
                .willReturn(Flux.fromArray(new Category[]{Category.builder().description("Cat1").build(), Category.builder().description("Cat2").build()}));

        webTestClient.get()
                .uri(CategoryController.BASE_URL + "/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);

    }

    @Test
    public void getCategoryByID() throws Exception {

        BDDMockito.given(categoryRepository.findById(Mockito.anyString()))
                .willReturn(Mono.just(Category.builder().description("Cat1").id("abc").build()));

        webTestClient.get()
                .uri(CategoryController.BASE_URL + "/categories/a")
                .exchange()
                .expectBody()
                .jsonPath("$.id").isEqualTo("abc")
                .jsonPath("$.description").isEqualTo("Cat1");

    }
}