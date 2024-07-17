package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.SchemeWork;
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

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SchemeWork>> create(
            @RequestParam(value = "json") String schemeWork,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return schemeWorkService.create(schemeWork, photo);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<SchemeWork>> findById(
            @PathVariable Long id
    ) {
        return schemeWorkService.findById(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<SchemeWork>>> findAll() {
        return schemeWorkService.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<SchemeWork>> update(
            @PathVariable Long id,
            @RequestParam(value = "json", required = false) String schemeWork,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        return schemeWorkService.update(id, schemeWork, photo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return schemeWorkService.deleteById(id);
    }

}
