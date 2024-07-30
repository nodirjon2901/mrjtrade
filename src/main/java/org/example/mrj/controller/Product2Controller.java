package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.Product2DTO;
import org.example.mrj.domain.entity.Product2;
import org.example.mrj.service.Product2Service;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor

@Controller
@RequestMapping("/product/v2")
public class Product2Controller
{
    private final Product2Service product2Service;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Product2>> addProduct(
            @RequestParam(value = "json") String jsonData,
//            @RequestParam(value = "main-photo", required = false) MultipartFile mainPhoto,
            @RequestParam(value = "gallery") List<MultipartFile> gallery)
    {
        return product2Service.
                add(jsonData, gallery);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<Product2>> getProduct(
            @PathVariable("slug") String slug)
    {
        return product2Service.get(slug);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Product2DTO>>> getProduct(
            @RequestParam(value = "category-id", required = false) Long categoryId,
            @RequestParam(value = "catalog-id", required = false) Long catalogId,
            @RequestParam(value = "tag", required = false) String tag)
    {
        return product2Service.getAll(categoryId, catalogId, tag);
    }


    @PutMapping
    public ResponseEntity<ApiResponse<Product2>> updateProduct(
            @RequestBody Product2 product2)
    {
        return product2Service.update(product2);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long id)
    {
        return product2Service.delete(id);
    }

}
