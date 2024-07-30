package org.example.mrj.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Catalog;
import org.example.mrj.domain.entity.Product;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductDTO {

    String name;

    String mainDescription;

    String slug;

    boolean active;

    String mainPhotoUrl;

    public ProductDTO(Product product) {
        this.name = product.getName();
        this.mainDescription = product.getMainDescription();
        this.slug = product.getSlug();
        this.active = product.isActive();
        this.mainPhotoUrl = product.getMainPhotoUrl();
    }

}
