package com.eleven.MongoConnApp.controller;

import com.eleven.MongoConnApp.model.GlobalCatalogs;
import com.eleven.MongoConnApp.model.Products;
import com.eleven.MongoConnApp.model.StoreCatalogv2;
import com.eleven.MongoConnApp.model.StoreDetails;
import com.eleven.MongoConnApp.service.GetCatalogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//Controller for checking the data fetched from the collections in 7MarketPlace db
@RestController()
public class MongoController {
    @Autowired
    @Qualifier("mongoTemplate7MP")
    MongoTemplate mongoTemplate7MP;

    @Autowired
    @Qualifier("mongoTemplate7NOW")
    MongoTemplate mongoTemplate7NOW;

    @Autowired
    GetCatalogs getCatalogs;

    @GetMapping("/")
    private String getIndex(){
        return "Index";
    }

    // For checking data fetched from the store_catalogsv2 collection
    @GetMapping("/test-store-catalogv2")
    private List<StoreCatalogv2> getStoreCatalogRecords(@RequestParam(required = false, defaultValue = "1") Long pageNo, @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        return getCatalogs.getCatalogsPaginated(mongoTemplate7MP, pageSize, pageNo);
    }

    // For checking data fetched from the global_catalogs collection
    @GetMapping("/test-global-catalogs")
    private List<GlobalCatalogs> getGlobalCatalogRecords(@RequestParam(required = false, defaultValue = "1") Long pageNo, @RequestParam(required = false, defaultValue = "10") Integer pageSize){
        return getCatalogs.getGlobalCatalogsPaginated(mongoTemplate7MP, pageSize, pageNo);
    }

    @GetMapping("/test-products")
    private List<Products> getProductsRecords(){
        return mongoTemplate7NOW.find(new Query().limit(10), Products.class).stream().toList();
    }

    @GetMapping("/test-store-details")
    private List<StoreDetails> getStoreDetailsRecords(){
        return mongoTemplate7NOW.find(new Query().limit(10), StoreDetails.class).stream().toList();
    }




}
