package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "partner")
public class Partner extends BaseEntity {

    String title;

    @JsonProperty("main_description")
    @Column(name = "main_description")
    String mainDescription;

    @Column(length = 5000)
    String description;

    @OneToOne
    Photo photo;

    @Column(unique = true)
    String slug;

    boolean active;

    @Column(name = "order_num")
    Integer orderNum;

}
