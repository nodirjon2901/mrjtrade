package org.example.mrj.service;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.ContactForm;
import org.example.mrj.repository.ContactFormRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactFormService {

    private final ContactFormRepository contactFormRepository;

    public ResponseEntity<ApiResponse<ContactForm>> create(ContactForm contactForm) {
        ApiResponse<ContactForm> response = new ApiResponse<>();
        Optional<ContactForm> contactFormOptional = contactFormRepository.findAll().stream().findFirst();
        if (contactFormOptional.isPresent()) {
            return update(contactForm);
        }
        ContactForm save = contactFormRepository.save(contactForm);
        response.setData(save);
        return ResponseEntity.status(201).body(response);
    }

    public ResponseEntity<ApiResponse<ContactForm>> find() {
        ApiResponse<ContactForm> response = new ApiResponse<>();
        Optional<ContactForm> contactFormOptional = contactFormRepository.findAll().stream().findFirst();
        if (contactFormOptional.isEmpty()) {
            response.setMessage("ContactForm is not found");
            return ResponseEntity.status(404).body(response);
        }
        ContactForm contactForm = contactFormOptional.get();
        response.setData(contactForm);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<ContactForm>> update(ContactForm contactForm) {
        ApiResponse<ContactForm> response = new ApiResponse<>();
        Optional<ContactForm> contactFormOptional = contactFormRepository.findAll().stream().findFirst();
        if (contactFormOptional.isEmpty()) {
            response.setMessage("ContactForm is not found");
            return ResponseEntity.status(404).body(response);
        }
        ContactForm oldContactForm = contactFormOptional.get();
        oldContactForm.setName(contactForm.getName());
        oldContactForm.setLocation(contactForm.getLocation());
        oldContactForm.setPhoneNumber(contactForm.getPhoneNumber());
        oldContactForm.setWorkTime(contactForm.getWorkTime());
        oldContactForm.setEmail(contactForm.getEmail());
        ContactForm save = contactFormRepository.save(oldContactForm);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<ContactForm> response = new ApiResponse<>();
        Optional<ContactForm> contactFormOptional = contactFormRepository.findAll().stream().findFirst();
        if (contactFormOptional.isEmpty()) {
            response.setMessage("ContactForm is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = contactFormOptional.get().getId();
        contactFormRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

}
