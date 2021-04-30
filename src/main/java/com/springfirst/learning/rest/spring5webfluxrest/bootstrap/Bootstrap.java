package com.springfirst.learning.rest.spring5webfluxrest.bootstrap;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Vendor;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final VendorRepository vendorRepository;

    public Bootstrap(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void run(String... args) throws Exception {

         vendorRepository.findAll().count().subscribe(k ->{
            if (k == 0){
                loadVendors();
            }
            else{
                log.info("{} Vendors already present in database.", k);
            }
        });

    }

    private void loadVendors(){

        String id = UUID.randomUUID().toString();

        Vendor vendor1 = Vendor.builder().id(id).firstName("WH").lastName("Smiths").build();
        Vendor vendor2 = Vendor.builder().id(id).firstName("Tim").lastName("Williams").build();
        vendorRepository.save(vendor1);
        vendorRepository.save(vendor2);

        log.info("Vendors loaded.");


    }
}
