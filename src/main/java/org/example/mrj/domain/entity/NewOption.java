package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    New newness;

    Integer orderNum;

}
