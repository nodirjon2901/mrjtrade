package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Event;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDTO {

    String title;

    String slug;

    boolean active;

    String photoUrl;

    public EventDTO(Event event) {
        this.title = event.getTitle();
        this.slug = event.getSlug();
        this.active = event.isActive();
        this.photoUrl = event.getPhotoUrl();
    }

}
