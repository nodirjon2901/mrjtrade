package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "event")
public class Event extends BaseEntity {

    String title;

    @Column(length = 3000)
    String description;

    @Column(unique = true)
    String slug;

    boolean active;

    String photoUrl;

//    @ElementCollection
//    @CollectionTable(name = "event_information", joinColumns = @JoinColumn(name = "event_id"))
//    @MapKeyColumn(name = "characteristic_key")
//    @Column(name = "characteristic_value")
//    @JsonProperty("event_information")
//    Map<String, String> eventInformation;

    @OneToOne(cascade = CascadeType.ALL)
    EventInformation eventInformation;

    @ManyToOne
    EventCity city;

}
