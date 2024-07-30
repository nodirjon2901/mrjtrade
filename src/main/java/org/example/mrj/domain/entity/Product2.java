package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "product2")
public class Product2 extends BaseEntity
{
    String name;

    @Column(unique = true)
    String slug;

    @ElementCollection
    List<String> tag;

    @Column(length = 500)
    String shortDescription;

    @Column(length = 1000)
    String description;

    @Min(value = 0, message = "Cannot have a value less than 0.")
    @Max(value = 100, message = "Cannot have a value more than 100.")
    @Column(columnDefinition = "INT CHECK (discount >= 0 AND discount <= 100)")
    Integer discount;

    @DecimalMin("0.0")
    @Column(columnDefinition = "DOUBLE PRECISION CHECK (original_price >= 0.0)")
    Double originalPrice;

    String conditions;

    @ManyToOne
    @JsonProperty(value = "brand")
    Partner partner;

    @ManyToOne(fetch = FetchType.LAZY)
    Catalog catalog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonProperty(value = "categoryItem")
    CategoryItem categoryItem;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", orphanRemoval = true)
    List<Characteristic> characteristics;

//    @OneToOne
//    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//    Photo mainPhoto;

    @OneToMany
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    List<Photo> gallery;

    @PostPersist
    private void setIdToCharacteristics()
    {
        if (this.characteristics != null)
            this.characteristics.forEach(i -> i.setProduct(this));
    }
}
