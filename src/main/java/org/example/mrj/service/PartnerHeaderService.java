package org.example.mrj.service;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.PartnerHeader;
import org.example.mrj.repository.PartnerHeaderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PartnerHeaderService {

    private final PartnerHeaderRepository headerRepository;

    public ResponseEntity<ApiResponse<PartnerHeader>> create(PartnerHeader partnerHeader) {
        ApiResponse<PartnerHeader> response = new ApiResponse<>();
        Optional<PartnerHeader> optional = headerRepository.findAll().stream().findFirst();
        if (optional.isPresent()) {
            return update(partnerHeader);
        }
        PartnerHeader save = headerRepository.save(partnerHeader);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<PartnerHeader>> find() {
        ApiResponse<PartnerHeader> response = new ApiResponse<>();
        Optional<PartnerHeader> optional = headerRepository.findAll().stream().findFirst();
        if (optional.isEmpty()) {
            response.setMessage("PartnerHeader is not found");
            return ResponseEntity.status(404).body(response);
        }
        PartnerHeader partnerHeader = optional.get();
        response.setData(partnerHeader);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<PartnerHeader>> update(PartnerHeader partnerHeader) {
        ApiResponse<PartnerHeader> response = new ApiResponse<>();
        Optional<PartnerHeader> optional = headerRepository.findAll().stream().findFirst();
        if (optional.isEmpty()) {
            response.setMessage("PartnerHeader is not found");
            return ResponseEntity.status(404).body(response);
        }
        PartnerHeader oldHeader = optional.get();
        oldHeader.setDescription(partnerHeader.getDescription());
        PartnerHeader save = headerRepository.save(oldHeader);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<PartnerHeader> optional = headerRepository.findAll().stream().findFirst();
        if (optional.isEmpty()) {
            response.setMessage("Header is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optional.get().getId();
        headerRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

}
