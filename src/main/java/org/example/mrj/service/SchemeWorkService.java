package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.SchemeWork;
import org.example.mrj.domain.entity.Photo;
import org.example.mrj.repository.SchemeWorkRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SchemeWorkService {

    private final SchemeWorkRepository schemeWorkRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<SchemeWork>> create(String strSchemeWork, MultipartFile photoFile) {
        ApiResponse<SchemeWork> response = new ApiResponse<>();
        try {
            SchemeWork schemeWork = objectMapper.readValue(strSchemeWork, SchemeWork.class);
            Photo photo = photoService.save(photoFile);
            schemeWork.setPhotoUrl(photo.getHttpUrl());
            SchemeWork save = schemeWorkRepository.save(schemeWork);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    public ResponseEntity<ApiResponse<SchemeWork>> findById(Long id) {
        ApiResponse<SchemeWork> response = new ApiResponse<>();
        Optional<SchemeWork> optionalSchemeWork = schemeWorkRepository.findById(id);
        if (optionalSchemeWork.isEmpty()) {
            response.setMessage("SchemeWork is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        SchemeWork schemeWork = optionalSchemeWork.get();
        response.setData(schemeWork);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<SchemeWork>>> findAll() {
        ApiResponse<List<SchemeWork>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<SchemeWork> all = schemeWorkRepository.findAllByOrderByIdAsc();
        all.forEach(schemeWork -> response.getData().add(schemeWork));
        response.setMessage("Found " + all.size() + " schemeWork(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<SchemeWork>> update(Long id, String newJson, MultipartFile newPhoto) {
        ApiResponse<SchemeWork> response = new ApiResponse<>();
        Optional<SchemeWork> optionalSchemeWork = schemeWorkRepository.findById(id);
        if (optionalSchemeWork.isEmpty()) {
            response.setMessage("SchemeWork is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        String oldPhotoUrl = schemeWorkRepository.findPhotoUrlById(id);
        SchemeWork newSchemeWork = new SchemeWork();

        try {
            if (newJson != null) {
                newSchemeWork = objectMapper.readValue(newJson, SchemeWork.class);
                if (newPhoto == null || !(newPhoto.getSize() > 0)) {
                    newSchemeWork.setPhotoUrl(oldPhotoUrl);
                }
                newSchemeWork.setId(id);
            } else {
                newSchemeWork = schemeWorkRepository.findById(id).get();
            }

            if (newPhoto != null && newPhoto.getSize() > 0) {
                Photo photo = photoService.save(newPhoto);
                newSchemeWork.setPhotoUrl(photo.getHttpUrl());
            }
            SchemeWork save = schemeWorkRepository.save(newSchemeWork);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(404).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (schemeWorkRepository.findById(id).isEmpty()) {
            response.setMessage("SchemeWork is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        schemeWorkRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }

}
