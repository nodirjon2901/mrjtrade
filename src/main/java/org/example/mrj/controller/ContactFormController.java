package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.ContactForm;
import org.example.mrj.service.ContactFormService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact-form")
@RequiredArgsConstructor
public class ContactFormController {

    private final ContactFormService contactFormService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ContactForm>> create(
            @RequestBody ContactForm contactForm
    ) {
        return contactFormService.create(contactForm);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<ContactForm>> find() {
        return contactFormService.find();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ContactForm>> update(
            @RequestBody ContactForm contactForm
    ) {
        return contactFormService.update(contactForm);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> delete() {
        return contactFormService.delete();
    }

}
