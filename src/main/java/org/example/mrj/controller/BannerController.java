package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Banner;
import org.example.mrj.service.BannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Banner>> createBanner(
            @RequestParam(value = "json") String banner,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return bannerService.create(banner, photo);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<Banner>> findById(
            @PathVariable Long id
    ) {
        return bannerService.findById(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<Banner>>> findAll() {
        return bannerService.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Banner>> update(
            @PathVariable Long id,
            @RequestParam(value = "json", required = false) String banner,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        return bannerService.update(id, banner, photo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return bannerService.deleteById(id);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return bannerService.changeActive(id);
    }

}
