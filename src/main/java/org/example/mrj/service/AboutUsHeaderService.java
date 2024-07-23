package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.AboutUsMainDTO;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.AboutUsHeader;
import org.example.mrj.repository.AboutUsHeaderRepository;
import org.springframework.http.HttpStatus;
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
        Optional<AboutUsHeader> optionalAboutUsHeader = aboutUsHeaderRepository.findAll().stream().findFirst();
        if (photoFiles.size()!=2){
            response.setMessage("The number of images must be two to create About us");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        try {
            AboutUsHeader aboutUsHeader = objectMapper.readValue(strAboutUsHeader, AboutUsHeader.class);
            if (optionalAboutUsHeader.isPresent()) {
                return update(aboutUsHeader);
            }
            aboutUsHeader.setPhotos(new ArrayList<>());
            for (MultipartFile photoFile : photoFiles) {
                aboutUsHeader.getPhotos().add(photoService.save(photoFile));
            }
            AboutUsHeader save = aboutUsHeaderRepository.save(aboutUsHeader);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<AboutUsHeader>> find() {
        ApiResponse<AboutUsHeader> response = new ApiResponse<>();
        Optional<AboutUsHeader> optionalAboutUsHeader = aboutUsHeaderRepository.findAll().stream().findFirst();
        if (optionalAboutUsHeader.isEmpty()) {
            response.setMessage("AboutUsHeader is not found");
            return ResponseEntity.status(404).body(response);
        }
        AboutUsHeader aboutUsHeader = optionalAboutUsHeader.get();
        response.setMessage("Found");
        response.setData(aboutUsHeader);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<AboutUsMainDTO>> findForMainPage(){
        ApiResponse<AboutUsMainDTO> response=new ApiResponse<>();
        Optional<AboutUsHeader> optionalAboutUsHeader = aboutUsHeaderRepository.findAll().stream().findFirst();
        if (optionalAboutUsHeader.isEmpty()) {
            response.setMessage("AboutUs is not found");
            return ResponseEntity.status(404).body(response);
        }
        AboutUsHeader aboutUsHeader = optionalAboutUsHeader.get();
        response.setMessage("Found");
        response.setData(new AboutUsMainDTO(aboutUsHeader));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<AboutUsHeader>> update(AboutUsHeader newAboutUsHeader) {
        ApiResponse<AboutUsHeader> response = new ApiResponse<>();
        Optional<AboutUsHeader> optionalAboutUsHeader = aboutUsHeaderRepository.findAll().stream().findFirst();
        if (optionalAboutUsHeader.isEmpty()) {
            response.setMessage("AboutUsHeader is not found");
            return ResponseEntity.status(404).body(response);
        }
        AboutUsHeader aboutUsHeader = optionalAboutUsHeader.get();
        if (newAboutUsHeader.getFormName()!=null){
            aboutUsHeader.setFormName(newAboutUsHeader.getFormName());
        }
        if (newAboutUsHeader.getTitle() != null) {
            aboutUsHeader.setTitle(newAboutUsHeader.getTitle());
        }
        if (newAboutUsHeader.getSubtitle() != null) {
            aboutUsHeader.setSubtitle(newAboutUsHeader.getSubtitle());
        }
        if (newAboutUsHeader.getDescription() != null) {
            aboutUsHeader.setDescription(newAboutUsHeader.getDescription());
        }
        response.setData(aboutUsHeaderRepository.save(aboutUsHeader));
        return ResponseEntity.status(201).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<AboutUsHeader> optionalAboutUsHeader = aboutUsHeaderRepository.findAll().stream().findFirst();
        if (optionalAboutUsHeader.isEmpty()) {
            response.setMessage("AboutUsHeader is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = optionalAboutUsHeader.get().getId();
        aboutUsHeaderRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }

//    public ResponseEntity<ApiResponse<?>> changeActive() {
//        ApiResponse<?> response = new ApiResponse<>();
//        Optional<AboutUsHeader> optionalAboutUsHeader = aboutUsHeaderRepository.findAll().stream().findFirst();
//        if (optionalAboutUsHeader.isEmpty()) {
//            response.setMessage("AboutUsHeader is not found");
//            return ResponseEntity.status(404).body(response);
//        }
//        AboutUsHeader aboutUsHeader = optionalAboutUsHeader.get();
//        boolean active = !aboutUsHeader.isActive();
//        aboutUsHeaderRepository.changeActive(aboutUsHeader.getId(), active);
//        response.setMessage("Successfully changed! Current aboutUsHeader active: " + active);
//        return ResponseEntity.status(200).body(response);
//    }

}
