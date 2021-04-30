package com.springfirst.learning.rest.spring5webfluxrest.repositories;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Vendor;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface VendorRepository extends ReactiveMongoRepository<Vendor, String> {
}
