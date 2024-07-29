package org.example.mrj.controller;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.EventDTO;
import org.example.mrj.domain.entity.Event;
import org.example.mrj.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController
{
    private final EventService eventService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Event>> add(
            @RequestParam("json") String json,
            @RequestParam("photo") MultipartFile photo)
    {
        return eventService.add(json, photo);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<Event>> findBySlug(@PathVariable String slug)
    {
        return eventService.get(slug);
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<EventDTO>>> getAll(
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "6") int size,
            @RequestParam(value = "sort", defaultValue = "id,asc") String[] sort)
    {
        return eventService.getAll(city, page, size, sort);
    }

    @GetMapping("/city-list")
    public ResponseEntity<ApiResponse<List<String>>> getCityList()
    {
        return eventService.getCityList();
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Event>> update(
            @RequestBody Event event)
    {
        return eventService.update(event);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id)
    {
        return eventService.delete(id);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<?>> deleteAbouts(
            @RequestParam(value = "about-id") Long aboutId)
    {
        return eventService.deleteAbout(aboutId);
    }
}
