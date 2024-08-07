package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.ProductDTO;
import org.example.mrj.domain.entity.Product;
import org.example.mrj.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor

@Controller
@RequestMapping("/product/v2")
public class ProductController
{
    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Product>> addProduct(
            @RequestParam(value = "json") String jsonData,
//            @RequestParam(value = "main-photo", required = false) MultipartFile mainPhoto,
            @RequestParam(value = "gallery") List<MultipartFile> gallery)
    {
        return productService.add(jsonData, gallery);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<?>> getProduct(
            @PathVariable("slug") String slug,
            @RequestParam(value = "similar", required = false, defaultValue = "false") boolean similar)
    {
        return productService.get(slug, similar);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getProduct(
            @RequestParam(value = "category-id", required = false) Long categoryId,
            @RequestParam(value = "catalog-id", required = false) Long catalogId,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "professional", required = false) Boolean professional)
    {
        return productService.getAll(categoryId, catalogId, tag, professional);
    }


    @PutMapping
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @RequestBody Product product)
    {
        return productService.update(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long id)
    {
        return productService.delete(id);
    }

}
