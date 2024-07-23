package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.ProductDTO;
import org.example.mrj.domain.entity.Category;
import org.example.mrj.domain.entity.CategoryItem;
import org.example.mrj.service.CategoryService;
import org.example.mrj.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController
{

    private final CategoryService categoryService;

    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Category>> add(
            @RequestBody CategoryItem categoryItem)
    {
        return categoryService.addItem(categoryItem);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Category>> get()
    {
        return categoryService.get();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Category>> update(
            @RequestBody Category category)
    {
        return categoryService.update(category);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    )
    {
        return categoryService.changeActive(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    )
    {
        return categoryService.delete(id);
    }

    @GetMapping("/products/{slug}")
    public ResponseEntity<ApiResponse<List<ProductDTO>>> getAllProductBySlug(
            @PathVariable String slug
    )
    {
        return productService.findAllByCategorySlug(slug);
    }

}
