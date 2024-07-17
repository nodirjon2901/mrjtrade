package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.MainAboutUs;
import org.example.mrj.domain.entity.Photo;
import org.example.mrj.repository.MainAboutUsRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MainAboutUsService {

    private final MainAboutUsRepository mainAboutUsRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<MainAboutUs>> create(String strMainAboutUs, MultipartFile photoFile) {
        ApiResponse<MainAboutUs> response = new ApiResponse<>();
        Optional<MainAboutUs> firstMainAboutUs = mainAboutUsRepository.findAll().stream().findFirst();
        if (firstMainAboutUs.isPresent()) {
            return update(strMainAboutUs, photoFile);
        }
        try {
            MainAboutUs mainAboutUs = objectMapper.readValue(strMainAboutUs, MainAboutUs.class);
            Photo photo = photoService.save(photoFile);
            mainAboutUs.setPhotoUrl(photo.getHttpUrl());
            MainAboutUs save = mainAboutUsRepository.save(mainAboutUs);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<MainAboutUs>> find() {
        ApiResponse<MainAboutUs> response = new ApiResponse<>();
        Optional<MainAboutUs> optionalMainAboutUs = mainAboutUsRepository.findAll().stream().findFirst();
        if (optionalMainAboutUs.isEmpty()) {
            response.setMessage("MainAboutUs is not found");
            return ResponseEntity.status(404).body(response);
        }
        MainAboutUs mainAboutUs = optionalMainAboutUs.get();
        response.setMessage("Found");
        response.setData(mainAboutUs);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<MainAboutUs>> update(String newJson, MultipartFile newPhoto) {
        ApiResponse<MainAboutUs> response = new ApiResponse<>();
        Optional<MainAboutUs> optionalMainAboutUs = mainAboutUsRepository.findAll().stream().findFirst();
        if (optionalMainAboutUs.isEmpty()) {
            response.setMessage("MainAboutUs is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optionalMainAboutUs.get().getId();
        String oldPhotoUrl = mainAboutUsRepository.findPhotoUrlById(id);
        boolean active = optionalMainAboutUs.get().isActive();
        MainAboutUs newMainAboutUs = new MainAboutUs();
        try {
            if (newJson != null) {
                newMainAboutUs = objectMapper.readValue(newJson, MainAboutUs.class);
                if (newPhoto == null || !(newPhoto.getSize() > 0)) {
                    newMainAboutUs.setPhotoUrl(oldPhotoUrl);
                }
                newMainAboutUs.setId(id);
                newMainAboutUs.setActive(active);
            } else {
                newMainAboutUs = mainAboutUsRepository.findById(id).get();
            }

            if (newPhoto != null && newPhoto.getSize() > 0) {
                Photo photo = photoService.save(newPhoto);
                newMainAboutUs.setPhotoUrl(photo.getHttpUrl());
            }

            MainAboutUs save = mainAboutUsRepository.save(newMainAboutUs);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<MainAboutUs> optionalMainAboutUs = mainAboutUsRepository.findAll().stream().findFirst();
        if (optionalMainAboutUs.isEmpty()) {
            response.setMessage("MainAboutUs is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optionalMainAboutUs.get().getId();
        mainAboutUsRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive() {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<MainAboutUs> optionalMainAboutUs = mainAboutUsRepository.findAll().stream().findFirst();
        if (optionalMainAboutUs.isEmpty()) {
            response.setMessage("MainAboutUs is not found");
            return ResponseEntity.status(404).body(response);
        }
        MainAboutUs mainAboutUs = optionalMainAboutUs.get();
        boolean active = !mainAboutUs.isActive();
        mainAboutUsRepository.changeActive(mainAboutUs.getId(), active);
        response.setMessage("Successfully changed! Current mainAboutUs active: " + active);
        return ResponseEntity.status(200).body(response);
    }

}
