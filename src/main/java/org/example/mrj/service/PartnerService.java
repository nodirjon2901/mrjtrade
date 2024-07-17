package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Partner;
import org.example.mrj.domain.entity.Photo;
import org.example.mrj.repository.PartnerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<Partner>> create(String strPartner, MultipartFile photoFile) {
        ApiResponse<Partner> response = new ApiResponse<>();
        try {
            Partner partner = objectMapper.readValue(strPartner, Partner.class);
            Photo photo = photoService.save(photoFile);
            partner.setPhotoUrl(photo.getHttpUrl());
            partner.setActive(true);
            Partner save = partnerRepository.save(partner);
            String slug = save.getId() + "-" + save.getTitle();
            partnerRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<Partner>> findById(Long id) {
        ApiResponse<Partner> response = new ApiResponse<>();
        Optional<Partner> optionalPartner = partnerRepository.findById(id);
        if (optionalPartner.isEmpty()) {
            response.setMessage("Partner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Partner partner = optionalPartner.get();
        response.setData(partner);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Partner>> findBySlug(String slug) {
        ApiResponse<Partner> response = new ApiResponse<>();
        Optional<Partner> optionalPartner = partnerRepository.findBySlug(slug);
        if (optionalPartner.isEmpty()) {
            response.setMessage("Partner is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        Partner partner = optionalPartner.get();
        response.setData(partner);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<Partner>>> findAll() {
        ApiResponse<List<Partner>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAll();
        all.forEach(partner -> response.getData().add(partner));
        response.setMessage("Found " + all.size() + " partner(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Partner>> update(Long id, String newJson, MultipartFile newPhoto) {
        ApiResponse<Partner> response = new ApiResponse<>();
        Optional<Partner> optionalPartner = partnerRepository.findById(id);
        if (optionalPartner.isEmpty()) {
            response.setMessage("Partner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        String oldPhotoUrl = partnerRepository.findPhotoUrlById(id);
        String slug = partnerRepository.findSlugById(id);
        boolean active = optionalPartner.get().isActive();
        Partner newPartner = new Partner();

        try {
            if (newJson != null) {
                newPartner = objectMapper.readValue(newJson, Partner.class);
                if (newPhoto == null || !(newPhoto.getSize() > 0)) {
                    newPartner.setPhotoUrl(oldPhotoUrl);
                }
                newPartner.setId(id);
                newPartner.setSlug(slug);
                newPartner.setActive(active);
            } else {
                newPartner = partnerRepository.findById(id).get();
            }

            if (newPhoto != null && newPhoto.getSize() > 0) {
                Photo photo = photoService.save(newPhoto);
                newPartner.setPhotoUrl(photo.getHttpUrl());
            }
            Partner save = partnerRepository.save(newPartner);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (partnerRepository.findById(id).isEmpty()) {
            response.setMessage("Partner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        partnerRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Partner> optionalPartner = partnerRepository.findById(id);
        if (optionalPartner.isEmpty()) {
            response.setMessage("Partner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Partner partner = optionalPartner.get();
        boolean active = !partner.isActive();
        partnerRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current partner active: " + active);
        return ResponseEntity.status(200).body(response);
    }


}
