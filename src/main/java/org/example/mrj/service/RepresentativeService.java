package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Representative;
import org.example.mrj.repository.RepresentativeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class RepresentativeService {

    private final RepresentativeRepository representativeRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Representative>> create(String json, MultipartFile photoFile) {
        ApiResponse<Representative> response = new ApiResponse<>();
        try {
            Representative representative = objectMapper.readValue(json, Representative.class);
            representative.setActive(true);
            representative.setAddable(true);
            representative.setPhoto(photoService.save(photoFile));
            Representative save = representativeRepository.save(representative);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    public ResponseEntity<ApiResponse<Representative>> findById(Long id) {
        ApiResponse<Representative> response = new ApiResponse<>();
        Optional<Representative> optional = representativeRepository.findById(id);
        if (optional.isEmpty()) {
            response.setMessage("Representative is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Representative representative = optional.get();
        response.setData(representative);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<Representative>>> findAll() {
        ApiResponse<List<Representative>> response = new ApiResponse<>();
        List<Representative> all = representativeRepository.findAll();
        response.setData(new ArrayList<>());
        all.forEach(representative -> response.getData().add(representative));
        return ResponseEntity.status(200).body(response);
    }

//    public ResponseEntity<ApiResponse<Representative>> update(Long id, String newJson, MultipartFile newPhotoFile) {
//        ApiResponse<Representative> response = new ApiResponse<>();
//        Optional<Representative> optional = representativeRepository.findById(id);
//        if (optional.isEmpty()) {
//            response.setMessage("Representative is not found");
//            return ResponseEntity.status(404).body(response);
//        }
//
//        boolean active = optional.get().isActive();
//        boolean addable = optional.get().isAddable();
//        Photo oldPhoto = optional.get().getPhoto();
//
//
//    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (representativeRepository.findById(id).isEmpty()) {
            response.setMessage("Representative is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        representativeRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Representative> optional = representativeRepository.findById(id);
        if (optional.isEmpty()) {
            response.setMessage("Representative is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        boolean active = !optional.get().isActive();
        representativeRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current representative active: " + active);
        return ResponseEntity.status(200).body(response);
    }
}
