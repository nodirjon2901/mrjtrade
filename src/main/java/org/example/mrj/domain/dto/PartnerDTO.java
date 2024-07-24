package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Partner;
import org.example.mrj.domain.entity.Photo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PartnerDTO {

    Long id;

    Photo photo;

    String slug;

    Integer orderNum;

    public PartnerDTO(Partner partner) {
        this.id= partner.getId();
        this.photo = partner.getPhoto();
        this.slug = partner.getSlug();
        this.orderNum=partner.getOrderNum();
    }

}
