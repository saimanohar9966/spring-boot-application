package com.eleven.MongoConnApp.service;
import com.eleven.MongoConnApp.model.GlobalCatalogs;
import com.eleven.MongoConnApp.model.StoreCatalogv2;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;

// Interface that contains methods that fetches the documents from collections
interface CatalogImpl {
    public List<StoreCatalogv2> getCatalogsPaginated(MongoTemplate mongoTemplate, Integer pageSize, Long pageNo);
    public List<GlobalCatalogs> getGlobalCatalogsPaginated(MongoTemplate mongoTemplate, Integer pageSize, Long pageNo);
}

