package org.example.mrj.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Entity(name = "scheme_work")
public class SchemeWork extends BaseEntity{

    String header;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "schemeWork")
    List<SchemeWorkItems> schemeWorkItems;

}
