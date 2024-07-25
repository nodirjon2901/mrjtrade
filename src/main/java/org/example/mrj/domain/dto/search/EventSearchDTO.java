package org.example.mrj.domain.dto.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Event;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class EventSearchDTO {

    Long id;

    String title;

    String slug;

    String dtoName;

    public EventSearchDTO(Event event) {
        this.id = event.getId();
        this.title = event.getHeading();
        this.slug = event.getSlug();
        this.dtoName = "Event";
    }

}
