package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.ApplicationFormText;
import org.example.mrj.service.ApplicationFormTextService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/application-form-text")
@RequiredArgsConstructor
public class ApplicationFormTexController {

    private final ApplicationFormTextService applicationFormTextService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ApplicationFormText>> createApplicationFormText(
            @RequestBody ApplicationFormText applicationFormText
    ) {
        return applicationFormTextService.create(applicationFormText);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<ApplicationFormText>> find() {
        return applicationFormTextService.find();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<ApplicationFormText>> update(
            @RequestBody ApplicationFormText applicationFormText
    ) {
        return applicationFormTextService.update(applicationFormText);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> delete() {
        return applicationFormTextService.delete();
    }

}
