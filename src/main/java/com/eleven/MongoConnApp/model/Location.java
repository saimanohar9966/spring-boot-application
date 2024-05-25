package com.eleven.MongoConnApp.model;


import lombok.Getter;
import lombok.NoArgsConstructor;


// Sub schema for the location attribute in store_catalogv2 collection
@Getter
@NoArgsConstructor
public class Location {
    private String lat;
    private String lon;
}
