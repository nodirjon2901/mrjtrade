package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.CheckInFormDTO;
import org.example.mrj.domain.dto.EventDTO;
import org.example.mrj.domain.entity.Event;
import org.example.mrj.domain.entity.EventCity;
import org.example.mrj.service.EventCityService;
import org.example.mrj.service.EventService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventCityService eventCityService;

    private final EventService eventService;

    @PostMapping("/create/{cityId}")
    public ResponseEntity<ApiResponse<Event>> create(
            @PathVariable Long cityId,
            @RequestParam(value = "json") String event,
            @RequestPart(value = "photo") MultipartFile photo
    ) {
        return eventService.create(cityId, event, photo);
    }

    @GetMapping("/get/{slug}")
    public ResponseEntity<ApiResponse<Event>> findBySlug(
            @PathVariable String slug
    ) {
        return eventService.findBySlug(slug);
    }

    @GetMapping("/get-all")
    public ResponseEntity<ApiResponse<List<EventDTO>>> findAll() {
        return eventService.findAll();
    }

    @GetMapping("/get-all/{cityId}")
    public ResponseEntity<ApiResponse<List<Event>>> findAllByCityId(
            @PathVariable Long cityId
    ) {
        return eventService.findAllByCityId(cityId);
    }

    @GetMapping("/get-all-similar/{eventSlug}")
    public ResponseEntity<ApiResponse<List<EventDTO>>> findAllSimilarByEventSlug(
            @PathVariable String eventSlug
    ) {
        return eventService.findAllSimilar(eventSlug);
    }

    @GetMapping("/check-in-form/{eventSlug}")
    public ResponseEntity<ApiResponse<CheckInFormDTO>> findCheckInFormByEventSlug(
            @PathVariable String eventSlug
    ) {
        return eventService.findCheckInForm(eventSlug);
    }

    @GetMapping("/get-all/page")
    public ResponseEntity<ApiResponse<Page<EventDTO>>> findAllByPageNation(
            @RequestParam(value = "page") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "6") Integer size
    ) {
        return eventService.findAllByPageNation(page, size);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Event>> update(
            @PathVariable Long id,
            @RequestParam(value = "json", required = false) String event,
            @RequestPart(value = "photo", required = false) MultipartFile photo
    ) {
        return eventService.update(id, event, photo);
    }

    @PutMapping("/change-active/{id}")
    public ResponseEntity<ApiResponse<?>> changeActive(
            @PathVariable Long id
    ) {
        return eventService.changeActive(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id
    ) {
        return eventService.delete(id);
    }

    @PostMapping("/city/create")
    public ResponseEntity<ApiResponse<EventCity>> createCity(
            @RequestBody EventCity eventCity
    ) {
        return eventCityService.create(eventCity);
    }

    @GetMapping("/city/get/{id}")
    public ResponseEntity<ApiResponse<EventCity>> findCityById(
            @PathVariable Long id
    ) {
        return eventCityService.findById(id);
    }

    @GetMapping("/city/get-all")
    public ResponseEntity<ApiResponse<List<EventCity>>> findAllCity() {
        return eventCityService.findAll();
    }

    @PutMapping("/city/update/{id}")
    public ResponseEntity<ApiResponse<EventCity>> updateCity(
            @PathVariable Long id,
            @RequestBody EventCity eventCity
    ) {
        return eventCityService.update(id, eventCity);
    }

    @DeleteMapping("/city/delete/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCity(
            @PathVariable Long id
    ) {
        return eventCityService.delete(id);
    }


}
