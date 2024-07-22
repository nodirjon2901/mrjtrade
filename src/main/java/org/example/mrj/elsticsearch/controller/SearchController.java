//package org.example.mrj.elsticsearch.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.mrj.elsticsearch.service.SearchService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//public class SearchController {
//
//    private final SearchService searchService;
//
//    @GetMapping("/search")
//    public ResponseEntity<Map<String, Object>> search(@RequestParam String query) {
//        Map<String, Object> searchResults = searchService.search(query);
//        return ResponseEntity.ok(searchResults);
//    }
//
//}
