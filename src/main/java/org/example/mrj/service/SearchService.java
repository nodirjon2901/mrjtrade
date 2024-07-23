//package org.example.mrj.service;
//
//import lombok.RequiredArgsConstructor;
//import org.example.mrj.domain.dto.ApiResponse;
//import org.example.mrj.domain.dto.search.*;
//import org.example.mrj.domain.entity.*;
//import org.example.mrj.repository.*;
//import org.example.mrj.util.SearchSpecification;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class SearchService {
//
//    private final CatalogRepository catalogRepository;
//
//    private final CategoryItemRepository categoryItemRepository;
//
//    private final ProductRepository productRepository;
//
//    private final NewRepository newRepository;
//
//    private final EventRepository eventRepository;
//
//    private final PartnerRepository partnerRepository;
//
//    private final SearchSpecification searchSpecification;
//
//    public ResponseEntity<ApiResponse<List<Object>>> search(String searchTerm) {
//        ApiResponse<List<Object>> response=new ApiResponse<>();
//        response.setData(new ArrayList<>());
//        List<Object> results = response.getData();
//
//        Specification<Category> equipmentCategorySpec = searchSpecification.equipmentCategoryContains(searchTerm);
//        results.addAll(categoryItemRepository.findAll(equipmentCategorySpec).stream()
//                .filter(Category::isActive)
//                .map(CategorySearchDTO::new).toList());
//
//        Specification<Catalog> catalogSpec = searchSpecification.catalogContains(searchTerm);
//        results.addAll(catalogRepository.findAll(catalogSpec).stream()
//                        .filter(Catalog::isActive)
//                        .map(CatalogSearchDTO::new).toList());
//
//        Specification<Product> productSpec = searchSpecification.productContains(searchTerm);
//        results.addAll(productRepository.findAll(productSpec).stream()
//                        .filter(Product::isActive)
//                .map(ProductSearchDTO::new).toList());
//
//        Specification<New> newSpec = searchSpecification.newContains(searchTerm);
//        results.addAll(newRepository.findAll(newSpec).stream()
//                .filter(New::isActive)
//                .map(NewSearchDTO::new).toList());
//
//        Specification<Event> eventSpec = searchSpecification.eventContains(searchTerm);
//        results.addAll(eventRepository.findAll(eventSpec).stream()
//                .filter(Event::isActive)
//                .map(EventSearchDTO::new).toList());
//
//        Specification<Partner> partnerSpec = searchSpecification.partnerContains(searchTerm);
//        results.addAll(partnerRepository.findAll(partnerSpec).stream()
//                .filter(Partner::isActive)
//                .map(PartnerSearchDTO::new).toList());
//
//        response.setData(results);
//        response.setMessage("Search result");
//        return ResponseEntity.status(200).body(response);
//    }
//}
