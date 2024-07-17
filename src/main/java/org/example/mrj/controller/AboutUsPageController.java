package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.AboutUsChooseUs;
import org.example.mrj.domain.entity.AboutUsHeader;
import org.example.mrj.domain.entity.AboutUsPartnerTask;
import org.example.mrj.service.AboutUsChooseUsService;
import org.example.mrj.service.AboutUsHeaderService;
import org.example.mrj.service.AboutUsPartnerTaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/about-us")
@RequiredArgsConstructor
public class AboutUsPageController {

    private final AboutUsHeaderService aboutUsHeaderService;

    private final AboutUsPartnerTaskService aboutUsPartnerTaskService;

    private final AboutUsChooseUsService aboutUsChooseUsService;

    @PostMapping("/header/create")
    public ResponseEntity<ApiResponse<AboutUsHeader>> createAboutUsHeader(
            @RequestParam(value = "json") String aboutUsHeader,
            @RequestPart(value = "photos") List<MultipartFile> photos
    ) {
        return aboutUsHeaderService.create(aboutUsHeader, photos);
    }

    @GetMapping("/header/get/{id}")
    public ResponseEntity<ApiResponse<AboutUsHeader>> findByIdAboutUsHeader(
            @PathVariable Long id
    ) {
        return aboutUsHeaderService.findById(id);
    }

    @GetMapping("/header/get-all")
    public ResponseEntity<ApiResponse<List<AboutUsHeader>>> findAllAboutUsHeader() {
        return aboutUsHeaderService.findAll();
    }

    @PutMapping("/header/update/{id}")
    public ResponseEntity<ApiResponse<AboutUsHeader>> updateAboutUsHeader(
            @PathVariable Long id,
            @RequestParam(value = "json", required = false) String aboutUsHeader,
            @RequestPart(value = "photos", required = false) List<MultipartFile> photos
    ) {
        return aboutUsHeaderService.update(id, aboutUsHeader, photos);
    }

    @DeleteMapping("/header/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteAboutUsHeader(
            @PathVariable Long id
    ) {
        return aboutUsHeaderService.delete(id);
    }

    @PostMapping("/partner-service/create")
    public ResponseEntity<ApiResponse<AboutUsPartnerTask>> createPartnerService(
            @RequestParam(value = "json") String partnerTask,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return aboutUsPartnerTaskService.create(partnerTask, photo);
    }

    @GetMapping("/partner-service/get/{id}")
    public ResponseEntity<ApiResponse<AboutUsPartnerTask>> findByIdPartnerTask(
            @PathVariable Long id
    ) {
        return aboutUsPartnerTaskService.findById(id);
    }

    @GetMapping("/partner-service/get-all")
    public ResponseEntity<ApiResponse<List<AboutUsPartnerTask>>> findAllPartnerTask() {
        return aboutUsPartnerTaskService.findAll();
    }

    @PutMapping("/partner-service/update/{id}")
    public ResponseEntity<ApiResponse<AboutUsPartnerTask>> updatePartnerTask(
            @PathVariable Long id,
            @RequestParam(value = "json") String partnerTask,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return aboutUsPartnerTaskService.update(id, partnerTask, photo);
    }

    @PutMapping("/partner-service/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActivePartnerTask(
            @PathVariable Long id
    ) {
        return aboutUsPartnerTaskService.changeActive(id);
    }

    @DeleteMapping("/partner-service/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deletePartnerTask(
            @PathVariable Long id
    ) {
        return aboutUsPartnerTaskService.delete(id);
    }

    @PostMapping("/choose-us/create")
    public ResponseEntity<ApiResponse<AboutUsChooseUs>> createChooseUs(
            @RequestBody AboutUsChooseUs aboutUsChooseUs
    ) {
        return aboutUsChooseUsService.create(aboutUsChooseUs);
    }

    @GetMapping("/choose-us/get/{id}")
    public ResponseEntity<ApiResponse<AboutUsChooseUs>> getByIdChooseUs(
            @PathVariable Long id
    ) {
        return aboutUsChooseUsService.findById(id);
    }

    @GetMapping("/choose-us/get-all")
    public ResponseEntity<ApiResponse<List<AboutUsChooseUs>>> findAllChooseUs() {
        return aboutUsChooseUsService.findAll();
    }

    @PutMapping("/choose-us/update/{id}")
    public ResponseEntity<ApiResponse<AboutUsChooseUs>> update(
            @PathVariable Long id,
            @RequestBody AboutUsChooseUs aboutUsChooseUs
    ) {
        return aboutUsChooseUsService.update(id, aboutUsChooseUs);
    }

    @DeleteMapping("/choose-us/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return aboutUsChooseUsService.delete(id);
    }
}
