package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Partner;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerDTO {

    String photoUrl;

    String slug;

    public PartnerDTO(Partner partner) {
        this.photoUrl = partner.getPhotoUrl();
        this.slug = partner.getSlug();
    }

}
