package com.springfirst.learning.rest.spring5webfluxrest.controllers;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Category;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.CategoryRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import org.mockito.Mockito;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

        given(categoryRepository.findAll())
                .willReturn(Flux.fromArray(new Category[]{Category.builder().description("Cat1").build(), Category.builder().description("Cat2").build()}));

        webTestClient.get()
                .uri(CategoryController.BASE_URL + "/categories")
                .exchange()
                .expectBodyList(Category.class)
                .hasSize(2);

    }

    @Test
    public void getCategoryByID() throws Exception {

        given(categoryRepository.findById(Mockito.anyString()))
                .willReturn(Mono.just(Category.builder().description("Cat1").id("abc").build()));

        webTestClient.get()
                .uri(CategoryController.BASE_URL + "/categories/a")
                .exchange()
                .expectBody()
                .jsonPath("$.id").isEqualTo("abc")
                .jsonPath("$.description").isEqualTo("Cat1");

    }

    @Test
    public void createCategory() {

        //given

        given(categoryRepository.saveAll(any(Publisher.class)))
                .willReturn(Flux.just(Category.builder().build()));

        Mono<Category> catToSave = Mono.just(Category.builder().description("foo").build());

        webTestClient.post()
                .uri(CategoryController.BASE_URL + "/categories")
                .body(catToSave, Category.class)
                .exchange()
                .expectStatus()
                .isCreated();



    }

    @Test
    public void updateCategory() {

        //given

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdate = Mono.just(Category.builder().description("foo").build());
        String idToSave = "123";

        webTestClient.put()
                .uri(CategoryController.BASE_URL + "/categories/123")
                .body(catToUpdate, Category.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(Category.class);



    }

    @Test
    public void testPatchWithChanges() {
        given(categoryRepository.findById(anyString()))
                .willReturn(Mono.just(Category.builder().description("original description").build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("new Description").build());

        webTestClient.patch()
                .uri("/api/v1/categories/123")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository).save(any());
    }

    @Test
    public void testPatchNoChanges() {
        given(categoryRepository.findById(Mockito.anyString()))
                .willReturn(Mono.just(Category.builder().description("same").build()));

        given(categoryRepository.save(any(Category.class)))
                .willReturn(Mono.just(Category.builder().build()));

        Mono<Category> catToUpdateMono = Mono.just(Category.builder().description("same").build());

        webTestClient.patch()
                .uri("/api/v1/categories/asdfasdf")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(categoryRepository, never()).save(any());
    }
}