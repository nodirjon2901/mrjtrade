package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.NewnessWrapper;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.NewDTO;
import org.example.mrj.domain.entity.New;
import org.example.mrj.domain.entity.NewOption;
import org.example.mrj.service.NewService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewController {

    private final NewService newService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<New>> createNew(
            @RequestParam(value = "json") String newness,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return newService.create(newness, photo);
    }

    @PostMapping("/add-block/{newId}")
    public ResponseEntity<ApiResponse<New>> addBlock(
            @PathVariable Long newId,
            @RequestParam(value = "json") String newOption,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return newService.addNewOption(newId, newOption, photo);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<New>> findBySlug(
            @PathVariable String slug
    ) {
        return newService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findAll() {
        return newService.findAll();
    }

    @GetMapping("/get-page")
    public ResponseEntity<ApiResponse<Page<NewDTO>>> findAllByPageNation(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "12") Integer size
    ) {
        return newService.findAllByPageNation(page, size);
    }

    @GetMapping("/get-all-other/{newSlug}")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findOtherFourNews(
            @PathVariable String newSlug
    ) {
        return newService.findOtherFourNews(newSlug);
    }

    @GetMapping("/get-four")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findFourNews() {
        return newService.findFourNews();
    }

    @PutMapping("/change-order")
    public ResponseEntity<ApiResponse<List<NewDTO>>> changeOrder(
            @RequestBody List<NewnessWrapper> newnessWrapperList
    ) {
        return newService.changeOrder(newnessWrapperList);
    }

    @PutMapping("/change-block-order")
    public ResponseEntity<ApiResponse<List<NewOption>>> changeOrderBlocks(
            @RequestBody List<NewOption> newOptionList
    ) {
        return newService.changeNewOptionOrder(newOptionList);
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<New>> update(
            @RequestBody New newness
    ) {
        return newService.update(newness);
    }

    @PutMapping("/update-block")
    public ResponseEntity<ApiResponse<New>> updateBlock(
            @RequestBody NewOption newOption
    ) {
        return newService.updateNewOption(newOption);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return newService.changeActive(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteNew(
            @PathVariable Long id
    ) {
        return newService.deleteById(id);
    }

    @DeleteMapping("/delete-block/{id}")
    public ResponseEntity<ApiResponse<?>> deleteBlock(
            @PathVariable Long id
    ) {
        return newService.deleteBlockById(id);
    }


}
