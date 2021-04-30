package com.springfirst.learning.rest.spring5webfluxrest.bootstrap;

import com.springfirst.learning.rest.spring5webfluxrest.domain.Category;
import com.springfirst.learning.rest.spring5webfluxrest.domain.Vendor;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.CategoryRepository;
import com.springfirst.learning.rest.spring5webfluxrest.repositories.VendorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class Bootstrap implements CommandLineRunner {

    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    public Bootstrap(VendorRepository vendorRepository, CategoryRepository categoryRepository) {
        this.vendorRepository = vendorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

         vendorRepository.findAll().count().subscribe(k ->{
            if (k == 0){
                loadVendors();
                loadCategories();
            }
            else{
                log.info("{} Vendors already present in database.", k);
            }
        });

    }


    private void loadCategories(){

        Category fruits = Category.builder().description("Fruits").build();
        Category nuts = Category.builder().description("Nuts").build();
        Category dried = Category.builder().description("Dried").build();
        Category exotic = Category.builder().description("Exotic").build();
        Category fresh = Category.builder().description("Fresh").build();
        List<Category> cats = new ArrayList<>();
        cats.add(fruits);
        cats.add(nuts);
        cats.add(exotic);
        cats.add(dried);
        cats.add(fresh);
        categoryRepository.saveAll(cats).subscribe(c ->log.info("Cat created {}", c.getId()));
    }
    private void loadVendors(){

        Vendor vendor1 = Vendor.builder().firstName("WH").lastName("Smiths").build();
        Vendor vendor2 = Vendor.builder().firstName("Tim").lastName("Williams").build();

        List<Vendor> vendors = new ArrayList<>();
        vendors.add(vendor1);
        vendors.add(vendor2);
        vendorRepository.saveAll(vendors).subscribe(v ->log.info("Vendor created {}", v.getId()));




    }
}
