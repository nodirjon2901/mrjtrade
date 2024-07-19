package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "partner")
public class Partner extends BaseEntity {

    String title;

    @JsonProperty("main_description")
    String mainDescription;

    @Column(length = 1000)
    String description;

    String photoUrl;

    @Column(unique = true)
    String slug;

    boolean active;

}
