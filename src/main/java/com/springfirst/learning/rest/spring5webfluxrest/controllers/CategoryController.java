package com.springfirst.learning.rest.spring5webfluxrest.controllers;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Category;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.CategoryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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
    public Flux<Category> getCategories(){

        return categoryRepository.findAll();
    }
    @GetMapping(BASE_URL + "/categories/{id}")
    Mono<Category> getCategoryByID(@PathVariable String id){

        return categoryRepository.findById(id);
    }

}
