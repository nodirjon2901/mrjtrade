package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "event")
public class Event extends BaseEntity
{

    String heading;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String slug;

    @OneToOne
    @JsonProperty(value = "photo", access = JsonProperty.Access.READ_ONLY)
    Photo coverPhoto; // Main photo

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event", orphanRemoval = true)
    List<EventAbout> abouts;

    String dateFrom;
    String dateTo;

    String timeFrom;
    String timeTo;


    String organizer;

    String city;

    String address;

    @PostPersist
    private void setIdToAbout()
    {
        if (abouts != null)
            this.abouts.forEach(i -> i.setEvent(this));
    }
}
