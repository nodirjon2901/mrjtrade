package org.example.mrj.domain.dto.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Product;


@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSearchDTO {

    Long id;

    String name;

    String mainDescription;

    String slug;

    String mainPhotoUrl;

    String dtoName;

    public ProductSearchDTO(Product product){
        this.id= product.getId();
        this.name= product.getName();
        this.mainDescription= product.getMainDescription();
        this.slug=product.getSlug();
        this.mainPhotoUrl= product.getMainPhotoUrl();
        this.dtoName="Product";
    }
}
