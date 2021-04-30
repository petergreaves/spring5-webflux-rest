package com.springfirst.learning.rest.spring5webfluxrest.controllers;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Vendor;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.VendorRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class VendorController {

    public static final String BASE_URL = "/api/v1";
    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @GetMapping(BASE_URL + "/vendors")
    public Flux<Vendor> getVendors(){

        return vendorRepository.findAll();

    }

    @GetMapping(BASE_URL + "/vendors/{id}")
    public Mono<Vendor> getVendorByID(@PathVariable String id){

        return vendorRepository.findById(id);

    }
}
