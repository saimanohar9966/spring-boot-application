package com.eleven.MongoConnApp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;
/*
Entity class that represents the schema of global_catalogs collection in 7MarketPlace db
Contains only required attributes from the document
* */
@Getter
@NoArgsConstructor
@Document("global_catalogs")
public class GlobalCatalogs {
    @Id
    private String _id;
    private String productName;
    private String productDescription;
    private UUID brandId;
    private String brand;
    private UUID chainProductId;
    private String productImageUrl;
    private List<String> cuisines;
    private List<String> categoryNames;
    private Integer sortOrder;
}
