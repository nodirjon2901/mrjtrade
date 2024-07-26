package org.example.mrj.domain.dto.search;

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
public class NewSearchDTO {

    Long id;

    String title;

    String slug;

    String dtoName;

    public NewSearchDTO(New newness){
        this.id= newness.getId();
//        this.title=newness.getTitle();
        this.slug= newness.getSlug();
        this.dtoName="New";
    }

}
