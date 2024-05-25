package com.eleven.MongoConnApp.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


/*
Entity class that represents the schema for the store_catalogsv2 collection
Contains only required attributes from the documents
 */
@Getter
@NoArgsConstructor
@Document("stores_catalogs_v2")
public class StoreCatalogv2 {
    @Id
    private String _id;
    private Store store;
}
