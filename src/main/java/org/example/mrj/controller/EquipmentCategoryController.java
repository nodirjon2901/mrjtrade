package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.ProductDTO;
import org.example.mrj.domain.entity.EquipmentCategory;
import org.example.mrj.domain.entity.Product;
import org.example.mrj.service.EquipmentCategoryService;
import org.example.mrj.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/equipment-category")
@RequiredArgsConstructor
public class EquipmentCategoryController {

    private final EquipmentCategoryService equipmentCategoryService;

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<EquipmentCategory>> create(
            @RequestParam(value = "json") String category,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return equipmentCategoryService.create(category, photo);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<EquipmentCategory>> findBySlug(
            @PathVariable String slug
    ) {
        return equipmentCategoryService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<EquipmentCategory>>> findAll() {
        return equipmentCategoryService.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<EquipmentCategory>> update(
            @PathVariable Long id,
            @RequestParam(value = "json", required = false) String json,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        return equipmentCategoryService.update(id, json, photo);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return equipmentCategoryService.changeActive(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return equipmentCategoryService.delete(id);
    }

    @GetMapping("/products/{slug}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProductBySlug(
            @PathVariable String slug
    ) {
        return productService.findAllByCategorySlug(slug);
    }

}
