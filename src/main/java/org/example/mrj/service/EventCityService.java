package org.example.mrj.service;

import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.EventCity;
import org.example.mrj.repository.EventCityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class EventCityService {

    private final EventCityRepository eventCityRepository;

    public ResponseEntity<ApiResponse<EventCity>> create(EventCity eventCity) {
        ApiResponse<EventCity> response = new ApiResponse<>();
        if (eventCityRepository.existsByName(eventCity.getName())) {
            response.setMessage("City is already exists with this name: " + eventCity.getName());
            return ResponseEntity.status(405).body(response);
        }
        EventCity save = eventCityRepository.save(eventCity);
        response.setData(save);
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<EventCity>> findById(Long id) {
        ApiResponse<EventCity> response = new ApiResponse<>();
        Optional<EventCity> optionalEventCity = eventCityRepository.findById(id);
        if (optionalEventCity.isEmpty()) {
            response.setMessage("City is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        EventCity eventCity = optionalEventCity.get();
        response.setData(eventCity);
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<List<EventCity>>> findAll() {
        ApiResponse<List<EventCity>> response = new ApiResponse<>();
        response.setData(new ArrayList<>());
        List<EventCity> all = eventCityRepository.findAll();
        all.forEach(eventCity -> response.getData().add(eventCity));
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<EventCity>> update(Long id, EventCity eventCity) {
        ApiResponse<EventCity> response = new ApiResponse<>();
        Optional<EventCity> optionalEventCity = eventCityRepository.findById(id);
        if (optionalEventCity.isEmpty()) {
            response.setMessage("City is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        EventCity oldCity = optionalEventCity.get();
        oldCity.setName(eventCity.getName());
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id) {
        ApiResponse<?> response = new ApiResponse<>();
        if (eventCityRepository.findById(id).isEmpty()) {
            response.setMessage("City is not found by id: " + id);
            return ResponseEntity.status(404).body(response);
        }
        eventCityRepository.deleteById(id);
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }

}
