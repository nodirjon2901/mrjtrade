package org.example.mrj.domain.dto.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.EquipmentCategory;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategorySearchDTO {

    Long id;

    String name;

    String slug;

    String dtoName;

    public CategorySearchDTO(EquipmentCategory category){
        this.id= category.getId();
        this.name= category.getName();
        this.slug= category.getSlug();
        this.dtoName="Category";
    }

}
