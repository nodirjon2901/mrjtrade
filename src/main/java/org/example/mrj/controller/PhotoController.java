package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.PhotoDTO;
import org.example.mrj.service.PhotoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/photo")
@RequiredArgsConstructor
public class PhotoController
{

    private final PhotoService photoService;

    @GetMapping("/{name-or-id}")
    public ResponseEntity<byte[]> getPhoto(@PathVariable(name = "name-or-id") String nameOrId)
    {
        return photoService.findByNameOrId(nameOrId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PhotoDTO>> updatePhoto(
            @PathVariable(name = "id") Long id,
            @RequestParam(value = "new-photo") MultipartFile newPhoto)
    {
        return photoService.update(id, newPhoto);
    }
}
