package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.CategoryItemDTO;
import org.example.mrj.domain.dto.ProductDTO;
import org.example.mrj.domain.entity.Category;
import org.example.mrj.domain.entity.CategoryItem;
import org.example.mrj.service.CategoryService;
import org.example.mrj.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController
{
    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Category>> add(
            @RequestParam("json") String json,
            @RequestParam("photo") MultipartFile photo)
    {
        return categoryService.addItem(json, photo);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Category>> get(
            @RequestParam(value = "main", required = false) Boolean main,
            @RequestParam(value = "active", required = false) Boolean active)
    {
        return categoryService.get(main, active);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<CategoryItem>> getItem(
            @PathVariable String slug)
    {
        return categoryService.getItem(slug);
    }

    @GetMapping("/name-list")
    public ResponseEntity<ApiResponse<List<CategoryItemDTO>>> getNameList()
    {
        return categoryService.getNameList();
    }


    @PutMapping
    public ResponseEntity<ApiResponse<Category>> update(
            @RequestBody Category category)
    {
        return categoryService.update(category);
    }

    @DeleteMapping("/delete/{item-id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable("item-id") Long itemId)
    {
        return categoryService.delete(itemId);
    }

}
