package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "new_option")
public class NewOption extends BaseEntity {

    String heading;

    @Column(length = 5000)
    String text;

    @OneToOne
    Photo photo;

    @ManyToOne
    @JsonIgnore
    New newness;

    Integer orderNum;

}
