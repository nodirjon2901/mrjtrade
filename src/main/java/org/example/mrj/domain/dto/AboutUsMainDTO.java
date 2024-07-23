package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.AboutUsHeader;
import org.example.mrj.domain.entity.Photo;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class AboutUsMainDTO {

    Long id;

    String formName;

    String description;

    List<Photo> photos;

    public AboutUsMainDTO(AboutUsHeader aboutUsHeader){
        this.id= aboutUsHeader.getId();
        this.formName=aboutUsHeader.getFormName();
        this.description=aboutUsHeader.getTitle()+" "+aboutUsHeader.getSubtitle()+". "+aboutUsHeader.getDescription();
        this.photos=aboutUsHeader.getPhotos();
    }

}
