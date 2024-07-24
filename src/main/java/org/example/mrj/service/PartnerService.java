package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.PartnerWrapper;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.PartnerDTO;
import org.example.mrj.domain.entity.Partner;
import org.example.mrj.repository.PartnerRepository;
import org.example.mrj.util.SlugUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PartnerService {

    private final PartnerRepository partnerRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<Partner>> create(String strPartner, MultipartFile photoFile) {
        ApiResponse<Partner> response = new ApiResponse<>();
        Optional<Integer> maxOrderNum = partnerRepository.getMaxOrderNum();
        try {
            Partner partner = objectMapper.readValue(strPartner, Partner.class);
            partner.setPhoto(photoService.save(photoFile));
            partner.setOrderNum(maxOrderNum.map(num -> num + 1).orElse(1));
            partner.setActive(true);
            Partner save = partnerRepository.save(partner);
            String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getTitle());
            partnerRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.status(201).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<Partner>> findById(Long id) {
        ApiResponse<Partner> response = new ApiResponse<>();
        Optional<Partner> optionalPartner = partnerRepository.findById(id);
        if (optionalPartner.isEmpty()) {
            response.setMessage("Partner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Partner partner = optionalPartner.get();
        response.setData(partner);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Partner>> findBySlug(String slug) {
        ApiResponse<Partner> response = new ApiResponse<>();
        Optional<Partner> optionalPartner = partnerRepository.findBySlug(slug);
        if (optionalPartner.isEmpty()) {
            response.setMessage("Partner is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        Partner partner = optionalPartner.get();
        response.setData(partner);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<Partner>>> findAll() {
        ApiResponse<List<Partner>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAllByOrderByOrderNum();
        all.forEach(partner -> response.getData().add(partner));
        response.setMessage("Found " + all.size() + " partner(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<PartnerDTO>>> findPartnerForMainPage() {
        ApiResponse<List<PartnerDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAllByOrderByOrderNum();
        all.stream()
                .filter(Partner::isActive)
                .forEach(partner -> response.getData().add(new PartnerDTO(partner)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<Partner>>> findOtherPartnerBySlug(String partnerSlug) {
        ApiResponse<List<Partner>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> all = partnerRepository.findAllByOrderByIdAsc();
        all.stream()
                .filter(partner -> partner.isActive() && !partner.getSlug().equals(partnerSlug)).toList()
                .forEach(partner -> response.getData().add(partner));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Partner>> update(Partner newPartner) {
        ApiResponse<Partner> response = new ApiResponse<>();
        return partnerRepository.findById(newPartner.getId())
                .map(existingPartner -> {
                    if (newPartner.getTitle() != null) {
                        existingPartner.setTitle(newPartner.getTitle());
                        String slug = existingPartner.getId() + "-" + SlugUtil.makeSlug(existingPartner.getTitle());
                        existingPartner.setSlug(slug);
                    }
                    if (newPartner.getMainDescription() != null) {
                        existingPartner.setMainDescription(newPartner.getMainDescription());
                    }
                    if (newPartner.getDescription() != null) {
                        existingPartner.setDescription(newPartner.getDescription());
                    }
//                    if (newPartner.getOrderNum() != null) {
//                         replaceOrderNum(newPartner.getOrderNum(), existingPartner.getOrderNum());
//                         existingPartner.setOrderNum(newPartner.getOrderNum());
//                    }
                    response.setData(partnerRepository.save(existingPartner));
                    return ResponseEntity.ok().body(response);
                }).orElseGet(() -> {
                    response.setMessage("Partner is not found by id: " + newPartner.getId());
                    return ResponseEntity.status(404).body(response);
                });
    }

//    private void replaceOrderNum(Integer orderNum, Integer oldOrderNum) {
//        long count = partnerRepository.count();
//        if (orderNum > count && orderNum <= 0) {
//            throw new RuntimeException("Order number is invalid");
//        }
//        Optional<Partner> optionalPartner = partnerRepository.findByOrderNum(orderNum);
//        if (optionalPartner.isPresent()) {
//            Partner oldPartner = optionalPartner.get();
//            oldPartner.setOrderNum(oldOrderNum);
//            partnerRepository.save(oldPartner);
//        }
//    }

    public ResponseEntity<ApiResponse<List<Partner>>> changeOrder(List<PartnerWrapper> newPartnerList) {
        ApiResponse<List<Partner>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Partner> dbAll = partnerRepository.findAll();
        if (partnerRepository.findAll().size()!=newPartnerList.size()) {
            response.setMessage("In database have: " + dbAll.size() + " partner(s). But you send " + newPartnerList.size() + " order number(s)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        for (Partner db : partnerRepository.findAll()) {
            Long dbId= db.getId();
            for (PartnerWrapper newPartner : newPartnerList) {
                if (newPartner.id().equals(dbId)){
                    db.setOrderNum(newPartner.orderNum());
                    response.getData().add(partnerRepository.save(db));
                }
            }
        }
        response.getData().sort(Comparator.comparing(Partner::getOrderNum));
        return ResponseEntity.status(201).body(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (partnerRepository.findById(id).isEmpty()) {
            response.setMessage("Partner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        partnerRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Partner> optionalPartner = partnerRepository.findById(id);
        if (optionalPartner.isEmpty()) {
            response.setMessage("Partner is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Partner partner = optionalPartner.get();
        boolean active = !partner.isActive();
        partnerRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current partner active: " + active);
        return ResponseEntity.status(200).body(response);
    }


}
