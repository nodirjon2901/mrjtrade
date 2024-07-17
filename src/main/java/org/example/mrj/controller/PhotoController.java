package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.exception.PhotoNotFoundException;
import org.example.mrj.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController {

    private final PhotoService photoService;

    @GetMapping("/{name-or-id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable(name = "name-or-id") String nameOrId) throws PhotoNotFoundException {
        return photoService.findByNameOrId(nameOrId);
    }

}
