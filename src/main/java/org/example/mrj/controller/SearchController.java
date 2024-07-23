//package org.example.mrj.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.example.mrj.domain.dto.ApiResponse;
//import org.example.mrj.service.SearchService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class SearchController {
//
//    private final SearchService searchService;
//
//    @GetMapping("/search")
//    public ResponseEntity<ApiResponse<List<Object>>> search(
//            @RequestParam String query
//    ){
//        return searchService.search(query);
//    }
//
//}
