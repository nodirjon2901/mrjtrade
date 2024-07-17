package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.AboutUsHeader;
import org.example.mrj.domain.entity.New;
import org.example.mrj.repository.AboutUsHeaderRepository;
import org.example.mrj.util.SlugUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AboutUsHeaderService {

    private final AboutUsHeaderRepository aboutUsHeaderRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<AboutUsHeader>> create(String strAboutUsHeader, List<MultipartFile> photoFiles) {
        ApiResponse<AboutUsHeader> response = new ApiResponse<>();
        try {
            AboutUsHeader aboutUsHeader = objectMapper.readValue(strAboutUsHeader, AboutUsHeader.class);
            aboutUsHeader.setPhotoUrls(new ArrayList<>());
            for (MultipartFile photo : photoFiles) {
                aboutUsHeader.getPhotoUrls().add(photoService.save(photo).getHttpUrl());
            }
            AboutUsHeader save = aboutUsHeaderRepository.save(aboutUsHeader);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<AboutUsHeader>> findById(Long id) {
        ApiResponse<AboutUsHeader> response = new ApiResponse<>();
        Optional<AboutUsHeader> optionalAboutUsHeader = aboutUsHeaderRepository.findById(id);
        if (optionalAboutUsHeader.isEmpty()) {
            response.setMessage("AboutUsHeader is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        AboutUsHeader aboutUsHeader = optionalAboutUsHeader.get();
        response.setData(aboutUsHeader);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<AboutUsHeader>>> findAll() {
        ApiResponse<List<AboutUsHeader>> response = new ApiResponse<>();
        List<AboutUsHeader> all = aboutUsHeaderRepository.findAll();
        response.setData(new ArrayList<>());
        all.forEach(aboutUsHeader -> response.getData().add(aboutUsHeader));
        response.setMessage("Found " + all.size() + " aboutUsHeader");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<AboutUsHeader>> update(Long id, String newJson, List<MultipartFile> newPhoto) {
        ApiResponse<AboutUsHeader> response = new ApiResponse<>();
        Optional<AboutUsHeader> optionalAboutUsHeader = aboutUsHeaderRepository.findById(id);
        if (optionalAboutUsHeader.isEmpty()) {
            response.setMessage("AboutUsHeader is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        List<String> oldPhotoUrls = optionalAboutUsHeader.get().getPhotoUrls();
        AboutUsHeader newAboutUsHeader = new AboutUsHeader();

        try {
            if (newJson != null) {
                newAboutUsHeader = objectMapper.readValue(newJson, AboutUsHeader.class);
                if (newPhoto == null || newPhoto.isEmpty()) {
                    newAboutUsHeader.setPhotoUrls(oldPhotoUrls);
                }
                newAboutUsHeader.setId(id);
            } else {
                newAboutUsHeader = aboutUsHeaderRepository.findById(id).get();
            }
            if (newPhoto != null && !newPhoto.isEmpty()) {
                newAboutUsHeader.setPhotoUrls(new ArrayList<>());
                for (MultipartFile photo : newPhoto) {
                    newAboutUsHeader.getPhotoUrls().add(photoService.save(photo).getHttpUrl());
                }
            }
            AboutUsHeader save = aboutUsHeaderRepository.save(newAboutUsHeader);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (aboutUsHeaderRepository.findById(id).isEmpty()) {
            response.setMessage("AboutUsHeader is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        aboutUsHeaderRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

}
