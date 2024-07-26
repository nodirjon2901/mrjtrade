package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.New;
import org.example.mrj.domain.entity.Photo;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewDTO {

    Long id;

    String title;

    String date;

    Photo photo;

    String slug;

    boolean active;

    Integer orderNum;

    public NewDTO(New newness) {
        this.id= newness.getId();
//        this.title = newness.getTitle();
        this.date = String.valueOf(newness.getCreateDate());
//        this.photo = newness.getPhoto();
        this.slug = newness.getSlug();
//        this.active = newness.isActive();
        this.orderNum=newness.getOrderNum();
    }

}
