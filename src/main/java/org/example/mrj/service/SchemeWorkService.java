package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.*;
import org.example.mrj.repository.SchemeWorkItemsRepository;
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

    private final SchemeWorkItemsRepository schemeWorkItemsRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<SchemeWorkItems>> addSchemeWorkItems(String strSchemeWorkItems, MultipartFile photoFile) {
        ApiResponse<SchemeWorkItems> response = new ApiResponse<>();
        Optional<SchemeWork> optionalSchemeWork = schemeWorkRepository.findAll().stream().findFirst();
        if (optionalSchemeWork.isEmpty()) {
            response.setMessage("SchemeWork is not found");
            return ResponseEntity.status(404).body(response);
        }
        SchemeWork schemeWork = optionalSchemeWork.get();
        if (schemeWorkItemsRepository.count()>=4){
            response.setMessage("SchemeWorkItems are enough");
            return ResponseEntity.status(409).body(response);
        }
        try {
            SchemeWorkItems schemeWorkItems = objectMapper.readValue(strSchemeWorkItems, SchemeWorkItems.class);
            schemeWorkItems.setPhoto(photoService.save(photoFile));
            schemeWorkItems.setSchemeWork(schemeWork);
            SchemeWorkItems save = schemeWorkItemsRepository.save(schemeWorkItems);
            schemeWork.getSchemeWorkItems().add(save);
            schemeWorkRepository.save(schemeWork);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    public ResponseEntity<ApiResponse<SchemeWork>> createOrUpdate(String header) {
        ApiResponse<SchemeWork> response = new ApiResponse<>();
        Optional<SchemeWork> optionalSchemeWork = schemeWorkRepository.findAll().stream().findFirst();
        if (optionalSchemeWork.isEmpty()) {
            SchemeWork schemeWork = SchemeWork.builder()
                    .header(header)
                    .schemeWorkItems(new ArrayList<>())
                    .build();
            response.setData(schemeWorkRepository.save(schemeWork));
            return ResponseEntity.status(201).body(response);
        }
        SchemeWork schemeWork = optionalSchemeWork.get();
        schemeWork.setHeader(header);
        response.setData(schemeWorkRepository.save(schemeWork));
        return ResponseEntity.status(201).body(response);
    }

    public ResponseEntity<ApiResponse<SchemeWorkItems>> findById(Long id) {
        ApiResponse<SchemeWorkItems> response = new ApiResponse<>();
        Optional<SchemeWorkItems> optionalSchemeWorkItems = schemeWorkItemsRepository.findById(id);
        if (optionalSchemeWorkItems.isEmpty()) {
            response.setMessage("SchemeWorkItems is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        SchemeWorkItems schemeWorkItems = optionalSchemeWorkItems.get();
        response.setData(schemeWorkItems);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<SchemeWork>> findAll() {
        ApiResponse<SchemeWork> response = new ApiResponse<>();
        Optional<SchemeWork> optionalSchemeWork = schemeWorkRepository.findAll().stream().findFirst();
        if (optionalSchemeWork.isEmpty()) {
            response.setMessage("SchemeWork is not found");
            return ResponseEntity.status(404).body(response);
        }
        response.setData(optionalSchemeWork.get());
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<SchemeWorkItems>> update(SchemeWorkItems newSchemeWorkItems){
        ApiResponse<SchemeWorkItems> response=new ApiResponse<>();
        Optional<SchemeWorkItems> optionalSchemeWorkItems = schemeWorkItemsRepository.findById(newSchemeWorkItems.getId());
        if (optionalSchemeWorkItems.isEmpty()) {
            response.setMessage("SchemeWorkItems is not found by id: "+newSchemeWorkItems.getId());
            return ResponseEntity.status(404).body(response);
        }
        SchemeWorkItems fromDb = optionalSchemeWorkItems.get();
        if (newSchemeWorkItems.getTitle()!=null) {
            fromDb.setTitle(newSchemeWorkItems.getTitle());
        }
        if (newSchemeWorkItems.getDescription()!=null){
            fromDb.setDescription(newSchemeWorkItems.getDescription());
        }
        response.setData(schemeWorkItemsRepository.save(fromDb));
        response.setMessage("Updated");
        return ResponseEntity.status(201).body(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (schemeWorkItemsRepository.findById(id).isEmpty()) {
            response.setMessage("SchemeWorkItems is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        schemeWorkItemsRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }

}
