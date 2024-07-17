package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Footer;
import org.example.mrj.service.FooterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/footer")
@RequiredArgsConstructor
public class FooterController {

    private final FooterService footerService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Footer>> createFooter(
            @RequestParam(value = "json") String footer,
            @RequestPart(value = "logoPhoto") MultipartFile logoPhoto,
            @RequestPart(value = "tgPhoto") MultipartFile tgPhoto,
            @RequestPart(value = "faceBookPhoto") MultipartFile faceBookPhoto,
            @RequestPart(value = "instagramPhoto") MultipartFile instagramPhoto,
            @RequestPart(value = "youTubePhoto") MultipartFile youTubePhoto,
            @RequestPart(value = "creatorPhoto") MultipartFile creatorPhoto
    ) {
        return footerService.create(footer, logoPhoto, tgPhoto, faceBookPhoto, instagramPhoto, youTubePhoto, creatorPhoto);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Footer>> find() {
        return footerService.find();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Footer>> update(
            @RequestParam(value = "json",required = false) String footer,
            @RequestPart(value = "logoPhoto",required = false) MultipartFile logoPhoto,
            @RequestPart(value = "tgPhoto",required = false) MultipartFile tgPhoto,
            @RequestPart(value = "faceBookPhoto",required = false) MultipartFile faceBookPhoto,
            @RequestPart(value = "instagramPhoto",required = false) MultipartFile instagramPhoto,
            @RequestPart(value = "youTubePhoto",required = false) MultipartFile youTubePhoto,
            @RequestPart(value = "creatorPhoto",required = false) MultipartFile creatorPhoto
    ) {
        return footerService.update(footer, logoPhoto, tgPhoto, faceBookPhoto, instagramPhoto, youTubePhoto, creatorPhoto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> delete() {
        return footerService.delete();
    }

}
