package org.example.mrj.service;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Application;
import org.example.mrj.repository.ApplicationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ResponseEntity<ApiResponse<Application>> create(Application application) {
        ApiResponse<Application> response = new ApiResponse<>();
        Application save = applicationRepository.save(application);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (applicationRepository.findById(id).isEmpty()) {
            response.setMessage("Application is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        applicationRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }

}
