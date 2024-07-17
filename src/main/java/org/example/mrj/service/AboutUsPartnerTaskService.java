package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.AboutUsPartnerTask;
import org.example.mrj.domain.entity.Photo;
import org.example.mrj.repository.AboutUsPartnerTaskRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AboutUsPartnerTaskService {

    private final AboutUsPartnerTaskRepository aboutUsPartnerTaskRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<AboutUsPartnerTask>> create(String json, MultipartFile photoFile) {
        ApiResponse<AboutUsPartnerTask> response = new ApiResponse<>();
        try {
            AboutUsPartnerTask aboutUsPartnerTask = objectMapper.readValue(json, AboutUsPartnerTask.class);
            Photo photo = photoService.save(photoFile);
            aboutUsPartnerTask.setIconUrl(photo.getHttpUrl());
            aboutUsPartnerTask.setActive(true);
            AboutUsPartnerTask save = aboutUsPartnerTaskRepository.save(aboutUsPartnerTask);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<AboutUsPartnerTask>> findById(Long id) {
        ApiResponse<AboutUsPartnerTask> response = new ApiResponse<>();
        Optional<AboutUsPartnerTask> optionalAboutUsPartnerTask = aboutUsPartnerTaskRepository.findById(id);
        if (optionalAboutUsPartnerTask.isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        AboutUsPartnerTask aboutUsPartnerTask = optionalAboutUsPartnerTask.get();
        response.setData(aboutUsPartnerTask);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<AboutUsPartnerTask>>> findAll() {
        ApiResponse<List<AboutUsPartnerTask>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<AboutUsPartnerTask> all = aboutUsPartnerTaskRepository.findAll();
        all.forEach(aboutUsPartnerTask -> response.getData().add(aboutUsPartnerTask));
        response.setMessage("Found " + all.size() + " AboutUsPartnerTask");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<AboutUsPartnerTask>> update(Long id, String newJson, MultipartFile newPhoto) {
        ApiResponse<AboutUsPartnerTask> response = new ApiResponse<>();
        Optional<AboutUsPartnerTask> optionalAboutUsPartnerTask = aboutUsPartnerTaskRepository.findById(id);
        if (optionalAboutUsPartnerTask.isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        String oldPhotoUrl = aboutUsPartnerTaskRepository.findPhotoUrlById(id);
        boolean active = optionalAboutUsPartnerTask.get().isActive();
        AboutUsPartnerTask newAboutUsPartnerTask = new AboutUsPartnerTask();

        try {
            if (newJson != null) {
                newAboutUsPartnerTask = objectMapper.readValue(newJson, AboutUsPartnerTask.class);
                if (newPhoto == null || !(newPhoto.getSize() > 0)) {
                    newAboutUsPartnerTask.setIconUrl(oldPhotoUrl);
                }
                newAboutUsPartnerTask.setId(id);
                newAboutUsPartnerTask.setActive(active);
            } else {
                newAboutUsPartnerTask = aboutUsPartnerTaskRepository.findById(id).get();
            }

            if (newPhoto != null && newPhoto.getSize() > 0) {
                Photo photo = photoService.save(newPhoto);
                newAboutUsPartnerTask.setIconUrl(photo.getHttpUrl());
            }
            AboutUsPartnerTask save = aboutUsPartnerTaskRepository.save(newAboutUsPartnerTask);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (aboutUsPartnerTaskRepository.findById(id).isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        aboutUsPartnerTaskRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<AboutUsPartnerTask> optionalAboutUsPartnerTask = aboutUsPartnerTaskRepository.findById(id);
        if (optionalAboutUsPartnerTask.isEmpty()) {
            response.setMessage("AboutUsPartnerTask is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        AboutUsPartnerTask aboutUsPartnerTask = optionalAboutUsPartnerTask.get();
        boolean active = !aboutUsPartnerTask.isActive();
        aboutUsPartnerTaskRepository.changeActive(id, active);
        response.setMessage("AboutUsPartnerTask active: " + active);
        return ResponseEntity.status(200).body(response);
    }

}
