package org.example.mrj.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class BannerSlider extends BaseEntity
{
    @OneToOne(orphanRemoval = true, cascade = CascadeType.ALL)
    Photo photo;

    String link;

    Boolean active;

    public BannerSlider(String link, Boolean active, Photo photo)
    {
        this.link = link;
        this.active = active;
        this.photo = photo;
    }
}
