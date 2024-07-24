package org.example.mrj.domain.dto.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.CategoryItem;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategorySearchDTO {

    Long id;

    String title;

    String slug;

    String dtoName;

    public CategorySearchDTO(CategoryItem category){
        this.id= category.getId();
        this.title= category.getTitle();
        this.slug= category.getSlug();
        this.dtoName="Category";
    }

}
