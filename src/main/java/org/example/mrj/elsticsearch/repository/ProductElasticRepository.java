//package org.example.mrj.elsticsearch.repository;
//
//import org.example.mrj.elsticsearch.entity.Product;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//
//import java.util.List;
//
//public interface ProductElasticRepository extends ElasticsearchRepository<Product, Long> {
//
//    List<Product> findByNameContainingOrMainDescriptionContainingOrDescriptionContaining(String name, String mainDescription, String description);
//
//
//}
