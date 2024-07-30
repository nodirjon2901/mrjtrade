package org.example.mrj.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Product2;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Product2DTO
{
    Long id;

    String slug;

    List<String> tag;

    PhotoDTO photo;

    String name;

    String shortDescription;

    Integer discount;

    Double originalPrice;

    public Product2DTO(Product2 entity)
    {
        this.id = entity.getId();
        this.slug = entity.getSlug();
        this.tag = entity.getTag();
        this.name = entity.getName();
        this.shortDescription = entity.getShortDescription();
        this.discount = entity.getDiscount();
        this.originalPrice = entity.getOriginalPrice();
        if (!entity.getGallery().isEmpty()) this.photo = new PhotoDTO(entity.getGallery().get(0));
    }
}
