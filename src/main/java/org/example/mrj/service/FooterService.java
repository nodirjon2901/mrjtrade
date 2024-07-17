package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Footer;
import org.example.mrj.repository.FooterRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FooterService {

    private final FooterRepository footerRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Footer>> create(String strFooter,
                                                      MultipartFile logoIcon,
                                                      MultipartFile tgIcon,
                                                      MultipartFile facebookIcon,
                                                      MultipartFile instIcon,
                                                      MultipartFile youtubeIcon,
                                                      MultipartFile creatorIcon) {
        ApiResponse<Footer> response = new ApiResponse<>();
        Optional<Footer> firstFooterOptional = footerRepository.findAll().stream().findFirst();
        if (firstFooterOptional.isPresent()) {
            return update(strFooter, logoIcon, tgIcon, facebookIcon, instIcon, youtubeIcon, creatorIcon);
        }
        try {
            Footer footer = objectMapper.readValue(strFooter, Footer.class);
            footer.setLogoIconUrl(photoService.save(logoIcon).getHttpUrl());
            footer.setTelegramIconUrl(photoService.save(tgIcon).getHttpUrl());
            footer.setFacebookIconUrl(photoService.save(facebookIcon).getHttpUrl());
            footer.setInstagramIconUrl(photoService.save(instIcon).getHttpUrl());
            footer.setYouTubeIconUrl(photoService.save(youtubeIcon).getHttpUrl());
            footer.setCreatorIconUrl(photoService.save(creatorIcon).getHttpUrl());
            Footer save = footerRepository.save(footer);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<Footer>> find() {
        ApiResponse<Footer> response = new ApiResponse<>();
        Optional<Footer> firstFooterOptional = footerRepository.findAll().stream().findFirst();
        if (firstFooterOptional.isEmpty()) {
            response.setMessage("Footer is not found");
            return ResponseEntity.status(404).body(response);
        }
        Footer footer = firstFooterOptional.get();
        response.setMessage("Found");
        response.setData(footer);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete() {
        ApiResponse<Footer> response = new ApiResponse<>();
        Optional<Footer> firstFooterOptional = footerRepository.findAll().stream().findFirst();
        if (firstFooterOptional.isEmpty()) {
            response.setMessage("Footer is not found");
            return ResponseEntity.status(404).body(response);
        }
        Long id = firstFooterOptional.get().getId();
        footerRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Footer>> update(String newJson,
                                                      MultipartFile newLogoIcon,
                                                      MultipartFile newTgIcon,
                                                      MultipartFile newFacebookIcon,
                                                      MultipartFile newInstIcon,
                                                      MultipartFile newYoutubeIcon,
                                                      MultipartFile newCreatorIcon) {
        ApiResponse<Footer> response = new ApiResponse<>();
        Optional<Footer> firstFooterOptional = footerRepository.findAll().stream().findFirst();
        if (firstFooterOptional.isEmpty()) {
            response.setMessage("Footer is not found");
            return ResponseEntity.status(404).body(response);
        }
        Footer olfFooter = firstFooterOptional.get();

        Footer newFooter = new Footer();
        try {
            if (newJson != null) {
                newFooter = objectMapper.readValue(newJson, Footer.class);
                newFooter.setId(olfFooter.getId());
            } else {
                newFooter = olfFooter;
            }

            if (newLogoIcon == null || newLogoIcon.isEmpty()) {
                newFooter.setLogoIconUrl(olfFooter.getLogoIconUrl());
            } else {
                newFooter.setLogoIconUrl(photoService.save(newLogoIcon).getHttpUrl());
            }

            if (newTgIcon == null || newTgIcon.isEmpty()) {
                newFooter.setTelegramIconUrl(olfFooter.getTelegramIconUrl());
            } else {
                newFooter.setTelegramIconUrl(photoService.save(newTgIcon).getHttpUrl());
            }

            if (newFacebookIcon == null || newFacebookIcon.isEmpty()) {
                newFooter.setFacebookIconUrl(olfFooter.getFacebookIconUrl());
            } else {
                newFooter.setFacebookIconUrl(photoService.save(newFacebookIcon).getHttpUrl());
            }

            if (newInstIcon == null || newInstIcon.isEmpty()) {
                newFooter.setInstagramIconUrl(olfFooter.getInstagramIconUrl());
            } else {
                newFooter.setInstagramIconUrl(photoService.save(newInstIcon).getHttpUrl());
            }

            if (newYoutubeIcon == null || newYoutubeIcon.isEmpty()) {
                newFooter.setYouTubeIconUrl(olfFooter.getYouTubeIconUrl());
            } else {
                newFooter.setYouTubeIconUrl(photoService.save(newYoutubeIcon).getHttpUrl());
            }

            if (newCreatorIcon == null || newCreatorIcon.isEmpty()) {
                newFooter.setCreatorIconUrl(olfFooter.getCreatorIconUrl());
            } else {
                newFooter.setCreatorIconUrl(photoService.save(newCreatorIcon).getHttpUrl());
            }

            Footer save = footerRepository.save(newFooter);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

}
