package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.ContactBody;
import org.example.mrj.domain.entity.Representative;
import org.example.mrj.service.ContactBodyService;
import org.example.mrj.service.RepresentativeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactPageController {

    private final ContactBodyService contactBodyService;

    private final RepresentativeService representativeService;

    @PostMapping("/body/create")
    public ResponseEntity<ApiResponse<ContactBody>> createBody(
            @RequestBody ContactBody contactBody
    ) {
        return contactBodyService.create(contactBody);
    }

    @GetMapping("/body/get")
    public ResponseEntity<ApiResponse<ContactBody>> find() {
        return contactBodyService.find();
    }

    @PutMapping("/body/update")
    public ResponseEntity<ApiResponse<ContactBody>> update(
            @RequestBody ContactBody contactBody
    ) {
        return contactBodyService.update(contactBody);
    }

    @DeleteMapping("/body/delete")
    public ResponseEntity<ApiResponse<?>> delete() {
        return contactBodyService.delete();
    }

    @PostMapping("/representative/create")
    public ResponseEntity<ApiResponse<Representative>> createRepresentative(
            @RequestParam(value = "json") String json,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return representativeService.create(json, photo);
    }

    @GetMapping("/representative/get/{id}")
    public ResponseEntity<ApiResponse<Representative>> getRepresentativeById(
            @PathVariable Long id
    ) {
        return representativeService.findById(id);
    }

    @GetMapping("/representative/get-all")
    public ResponseEntity<ApiResponse<List<Representative>>> getAllRepresentative() {
        return representativeService.findAll();
    }

    @PutMapping("/representative/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActiveRepresentative(
            @PathVariable Long id
    ) {
        return representativeService.changeActive(id);
    }

    @DeleteMapping("/representative/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteRepresentative(
            @PathVariable Long id
    ) {
        return representativeService.delete(id);
    }

}
