package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.New;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewDTO {

    String title;

    String date;

    String mainPhotoUrl;

    String slug;

    boolean active;

    public NewDTO(New newness) {
        this.title = newness.getTitle();
        this.date = newness.getDate();
        this.mainPhotoUrl = newness.getMainPhotoUrl();
        this.slug = newness.getSlug();
        this.active = newness.isActive();
    }

}
