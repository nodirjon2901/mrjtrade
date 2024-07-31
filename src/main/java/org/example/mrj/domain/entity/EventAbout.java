package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "event_about")
public class EventAbout extends BaseEntity
{
    String heading;

    @Column(length = 1500)
    String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    Event event;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof EventAbout that)) return false;
        return super.id.equals(that.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), heading, text, event);
    }
}
