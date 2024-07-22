package org.example.mrj.domain.dto.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Partner;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class PartnerSearchDTO {

    Long id;

    String title;

    String slug;

    String dtoName;

    public PartnerSearchDTO(Partner partner){
        this.id= partner.getId();
        this.title= partner.getTitle();
        this.slug= partner.getSlug();
        this.dtoName="Partner";
    }

}
