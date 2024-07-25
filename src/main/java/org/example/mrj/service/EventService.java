package org.example.mrj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Event;
import org.example.mrj.domain.entity.EventAbout;
import org.example.mrj.exception.NotFoundException;
import org.example.mrj.repository.EventRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class EventService
{

    private final EventRepository eventRepository;

    private final ObjectMapper objectMapper;

    private final PhotoService photoService;

    public ResponseEntity<ApiResponse<Event>> add(String json, MultipartFile photo)
    {
        ApiResponse<Event> response = new ApiResponse<>();
        try
        {
            Event event = objectMapper.readValue(json, Event.class);
            Long id = eventRepository.save(new Event()).getId();
            event.setCoverPhoto(photoService.save(photo));
            event.setId(id);
            event.setSlug(id + "-" + event.getHeading());
            response.setData(eventRepository.save(event));
            response.setMessage("Added");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (JsonProcessingException e)
        {
            throw new RuntimeException("Failed to parse json: " + e.getMessage());
        }
    }

    public ResponseEntity<ApiResponse<Event>> get(String slug)
    {
        ApiResponse<Event> response = new ApiResponse<>();
        Optional<Event> bySlug = eventRepository.findBySlug(slug);
        if (bySlug.isEmpty())
            throw new NotFoundException("Event with slug \'" + slug + "\' not found");
        response.setData(bySlug.get());
        response.setMessage("Found");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<List<Event>>> getAll(String city, int page, int size, String[] sort)
    {
        ApiResponse<List<Event>> response = new ApiResponse<>();
        List<Event> all = new ArrayList<>();

        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Sort sortOrder = Sort.by(direction, sort[0]);

        page = Math.max(page - 1, 0); // Pagination Default to 1 instead of 0
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        if (city == null)
        {
            all = eventRepository.findAll(pageable).getContent();
        } else
        {
            all = eventRepository.findByCityEqualsIgnoreCase(city, pageable);
        }
        response.setData(all);
        response.setMessage("Found " + all.size() + " event(s)");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<List<String>>> getCityList()
    {
        ApiResponse<List<String>> response = new ApiResponse<>();
        response.setData(eventRepository.getCity());
        response.setMessage("Found " + response.getData().size() + " event(s)");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<Event>> update(Event newEvent)
    {
        ApiResponse<Event> response = new ApiResponse<>();
        Optional<Event> byId = eventRepository.findById(newEvent.getId());
        if (byId.isEmpty())
            throw new NotFoundException("Event not found with id " + newEvent.getId());

        Event fromDB = byId.get();

        if (newEvent.getHeading() != null)
        {
            fromDB.setHeading(newEvent.getHeading());
            fromDB.setSlug(fromDB.getId() + "-" + fromDB.getHeading());
        }

        if (newEvent.getDateFrom() != null) fromDB.setDateFrom(newEvent.getDateFrom());
        if (newEvent.getDateTo() != null) fromDB.setDateTo(newEvent.getDateTo());
        if (newEvent.getTimeFrom() != null) fromDB.setTimeFrom(newEvent.getTimeFrom());
        if (newEvent.getTimeTo() != null) fromDB.setTimeTo(newEvent.getTimeTo());
        if (newEvent.getOrganizer() != null) fromDB.setOrganizer(newEvent.getOrganizer());
        if (newEvent.getCity() != null) fromDB.setCity(newEvent.getCity());
        if (newEvent.getAddress() != null) fromDB.setAddress(newEvent.getAddress());
        if (newEvent.getAbouts() != null)
        {
            List<EventAbout> newEventAbouts = newEvent.getAbouts();
            List<EventAbout> dbAbouts = fromDB.getAbouts();
            for (EventAbout newEventAbout : newEventAbouts)
            {
                Long id = newEventAbout.getId();
                for (EventAbout dbAbout : dbAbouts)
                {
                    if (dbAbout.getId().equals(id))
                    {
                        dbAbout.setHeading(newEventAbout.getHeading());
                        dbAbout.setText(newEventAbout.getText());
                    }
                }
                if (id == null)
                {
                    dbAbouts.add(newEventAbout);
                }
            }
        }

        response.setData(eventRepository.save(fromDB));
        response.setMessage("Updated");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<?>> delete(Long id)
    {
        ApiResponse<?> response = new ApiResponse<>();
        if (!eventRepository.existsById(id))
            throw new NotFoundException("Event not found with id " + id);

        eventRepository.deleteById(id);
        response.setMessage("Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    public ResponseEntity<ApiResponse<?>> deleteAbout(Long aboutId)
    {
        ApiResponse<?> response = new ApiResponse<>();
        List<Event> fromDB = eventRepository.findAll();
        for (int i = 0; i < fromDB.size(); i++)
        {
            List<EventAbout> abouts = fromDB.get(i).getAbouts();
            for (int j = 0, aboutsSize = abouts.size(); j < aboutsSize; j++)
            {
                if (abouts.get(j).getId().equals(aboutId))
                {
                    abouts.remove(j);
                    eventRepository.save(fromDB.get(i));
                    break;
                }
            }
        }
        response.setMessage("Deleted");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
