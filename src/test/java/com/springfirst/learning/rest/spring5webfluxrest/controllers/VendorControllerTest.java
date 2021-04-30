package com.springfirst.learning.rest.spring5webfluxrest.controllers;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Vendor;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

class VendorControllerTest {


    private VendorController vendorController;
    private VendorRepository vendorRepository;
    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    public void getVendors() throws Exception {

        BDDMockito.given(vendorRepository.findAll()).willReturn(Flux.fromArray(
                new Vendor[]{ Vendor.builder().firstName("foo1").lastName("bar1").build(), Vendor.builder().firstName("foo2").lastName("bar2").build()}));

        webTestClient.get()
                .uri(VendorController.BASE_URL + "/vendors")
                .exchange()
                .expectBodyList(Vendor.class)
                .hasSize(2);
    }

    @Test
    public void getVendorByID() throws Exception {

        BDDMockito.given(vendorRepository.findById(Mockito.anyString())).willReturn(Mono.just(Vendor.builder().firstName("foo1").lastName("bar1").build()));

        webTestClient.get()
                .uri(VendorController.BASE_URL + "/vendors/abc")
                .exchange()
                .expectBody(Vendor.class);
    }
}