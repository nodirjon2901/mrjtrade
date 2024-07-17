package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.New;
import org.example.mrj.service.NewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewController {

    private final NewService newService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<New>> createNew(
            @RequestParam(value = "json") String newness,
            @RequestPart(value = "photos") List<MultipartFile> photos
    ) {
        return newService.create(newness, photos);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<New>> findBySlug(
            @PathVariable String slug
    ) {
        return newService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<New>>> findAll() {
        return newService.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<New>> update(
            @PathVariable Long id,
            @RequestParam(value = "json") String newness,
            @RequestPart(value = "photos") List<MultipartFile> photos
    ) {
        return newService.update(id, newness, photos);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return newService.changeActive(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteNew(
            @PathVariable Long id
    ) {
        return newService.deleteById(id);
    }


}
