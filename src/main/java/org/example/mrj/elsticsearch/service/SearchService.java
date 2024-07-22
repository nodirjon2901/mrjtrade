//package org.example.mrj.elsticsearch.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.mrj.elsticsearch.entity.Catalog;
//import org.example.mrj.elsticsearch.entity.Product;
//import org.example.mrj.elsticsearch.repository.CatalogElasticRepository;
//import org.example.mrj.elsticsearch.repository.ProductElasticRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class SearchService {
//
//    private final ProductElasticRepository productRepository;
//
//    private final CatalogElasticRepository catalogRepository;
//
//    public Map<String, Object> search(String query) {
//        Map<String, Object> searchResults = new HashMap<>();
//
//        List<Product> products = productRepository.findByNameContainingOrMainDescriptionContainingOrDescriptionContaining(query, query, query);
//        searchResults.put("products", products);
//
//        List<Catalog> catalogs = catalogRepository.findByNameContainingOrDescriptionContaining(query, query);
//        searchResults.put("catalogs", catalogs);
//
//        // Boshqa jadval va repositorylar uchun qidiruv qo'shish
//
//        return searchResults;
//    }
//
//}
