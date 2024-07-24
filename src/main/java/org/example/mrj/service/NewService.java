package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.NewnessWrapper;
import org.example.mrj.domain.PartnerWrapper;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.NewDTO;
import org.example.mrj.domain.entity.AboutUsHeader;
import org.example.mrj.domain.entity.New;
import org.example.mrj.domain.entity.Partner;
import org.example.mrj.exception.NotFoundException;
import org.example.mrj.repository.NewRepository;
import org.example.mrj.util.SlugUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class NewService {

    private final NewRepository newRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;

    public ResponseEntity<ApiResponse<New>> create(String strNew, MultipartFile mainPhoto, List<MultipartFile> photoFiles) {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<Integer> maxOrderNum = newRepository.getMaxOrderNum();
        try {
            New newness = objectMapper.readValue(strNew, New.class);
            newness.setGallery(new ArrayList<>());
            newness.setActive(true);
            newness.setOrderNum(maxOrderNum.map(num -> num + 1).orElse(1));
            newness.setPhoto(photoService.save(mainPhoto));
            for (MultipartFile photo : photoFiles) {
                newness.getGallery().add(photoService.save(photo));
            }
            New save = newRepository.save(newness);
            String slug = save.getId() + "-" + SlugUtil.makeSlug(save.getTitle());
            newRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(409).body(response);
        }
    }

    public ResponseEntity<ApiResponse<New>> findById(Long id) {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findById(id);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        response.setMessage("Found");
        response.setData(newness);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<New>> findBySlug(String slug) {
        ApiResponse<New> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findBySlug(slug);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        response.setMessage("Found");
        response.setData(newness);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> findAll() {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        List<New> all = newRepository.findAll();
        response.setData(new ArrayList<>());
        all.forEach(newness -> response.getData().add(new NewDTO(newness)));
        response.setMessage("Found " + all.size() + " new(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Page<NewDTO>>> findAllByPageNation(int page, int size) {
        ApiResponse<Page<NewDTO>> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<New> all = newRepository.findAll(pageable);
        Page<NewDTO> map = all.map(NewDTO::new);
        response.setData(map);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> findFourNews() {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        List<New> all = newRepository.findAllByOrderByIdAsc();
        response.setData(new ArrayList<>());
        all.stream().limit(4).toList().forEach(newness -> response.getData().add(new NewDTO(newness)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> findOtherFourNews(String newSlug) {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        List<New> all = newRepository.findAllByOrderByIdAsc();
        response.setData(new ArrayList<>());
        all.stream()
                .filter(newness -> !newness.getSlug().equals(newSlug))
                .limit(4).toList()
                .forEach(newness -> response.getData().add(new NewDTO(newness)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<New>> update(New newness) {
        ApiResponse<New> response = new ApiResponse<>();
        New existingNew = newRepository.findById(newness.getId()).orElseThrow(() -> new NotFoundException("Newness is not found by id: " + newness.getId()));
        String slug = newness.getId() + "-" + SlugUtil.makeSlug(newness.getTitle());
        existingNew.setSlug(slug);
        if (newness.getTitle() != null) {
            existingNew.setTitle(newness.getTitle());
        }
        if (newness.getBody() != null) {
            existingNew.setBody(newness.getBody());
        }
        if (newness.getDate() != null) {
            existingNew.setDate(newness.getDate());
        }
        response.setData(newRepository.save(existingNew));
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<ApiResponse<List<NewDTO>>> changeOrder(List<NewnessWrapper> newnessWrapperList) {
        ApiResponse<List<NewDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<New> dbAll = newRepository.findAll();
        if (dbAll.size() != newnessWrapperList.size()) {
            response.setMessage("In database have: " + dbAll.size() + " newness. But you send " + newnessWrapperList.size() + " order number(s)");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        for (New db : newRepository.findAll()) {
            Long dbId = db.getId();
            for (NewnessWrapper newnessWrapper : newnessWrapperList) {
                if (newnessWrapper.id().equals(dbId)) {
                    db.setOrderNum(newnessWrapper.orderNum());
                    response.getData().add(new NewDTO(newRepository.save(db)));
                }
            }
        }
        response.getData().sort(Comparator.comparing(NewDTO::getOrderNum));
        return ResponseEntity.status(201).body(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteById(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (newRepository.findById(id).isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        newRepository.deleteById(id);
        response.setMessage("Successfully deleted");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<New> optionalNew = newRepository.findById(id);
        if (optionalNew.isEmpty()) {
            response.setMessage("New is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        New newness = optionalNew.get();
        boolean active = !newness.isActive();
        newRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current new active: " + active);
        return ResponseEntity.status(200).body(response);
    }
}
