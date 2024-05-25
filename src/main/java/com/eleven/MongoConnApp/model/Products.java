package com.eleven.MongoConnApp.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("products")
@Getter
@NoArgsConstructor
public class Products {
    @Id
    private String _id;
    private String id;
    private List<String> tags;
    private String category;
    private String subcategory;
    private String name;
    private String long_desc;
    private String brand;
    private String thumbnail;
}
