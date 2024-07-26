package org.example.mrj.controller;

import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.NewnessWrapper;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.NewDTO;
import org.example.mrj.domain.entity.New;
import org.example.mrj.service.NewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewController
{

    private final NewService newService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<New>> createNew(
            @RequestParam(value = "json") String newness,
            MultipartHttpServletRequest request) throws ServletException, IOException
    {
        return newService.create(newness,request);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<New>> findBySlug(
            @PathVariable String slug
    )
    {
        return newService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "12") Integer size)
    {
        return newService.findAllByPageNation(page, size);

    }

    @GetMapping("/get-all-other/{newSlug}")
    public ResponseEntity<ApiResponse<List<NewDTO>>> findOtherFourNews(
            @PathVariable String newSlug
    )
    {
        return newService.findOtherFourNews(newSlug);
    }

    @PutMapping("/change-order")
    public ResponseEntity<ApiResponse<List<NewDTO>>> changeOrder(
            @RequestBody List<NewnessWrapper> newnessWrapperList
    )
    {
        return newService.changeOrder(newnessWrapperList);
    }


    @PutMapping("/update")
    public ResponseEntity<ApiResponse<New>> update(
            @RequestBody New newness)
    {
        return newService.update(newness);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteNew(
            @PathVariable Long id)
    {
        return newService.deleteById(id);
    }


}
