package com.eleven.MongoConnApp.service;

import com.eleven.MongoConnApp.model.GlobalCatalogs;
import com.eleven.MongoConnApp.model.StoreCatalogv2;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

// Business logic for querying the collections
@Service
public class GetCatalogs implements CatalogImpl{

    @Override
    public List<StoreCatalogv2> getCatalogsPaginated(MongoTemplate mongoTemplate, Integer pageSize, Long pageNo) {
        Query q = new Query().limit(pageSize).skip((pageSize*pageNo) - pageSize);
        return mongoTemplate.find(q, StoreCatalogv2.class).stream().toList();
    }

    @Override
    public List<GlobalCatalogs> getGlobalCatalogsPaginated(MongoTemplate mongoTemplate, Integer pageSize, Long pageNo) {
        Query q = new Query().limit(pageSize).skip((pageSize*pageNo) - pageSize);
        return mongoTemplate.find(q, GlobalCatalogs.class).stream().toList();
    }
}
