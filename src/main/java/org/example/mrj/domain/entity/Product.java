package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
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
public class Product extends BaseEntity{

    String name;

    @Column(length = 1000)
    String description;

    Map<String, String> characteristic;

    @Column(unique = true)
    String slug;

    boolean isNew;

    boolean promotion;

    Double price;

    String sale;

    String mainPhotoUrl;

    List<String> photoUrls;

    @ManyToOne
    @JsonIgnore
    Catalog catalog;

}
