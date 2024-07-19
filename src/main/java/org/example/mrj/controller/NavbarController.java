package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Navbar;
import org.example.mrj.service.NavbarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/navbar")
@RequiredArgsConstructor
public class NavbarController {

    private final NavbarService navbarService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Navbar>> create(
            @RequestParam(value = "json") String navbar,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return navbarService.create(navbar, photo);
    }

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Navbar>> find() {
        return navbarService.find();
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Navbar>> update(
            @RequestBody Navbar navbar) {
        System.err.println("navbar = " + navbar);
        return navbarService.update(navbar);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> delete() {
        return navbarService.delete();
    }

}
