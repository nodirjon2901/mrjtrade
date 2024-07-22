package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Banner;
import org.example.mrj.domain.entity.BannerSlider;
import org.example.mrj.service.BannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController
{

    private final BannerService bannerService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Banner>> createBanner(
            @RequestParam(value = "link") String link,
            @RequestParam(value = "photo") MultipartFile gallery)
    {
        return bannerService.addSlider(link, gallery);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Banner>> get()
    {
        return bannerService.get();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Banner>> update(
            @RequestBody Banner banner)
    {
        return bannerService.update(banner);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    )
    {
        return bannerService.deleteById(id);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    )
    {
        return bannerService.changeActive(id);
    }

}
