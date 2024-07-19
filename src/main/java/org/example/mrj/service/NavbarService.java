package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Navbar;
import org.example.mrj.domain.entity.Photo;
import org.example.mrj.repository.NavbarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class NavbarService {

    private final NavbarRepository navbarRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<Navbar>> create(String strNavbar, MultipartFile photoFile) {
        ApiResponse<Navbar> response = new ApiResponse<>();
        Optional<Navbar> firstNavbarOptional = navbarRepository.findAll().stream().findFirst();
        if (firstNavbarOptional.isPresent()) {
            response.setMessage("Navbar already exists");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            Navbar navbar = objectMapper.readValue(strNavbar, Navbar.class);
            navbar.setLogo(photoService.save(photoFile));
            Navbar save = navbarRepository.save(navbar);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(400).body(response);
        }
    }

    public ResponseEntity<ApiResponse<Navbar>> find() {
        ApiResponse<Navbar> response = new ApiResponse<>();
        Optional<Navbar> optionalNavbar = navbarRepository.findAll().stream().findFirst();
        if (optionalNavbar.isEmpty()) {
            response.setMessage("Navbar is not found");
            return ResponseEntity.status(404).body(response);
        }
        Navbar navbar = optionalNavbar.get();
        response.setMessage("Found");
        response.setData(navbar);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Navbar>> update(Navbar navbar)
    {
        ApiResponse<Navbar> response = new ApiResponse<>();
        Optional<Navbar> optionalNavbarOptional = navbarRepository.findAll().stream().findFirst();
        if (optionalNavbarOptional.isPresent()) {
            navbar.setId(optionalNavbarOptional.get().getId());
            navbar.setLogo(optionalNavbarOptional.get().getLogo());
            navbarRepository.save(navbar);
            response.setMessage("Updated");
            response.setData(navbar);
            return ResponseEntity.status(200).body(response);
        }
        response.setMessage("Navbar is not found");
        return ResponseEntity.status(404).body(response);
    }
/*
    public ResponseEntity<ApiResponse<Navbar>> update(String newJson, MultipartFile newPhoto) {
        ApiResponse<Navbar> response = new ApiResponse<>();
        Optional<Navbar> optionalNavbar = navbarRepository.findAll().stream().findFirst();
        if (optionalNavbar.isEmpty()) {
            response.setMessage("Navbar is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optionalNavbar.get().getId();
        Photo oldPhoto = optionalNavbar.get().getPhoto();
        Navbar newNavbar = new Navbar();

        try {
            if (newJson != null) {
                newNavbar = objectMapper.readValue(newJson, Navbar.class);
                if (newPhoto == null || !(newPhoto.getSize() > 0)) {
                    newNavbar.setPhoto(oldPhoto);
                }
                newNavbar.setId(id);
            } else {
                newNavbar = navbarRepository.findById(id).get();
            }

            if (newPhoto != null && newPhoto.getSize() > 0) {
                newNavbar.setPhoto(photoService.save(newPhoto));
            }
            Navbar save = navbarRepository.save(newNavbar);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }
*/

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Navbar> optionalNavbar = navbarRepository.findAll().stream().findFirst();
        if (optionalNavbar.isEmpty()) {
            response.setMessage("Navbar is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optionalNavbar.get().getId();
        navbarRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }

}
