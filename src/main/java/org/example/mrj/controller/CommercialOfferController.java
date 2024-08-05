package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.CommercialOffer;
import org.example.mrj.service.CommercialOfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@Controller
@RequestMapping("/commercial-offer")
public class CommercialOfferController
{

    private final CommercialOfferService offerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommercialOffer>> addCommercialOffer(
            @RequestBody CommercialOffer commercialOffer)
    {
        return offerService.add(commercialOffer);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CommercialOffer>>> getAll()
    {
        return offerService.getAll();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> deleteCommercialOffer(
            @PathVariable Long id)
    {
        return offerService.delete(id);
    }
}
