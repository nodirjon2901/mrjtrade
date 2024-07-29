package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "news")
public class New extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "news", orphanRemoval = true)
    NewHeadOption head;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "newness")
    List<NewOption> newOptions;

    @Column(unique = true)
    String slug;

    Integer orderNum;

    @CreationTimestamp
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    Date createDate;

    Boolean active;

    @PostPersist
    private void setOptionsId() {
        if (this.newOptions != null)
            this.newOptions.forEach(i -> i.setNewness(this));
        if (this.head != null)
            this.head.setNews(this);
    }


}
