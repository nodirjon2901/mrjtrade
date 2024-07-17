package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.MainAboutUs;
import org.example.mrj.service.MainAboutUsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/main-about-us")
@RequiredArgsConstructor
public class MainAboutUsController {

    private final MainAboutUsService mainAboutUsService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<MainAboutUs>> createAboutUs(
            @RequestParam(value = "json") String mainAboutUs,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return mainAboutUsService.create(mainAboutUs, photo);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<MainAboutUs>> find() {
        return mainAboutUsService.find();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<MainAboutUs>> update(
            @RequestParam(value = "json") String mainAboutUs,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return mainAboutUsService.update(mainAboutUs, photo);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> delete() {
        return mainAboutUsService.delete();
    }

    @PutMapping("/change-active")
    public ResponseEntity<ApiResponse<?>> changeActive() {
        return mainAboutUsService.changeActive();
    }

}
