package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.dto.CheckInFormDTO;
import org.example.mrj.domain.dto.EventDTO;
import org.example.mrj.domain.entity.Event;
import org.example.mrj.domain.entity.EventCity;
import org.example.mrj.exception.NotFoundException;
import org.example.mrj.repository.EventCityRepository;
import org.example.mrj.repository.EventRepository;
import org.example.mrj.util.SlugUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final EventCityRepository cityRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Event>> create(Long cityId, String json, MultipartFile newPhoto) {
        ApiResponse<Event> response = new ApiResponse<>();
        Optional<EventCity> cityOptional = cityRepository.findById(cityId);
        if (cityOptional.isEmpty()) {
            response.setMessage("City is not found by city id: " + cityId);
            return ResponseEntity.status(404).body(response);
        }
        try {
            Event event = objectMapper.readValue(json, Event.class);
            event.setActive(true);
            event.setCity(cityOptional.get());
            event.setPhoto(photoService.save(newPhoto));
            Event save = eventRepository.save(event);
            String slug = save.getId() + "-" + SlugUtil.makeSlug(event.getTitle());
            eventRepository.updateSlug(slug, save.getId());
            save.setSlug(slug);
            response.setData(save);
            return ResponseEntity.status(200).body(response);
        } catch (JsonProcessingException e) {
            response.setMessage(e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    public ResponseEntity<ApiResponse<Event>> findById(Long id) {
        ApiResponse<Event> response = new ApiResponse<>();
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            response.setMessage("Event is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Event event = optionalEvent.get();
        response.setData(event);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Event>> findBySlug(String slug) {
        ApiResponse<Event> response = new ApiResponse<>();
        Optional<Event> optionalEvent = eventRepository.findBySlug(slug);
        if (optionalEvent.isEmpty()) {
            response.setMessage("Event is not found by slug: " + slug);
            return ResponseEntity.status(404).body(response);
        }
        Event event = optionalEvent.get();
        response.setMessage("Found");
        response.setData(event);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<CheckInFormDTO>> findCheckInForm(String eventSlug) {
        ApiResponse<CheckInFormDTO> response = new ApiResponse<>();
        Optional<Event> optionalEvent = eventRepository.findBySlug(eventSlug);
        if (optionalEvent.isEmpty()) {
            response.setMessage("Event is not found by slug: " + eventSlug);
            return ResponseEntity.status(404).body(response);
        }
        Event event = optionalEvent.get();
        response.setData(new CheckInFormDTO(event.getEventInformation()));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<EventDTO>>> findAll() {
        ApiResponse<List<EventDTO>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<Event> all = eventRepository.findAll();
        all.forEach(event -> response.getData().add(new EventDTO(event)));
        response.setMessage("Found " + all.size() + " event(s)");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<EventDTO>>> findAllSimilar(String eventSlug) {
        ApiResponse<List<EventDTO>> response = new ApiResponse<>();
        Optional<Event> optionalEvent = eventRepository.findBySlug(eventSlug);
        if (optionalEvent.isEmpty()) {
            response.setMessage("Event is not found by slug: " + eventSlug);
            return ResponseEntity.status(404).body(response);
        }
        response.setData(new ArrayList<>());
        EventCity city = optionalEvent.get().getCity();
        List<Event> all = eventRepository.findAll();
        all.stream()
                .filter(event -> event.getCity().getId().equals(city.getId()) && !event.getSlug().equals(eventSlug))
                .toList()
                .forEach(event -> response.getData().add(new EventDTO(event)));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Page<EventDTO>>> findAllByPageNation(int page, int size) {
        ApiResponse<Page<EventDTO>> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Event> all = eventRepository.findAll(pageable);
        Page<EventDTO> map = all.map(EventDTO::new);
        response.setData(map);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<Event>>> findAllByCityId(Long cityId) {
        ApiResponse<List<Event>> response = new ApiResponse<>();
        if (!cityRepository.existsById(cityId)) {
            response.setMessage("City is not found by id: " + cityId);
            return ResponseEntity.status(404).body(response);
        }
        response.setData(new ArrayList<>());
        List<Event> all = eventRepository.findAll();
        all.stream()
                .filter(event -> event.getCity().getId().equals(cityId))
                .toList()
                .forEach(event -> response.getData().add(event));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<Event>> update(Event event) {
        ApiResponse<Event> response = new ApiResponse<>();
        Event existingEvent = eventRepository.findById(event.getId()).orElseThrow(() -> new NotFoundException("Event is not found by id: " + event.getId()));
        if (event.getTitle() != null) {
            existingEvent.setTitle(event.getTitle());
            String slug = existingEvent.getId() + "-" + SlugUtil.makeSlug(existingEvent.getTitle());
            existingEvent.setTitle(slug);
        }
        if (event.getDescription() != null) {
            existingEvent.setDescription(event.getDescription());
        }
        response.setData(eventRepository.save(existingEvent));
        return ResponseEntity.ok().body(response);
    }

    public ResponseEntity<ApiResponse<?>> changeActive(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        Optional<Event> optionalEvent = eventRepository.findById(id);
        if (optionalEvent.isEmpty()) {
            response.setMessage("Event is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        Event event = optionalEvent.get();
        boolean active = !event.isActive();
        eventRepository.changeActive(id, active);
        response.setMessage("Successfully changed! Current event active: " + active);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (eventRepository.findById(id).isEmpty()) {
            response.setMessage("Event is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        eventRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }

}
