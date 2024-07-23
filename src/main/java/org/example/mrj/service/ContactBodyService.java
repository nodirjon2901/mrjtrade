package org.example.mrj.service;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.ContactBody;
import org.example.mrj.repository.ContactBodyRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactBodyService {

    private final ContactBodyRepository contactBodyRepository;

    public ResponseEntity<ApiResponse<ContactBody>> create(ContactBody contactBody) {
        ApiResponse<ContactBody> response = new ApiResponse<>();
        Optional<ContactBody> optionalContactBody = contactBodyRepository.findAll().stream().findFirst();
        if (optionalContactBody.isPresent()) {
            return update(contactBody);
        }
        contactBody.setAddable(true);
        ContactBody save = contactBodyRepository.save(contactBody);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<ContactBody>> find() {
        ApiResponse<ContactBody> response = new ApiResponse<>();
        Optional<ContactBody> optionalContactBody = contactBodyRepository.findAll().stream().findFirst();
        if (optionalContactBody.isEmpty()) {
            response.setMessage("ContactBody is not found");
            return ResponseEntity.status(404).body(response);
        }
        ContactBody contactBody = optionalContactBody.get();
        response.setData(contactBody);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<ContactBody>> update(ContactBody contactBody) {
        ApiResponse<ContactBody> response = new ApiResponse<>();
        Optional<ContactBody> optionalContactBody = contactBodyRepository.findAll().stream().findFirst();
        if (optionalContactBody.isEmpty()) {
            response.setMessage("ContactBody is not found");
            return ResponseEntity.status(404).body(response);
        }
        ContactBody oldContactBody = optionalContactBody.get();
        oldContactBody.setAddress(contactBody.getAddress());
        oldContactBody.setPhone(contactBody.getPhone());
        oldContactBody.setEmail(contactBody.getEmail());
        oldContactBody.setSchedule(contactBody.getSchedule());
        oldContactBody.setAddable(true);
        ContactBody save = contactBodyRepository.save(oldContactBody);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<ContactBody> optionalContactBody = contactBodyRepository.findAll().stream().findFirst();
        if (optionalContactBody.isEmpty()) {
            response.setMessage("ContactBody is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optionalContactBody.get().getId();
        contactBodyRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }


}
