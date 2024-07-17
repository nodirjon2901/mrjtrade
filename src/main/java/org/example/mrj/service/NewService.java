package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.New;
import org.example.mrj.repository.NewRepository;
import org.example.mrj.util.SlugUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class NewService {

    private final NewRepository newRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<New>> create(String strNew, List<MultipartFile> photoFiles) {
        ApiResponse<New> response = new ApiResponse<>();
        try {
            New newness = objectMapper.readValue(strNew, New.class);
            newness.setPhotoUrls(new ArrayList<>());
            newness.setActive(true);
            for (MultipartFile photo : photoFiles) {
                newness.getPhotoUrls().add(photoService.save(photo).getHttpUrl());
            }
            New save = newRepository.save(newness);
            String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getTitle());
            newRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<New>> findById(Long id) {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findById(id);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        response.setMessage("Found");
        response.setData(newness);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<New>> findBySlug(String slug) {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findBySlug(slug);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        response.setMessage("Found");
        response.setData(newness);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<New>>> findAll() {
        ApiResponse<List<New>> response = new ApiResponse<>();
        List<New> all = newRepository.findAll();
        response.setData(new ArrayList<>());
        all.forEach(newness -> response.getData().add(newness));
        response.setMessage("Found " + all.size() + " new(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<New>> update(Long id, String newJson, List<MultipartFile> newPhotosFile) {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findById(id);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        List<String> oldPhotoUrls = optionalNew.get().getPhotoUrls();
        boolean active = optionalNew.get().isActive();
        String slug = newRepository.findSlugById(id);
        New newness = new New();

        try {
            if (newJson != null) {
                newness = objectMapper.readValue(newJson, New.class);
                if (newPhotosFile == null || newPhotosFile.isEmpty()) {
                    newness.setPhotoUrls(oldPhotoUrls);
                }
                newness.setId(id);
                newness.setSlug(slug);
                newness.setActive(active);
            } else {
                newness = newRepository.findById(id).get();
            }
            if (newPhotosFile != null && !newPhotosFile.isEmpty()) {
                newness.setPhotoUrls(new ArrayList<>());
                for (MultipartFile photo : newPhotosFile) {
                    newness.getPhotoUrls().add(photoService.save(photo).getHttpUrl());
                }
            }
            New save = newRepository.save(newness);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (newRepository.findById(id).isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        newRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findById(id);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        boolean active = !newness.isActive();
        newRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current new active: " + active);
        return ResponseEntity.status(200).body(response);
    }
}
