package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.SchemeWork;
import org.example.mrj.domain.entity.SchemeWorkItems;
import org.example.mrj.service.SchemeWorkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/scheme-work")
@RequiredArgsConstructor
public class SchemeWorkController {

    private final SchemeWorkService schemeWorkService;

    @PostMapping("/add-items")
    public ResponseEntity<ApiResponse<SchemeWorkItems>> addSchemeWorkItems(
            @RequestParam(value = "json") String schemeWorkItems,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return schemeWorkService.addSchemeWorkItems(schemeWorkItems, photo);
    }

    @PostMapping("/header")
    public ResponseEntity<ApiResponse<SchemeWork>> createOrHeader(
            @RequestParam(value = "header") String header
    ){
        return schemeWorkService.createOrUpdate(header);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<SchemeWorkItems>> findById(
            @PathVariable Long id
    ) {
        return schemeWorkService.findById(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<SchemeWork>> findAll() {
        return schemeWorkService.findAll();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<SchemeWorkItems>> update(
            @RequestBody SchemeWorkItems schemeWorkItems
    ) {
        return schemeWorkService.update(schemeWorkItems);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return schemeWorkService.deleteById(id);
    }

}
