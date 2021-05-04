package com.springfirst.learning.rest.spring5webfluxrest.controllers;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Category;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class CategoryController {


    public static final String BASE_URL = "/api/v1";
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;

    }


    @GetMapping(BASE_URL + "/categories")
    public Flux<Category> getCategories() {

        return categoryRepository.findAll();
    }

    @GetMapping(BASE_URL + "/categories/{id}")
    Mono<Category> getCategoryByID(@PathVariable String id) {

        return categoryRepository.findById(id);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(BASE_URL + "/categories")
    public Mono<Void> createCategories(@RequestBody Publisher<Category> categoryStream) {
        return categoryRepository.saveAll(categoryStream).then();
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(BASE_URL + "/categories/{id}")
    public Mono<Category> update(@PathVariable String id, @RequestBody Category category) {
        category.setId(id);
        return categoryRepository.save(category);
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(BASE_URL + "/categories/{id}")
    public Mono<Category> patch(@PathVariable String id, @RequestBody Category category) {

        return categoryRepository.findById(id).
                map(cat -> {
                    if (!cat.getDescription().equals(category.getDescription())) {
                        cat.setDescription(cat.getDescription());
                        categoryRepository.save(cat);
                    }
                    return cat;
                });
    }


}
