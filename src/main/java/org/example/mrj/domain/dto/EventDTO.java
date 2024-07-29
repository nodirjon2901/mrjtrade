package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Event;
import org.example.mrj.domain.entity.Photo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDTO
{
    Long id;

    String slug;

    String heading;

    Photo coverPhoto; // Main photo

    public EventDTO(Event entity)
    {
        this.id = entity.getId();
        this.slug = entity.getSlug();
        this.heading = entity.getHeading();
        this.coverPhoto = entity.getCoverPhoto();
    }
}
