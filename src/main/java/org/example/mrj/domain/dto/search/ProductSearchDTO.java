package org.example.mrj.domain.dto.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Photo;
import org.example.mrj.domain.entity.Product;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSearchDTO {

    Long id;

    String name;

    String shortDescription;

    String slug;

    Photo photo;

    String dtoName;

    public ProductSearchDTO(Product product){
        this.id= product.getId();
        this.name= product.getName();
        this.shortDescription= product.getShortDescription();
        this.slug=product.getSlug();
        this.photo= product.getGallery().get(0);
        this.dtoName="Product";
    }
}
