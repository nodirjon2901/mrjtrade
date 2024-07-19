package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "photo")
public class Photo extends BaseEntity {

    @JsonIgnore
    String name;

    @JsonIgnore
    String filePath;

    @JsonProperty(value = "url")
    String httpUrl;

    @JsonIgnore
    String type;

    public Photo(String name, String filePath, String httpUrl) {
        this.name = name;
        this.filePath = filePath;
        this.httpUrl = httpUrl;
    }

}
