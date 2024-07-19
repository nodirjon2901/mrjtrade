package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.ContactBody;
import org.example.mrj.service.ContactBodyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
@RequiredArgsConstructor
public class ContactBodyController {

    private final ContactBodyService contactBodyService;

    @PostMapping("/body/create")
    public ResponseEntity<ApiResponse<ContactBody>> createBody(
            @RequestBody ContactBody contactBody
    ) {
        return contactBodyService.create(contactBody);
    }

    @GetMapping("/body/get")
    public ResponseEntity<ApiResponse<ContactBody>> find() {
        return contactBodyService.find();
    }

    @PutMapping("/body/update")
    public ResponseEntity<ApiResponse<ContactBody>> update(
            @RequestBody ContactBody contactBody
    ) {
        return contactBodyService.update(contactBody);
    }

    @DeleteMapping("/body/delete")
    public ResponseEntity<ApiResponse<?>> delete() {
        return contactBodyService.delete();
    }

}
