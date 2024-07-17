package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Address;
import org.example.mrj.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Address>> create(
            @RequestBody Address address
    ) {
        return addressService.create(address);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse<Address>> findById(
            @PathVariable Long id
    ) {
        return addressService.findById(id);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<Address>>> findAll() {
        return addressService.findAll();
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Address>> update(
            @PathVariable Long id,
            @RequestBody Address address
    ) {
        return addressService.update(id, address);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return addressService.delete(id);
    }

}
