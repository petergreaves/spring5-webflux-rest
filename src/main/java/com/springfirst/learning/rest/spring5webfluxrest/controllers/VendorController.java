package com.springfirst.learning.rest.spring5webfluxrest.controllers;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Category;
import com.springfirst.learning.rest.spring5webfluxrest.domain.Vendor;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public Flux<Vendor> getVendors() {

        return vendorRepository.findAll();

    }

    @GetMapping(BASE_URL + "/vendors/{id}")
    public Mono<Vendor> getVendorByID(@PathVariable String id) {

        return vendorRepository.findById(id);

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(BASE_URL + "/vendors")
    public Mono<Void> createVendor(@RequestBody Publisher<Vendor> vendorStream) {

        return vendorRepository.saveAll(vendorStream).then();


    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(BASE_URL + "/vendors/{id}")
    public Mono<Void> updateVendor(@PathVariable String id, @RequestBody Vendor vendor) {
        return vendorRepository.save(vendor).then();
    }

    @ResponseStatus(HttpStatus.OK)
    @PatchMapping(BASE_URL + "/vendors/{id}")
    public Mono<Vendor> patch(@PathVariable String id, @RequestBody Vendor vendor) {

        return vendorRepository.findById(id).
                map(v -> {
                    boolean hasChanged = false;
                    if (v.getFirstName()!=null && !(v.getFirstName().equals(vendor.getFirstName()))) {
                        v.setFirstName(vendor.getFirstName());
                        hasChanged = true;
                    }
                    if (v.getLastName()!=null &&!(v.getLastName().equals(vendor.getLastName()))) {
                        v.setLastName(vendor.getLastName());
                        hasChanged = true;
                    }

                    if (hasChanged) {
                        vendorRepository.save(v);
                    }
                    return v;
                });
    }

}
