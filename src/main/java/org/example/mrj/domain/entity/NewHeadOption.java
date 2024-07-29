package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class NewHeadOption extends BaseEntity {

    String title;

    @Column(length = 5000)
    String body;

    @OneToOne
    Photo photo;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    New news;

    @Override
    public String toString() {
        return "NewHeadOption{" +
                "title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", photo=" + photo +
                ", id=" + id +
                '}';
    }
}
