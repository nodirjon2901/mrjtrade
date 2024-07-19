package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Photo;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhotoDTO
{
    Long id;

    String url;

    public PhotoDTO(Photo entity)
    {
        this.id = entity.getId();
        this.url = entity.getHttpUrl();
    }
}