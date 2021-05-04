package com.springfirst.learning.rest.spring5webfluxrest.controllers;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Category;
import com.springfirst.learning.rest.spring5webfluxrest.domain.Vendor;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.VendorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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

    @Test
    public void createVendor() {

        //given

        BDDMockito.given(vendorRepository.saveAll(Mockito.any(Publisher.class)))
                .willReturn(Flux.just(Vendor.builder().firstName("fooCreated").lastName("barCreated").build()));

        Mono<Vendor> vendorToSave = Mono.just(Vendor.builder().firstName("foo").lastName("bar").build());

        webTestClient.post()
                .uri(VendorController.BASE_URL + "/vendors")
                .body(vendorToSave, Vendor.class)
                .exchange()
                .expectStatus()
                .isCreated();


    }

    @Test
    public void updateVendor() {

        //given

        BDDMockito.given(vendorRepository.save(Mockito.any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> vendorToUpdate = Mono.just(Vendor.builder().firstName("foo").lastName("bar").build());

        webTestClient.put()
                .uri(VendorController.BASE_URL + "/vendors/123")
                .body(vendorToUpdate, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk()
              ;

    }

    @Test
    public void testPatchWithFirstNameChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("Joe").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToUpdateMono = Mono.just(Vendor.builder().firstName("john").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/123")
                .body(venToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }

    @Test
    public void testPatchWithLastNameChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().lastName("smith").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToUpdateMono = Mono.just(Vendor.builder().lastName("jones").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/123")
                .body(venToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }
    @Test
    public void testPatchWithAllNameChanges() {
        given(vendorRepository.findById(anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("joe").lastName("Smith").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> venToUpdateMono = Mono.just(Vendor.builder().firstName("fred").lastName("jones").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/123")
                .body(venToUpdateMono, Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository).save(any());
    }

    @Test
    public void testPatchNoChanges() {
        given(vendorRepository.findById(Mockito.anyString()))
                .willReturn(Mono.just(Vendor.builder().firstName("same").build()));

        given(vendorRepository.save(any(Vendor.class)))
                .willReturn(Mono.just(Vendor.builder().build()));

        Mono<Vendor> catToUpdateMono = Mono.just(Vendor.builder().firstName("same").build());

        webTestClient.patch()
                .uri("/api/v1/vendors/123")
                .body(catToUpdateMono, Category.class)
                .exchange()
                .expectStatus()
                .isOk();

        verify(vendorRepository, never()).save(any());
    }


}