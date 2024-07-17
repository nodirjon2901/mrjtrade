package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Banner;
import org.example.mrj.domain.entity.Photo;
import org.example.mrj.repository.BannerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BannerService {

    private final BannerRepository bannerRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<Banner>> create(String strBanner, MultipartFile photoFile) {
        ApiResponse<Banner> response = new ApiResponse<>();
        try {
            Banner banner = objectMapper.readValue(strBanner, Banner.class);
            Photo photo = photoService.save(photoFile);
            banner.setPhotoUrl(photo.getHttpUrl());
            banner.setActive(true);
            Banner save = bannerRepository.save(banner);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    public ResponseEntity<ApiResponse<Banner>> findById(Long id) {
        ApiResponse<Banner> response = new ApiResponse<>();
        Optional<Banner> optionalBanner = bannerRepository.findById(id);
        if (optionalBanner.isEmpty()) {
            response.setMessage("Banner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Banner banner = optionalBanner.get();
        response.setData(banner);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<Banner>>> findAll() {
        ApiResponse<List<Banner>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Banner> all = bannerRepository.findAll();
        all.forEach(banner -> response.getData().add(banner));
        response.setMessage("Found " + all.size() + " banner(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Banner>> update(Long id, String newJson, MultipartFile newPhoto) {
        ApiResponse<Banner> response = new ApiResponse<>();
        Optional<Banner> optionalBanner = bannerRepository.findById(id);
        if (optionalBanner.isEmpty()) {
            response.setMessage("Banner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        String oldPhotoUrl = bannerRepository.findPhotoUrlById(id);
        boolean active = optionalBanner.get().isActive();
        Banner newBanner = new Banner();

        try {
            if (newJson != null) {
                newBanner = objectMapper.readValue(newJson, Banner.class);
                if (newPhoto == null || !(newPhoto.getSize() > 0)) {
                    newBanner.setPhotoUrl(oldPhotoUrl);
                }
                newBanner.setId(id);
                newBanner.setActive(active);
            } else {
                newBanner = bannerRepository.findById(id).get();
            }

            if (newPhoto != null && newPhoto.getSize() > 0) {
                Photo photo = photoService.save(newPhoto);
                newBanner.setPhotoUrl(photo.getHttpUrl());
            }
            Banner save = bannerRepository.save(newBanner);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (bannerRepository.findById(id).isEmpty()) {
            response.setMessage("Banner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        bannerRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Banner> optionalBanner = bannerRepository.findById(id);
        if (optionalBanner.isEmpty()) {
            response.setMessage("Banner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Banner banner = optionalBanner.get();
        boolean active = !banner.isActive();
        bannerRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current banner active: " + active);
        return ResponseEntity.status(200).body(response);
    }

}
