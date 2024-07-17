package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.CatalogDTO;
import org.example.mrj.domain.entity.Catalog;
import org.example.mrj.service.CatalogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @PostMapping("/create/{categoryId}")
    public ResponseEntity<ApiResponse<CatalogDTO>> create(
            @PathVariable Long categoryId,
            @RequestBody Catalog catalog
    ) {
        return catalogService.create(categoryId, catalog);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<CatalogDTO>> findById(
            @PathVariable String slug
    ) {
        return catalogService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<CatalogDTO>>> findAll() {
        return catalogService.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<CatalogDTO>> update(
            @PathVariable Long id,
            @RequestBody Catalog catalog
    ) {
        return catalogService.update(id, catalog);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return catalogService.delete(id);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return catalogService.changeActive(id);
    }


}
