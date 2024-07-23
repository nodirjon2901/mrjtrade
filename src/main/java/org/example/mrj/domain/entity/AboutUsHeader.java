package org.example.mrj.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "about_us_header")
public class AboutUsHeader extends BaseEntity{

    String formName;

    String title;

    String subtitle;

    String description;

    @OneToMany
    List<Photo> photos;

}
