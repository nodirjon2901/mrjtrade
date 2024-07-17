package org.example.mrj.domain.entity;

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
@Entity(name = "main_about_us")
public class MainAboutUs extends BaseEntity{

    String title;

    String description;

    String photoUrl;

    boolean active;

}
