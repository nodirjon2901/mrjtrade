package org.example.mrj.service;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.ApplicationFormText;
import org.example.mrj.repository.ApplicationTextRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationFormTextService {

    private final ApplicationTextRepository applicationTextRepository;

    public ResponseEntity<ApiResponse<ApplicationFormText>> create(ApplicationFormText applicationFormText) {
        ApiResponse<ApplicationFormText> response = new ApiResponse<>();
        Optional<ApplicationFormText> firstApplicationFormTextOptional = applicationTextRepository.findAll().stream().findFirst();
        if (firstApplicationFormTextOptional.isPresent()) {
            return update(applicationFormText);
        }
        ApplicationFormText save = applicationTextRepository.save(applicationFormText);
        response.setData(save);
        return ResponseEntity.status(201).body(response);
    }

    public ResponseEntity<ApiResponse<ApplicationFormText>> find() {
        ApiResponse<ApplicationFormText> response = new ApiResponse<>();
        Optional<ApplicationFormText> optionalApplicationFormText = applicationTextRepository.findAll().stream().findFirst();
        if (optionalApplicationFormText.isEmpty()) {
            response.setMessage("ApplicationFormText is not found");
            return ResponseEntity.status(404).body(response);
        }
        ApplicationFormText applicationFormText = optionalApplicationFormText.get();
        response.setData(applicationFormText);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<ApplicationFormText>> update(ApplicationFormText applicationFormText) {
        ApiResponse<ApplicationFormText> response = new ApiResponse<>();
        Optional<ApplicationFormText> optionalApplicationFormText = applicationTextRepository.findAll().stream().findFirst();
        if (optionalApplicationFormText.isEmpty()) {
            response.setMessage("ApplicationFormText is not found");
            return ResponseEntity.status(404).body(response);
        }
        ApplicationFormText oldApplicationFormText = optionalApplicationFormText.get();
        oldApplicationFormText.setTitle(applicationFormText.getTitle());
        oldApplicationFormText.setDescription(applicationFormText.getDescription());
        ApplicationFormText save = applicationTextRepository.save(oldApplicationFormText);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<ApplicationFormText> response = new ApiResponse<>();
        Optional<ApplicationFormText> optionalApplicationFormText = applicationTextRepository.findAll().stream().findFirst();
        if (optionalApplicationFormText.isEmpty()) {
            response.setMessage("ApplicationFormText is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optionalApplicationFormText.get().getId();
        applicationTextRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

}
