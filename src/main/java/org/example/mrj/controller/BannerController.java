package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.BannerWrapper;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Banner;
import org.example.mrj.service.BannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/banner")
@RequiredArgsConstructor
public class BannerController
{

    private final BannerService bannerService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<BannerWrapper>> createBanner(
            @RequestParam(value = "link") String link,
            @RequestParam(value = "active") Boolean active,
            @RequestParam(value = "photo") MultipartFile gallery)
    {
        return bannerService.addSlider(link, active, gallery);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<BannerWrapper>> get()
    {
        return bannerService.get();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<BannerWrapper>> update(
            @RequestBody Banner banner)
    {
        return bannerService.update(banner);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> delete()
    {
        return bannerService.delete();
    }


}
