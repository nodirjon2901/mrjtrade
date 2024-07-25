package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "news")
public class New extends BaseEntity{

    String title;

    @Column(length = 5000)
    String body;

    @OneToOne
    Photo photo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "newness")
    List<NewOption> newOptions;

    @Column(unique = true)
    String slug;

    boolean active;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date createDate;

    Integer orderNum;

}
