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

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "equipment_category")
public class EquipmentCategory extends BaseEntity {

    @Column(unique = true)
    String name;

    @OneToOne
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    Photo photo;

    @Column(unique = true)
    String slug;

    boolean active;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
//    @JsonIgnore
    List<Catalog> catalogList;

}
