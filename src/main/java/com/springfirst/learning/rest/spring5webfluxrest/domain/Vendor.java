package com.springfirst.learning.rest.spring5webfluxrest.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document
public class Vendor {

    @Id
    private String id;

    private String lastName;
    private String firstName;

}
