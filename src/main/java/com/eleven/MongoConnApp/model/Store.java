package com.eleven.MongoConnApp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

// Sub-schema for the store attribute in store_catalogv2 collection
@Getter
@NoArgsConstructor
public class Store {
    private Long sourceId;
    private String name;
    private String city;
    private String state;
    private String zip;
    private String streetAddress;
    private Location location;
    private UUID brandId;
    private String brand;
}

