package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.DecimalMin;
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
    @Column(columnDefinition = "INT CHECK (discount >= 0)")
    Integer discount;

    @DecimalMin("0.0")
    @Column(columnDefinition = "DOUBLE PRECISION CHECK (original_price >= 0.0)")
    Double originalPrice;

    String conditions;

    @ManyToOne
    @JsonProperty(value = "brand")
    Partner partner;

    @ManyToOne
    Catalog catalog;

    @OneToMany(cascade = CascadeType.ALL)
    List<Characteristic> characteristics;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Photo mainPhoto;

    @OneToMany
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    List<Photo> gallery;
}
