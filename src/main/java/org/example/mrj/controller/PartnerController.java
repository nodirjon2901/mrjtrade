package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.PartnerDTO;
import org.example.mrj.domain.entity.Partner;
import org.example.mrj.domain.entity.PartnerHeader;
import org.example.mrj.service.PartnerHeaderService;
import org.example.mrj.service.PartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;

    private final PartnerHeaderService partnerHeaderService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Partner>> createPartner(
            @RequestParam(value = "json") String partner,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return partnerService.create(partner, photo);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<Partner>> getBySlug(
            @PathVariable String slug
    ) {
        return partnerService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<Partner>>> findAll() {
        return partnerService.findAll();
    }

    @GetMapping("/get-all-partner")
    public ResponseEntity<ApiResponse<List<PartnerDTO>>> findAllForMenuPage() {
        return partnerService.findSixPartnerForMainPage();
    }

    @GetMapping("/get-others/{partnerSlug}")
    public ResponseEntity<ApiResponse<List<Partner>>> findOtherPartner(
            @PathVariable String partnerSlug
    ) {
        return partnerService.findOtherPartnerBySlug(partnerSlug);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Partner>> update(
            @PathVariable Long id,
            @RequestParam(value = "json") String partner,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return partnerService.update(id, partner, photo);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return partnerService.deleteById(id);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return partnerService.changeActive(id);
    }

    @PostMapping("/header/create")
    public ResponseEntity<ApiResponse<PartnerHeader>> createHeader(
            @RequestBody PartnerHeader header
    ) {
        return partnerHeaderService.create(header);
    }

    @GetMapping("/header/get")
    public ResponseEntity<ApiResponse<PartnerHeader>> findHeader() {
        return partnerHeaderService.find();
    }

    @PutMapping("/header/update")
    public ResponseEntity<ApiResponse<PartnerHeader>> updateHeader(
            @RequestBody PartnerHeader partnerHeader
    ) {
        return partnerHeaderService.update(partnerHeader);
    }

    @DeleteMapping("/header/delete")
    public ResponseEntity<ApiResponse<?>> deleteHeader() {
        return partnerHeaderService.delete();
    }

}
