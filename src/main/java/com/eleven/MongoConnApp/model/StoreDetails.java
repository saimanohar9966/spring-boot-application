package com.eleven.MongoConnApp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("store_details")
@Getter
@NoArgsConstructor
public class StoreDetails{
    @Id
    private String _id;
    private String name;
    private String city;
    private String state;
    private String zip;
    private String address;
    private Double lat;
    private Double lon;
}
