package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "product")
public class Product extends BaseEntity {

    String name;

    @JsonProperty("main_description")
    String mainDescription;

    @Column(length = 1000)
    String description;

    @ElementCollection
    @CollectionTable(name = "product_characteristics", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "characteristic_key")
    @Column(name = "characteristic_value",length = 1000)
    Map<String, String> characteristic;

    @Column(unique = true)
    String slug;

    boolean active;

    @JsonProperty("isAll")
    boolean isAll;

    @JsonProperty("isNew")
    boolean isNew;

    boolean promotion;

    double price;

    double sale;

    double salePrice;

    String mainPhotoUrl;

    @ElementCollection
    List<String> photoUrls;

    @ManyToOne
    Catalog catalog;

}
