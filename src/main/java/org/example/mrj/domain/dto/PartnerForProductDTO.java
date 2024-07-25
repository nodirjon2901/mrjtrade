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
public class PartnerForProductDTO {

    Long id;

    String title;

    Photo photo;

    public PartnerForProductDTO(Partner partner){
        this.id= partner.getId();
        this.title=partner.getTitle();
        this.photo=partner.getPhoto();
    }

}
