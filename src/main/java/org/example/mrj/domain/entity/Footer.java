package org.example.mrj.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "footer")
public class Footer extends BaseEntity {

    String logoIconUrl;

    String aboutLogo;

    String telegramUrl;

    String facebookUrl;

    String instagramUrl;

    String youTubeUrl;

    String creatorUrl; // examle: https://result-me.uz

    String telegramIconUrl;

    String facebookIconUrl;

    String instagramIconUrl;// example : http://localhost:8080/photo/instagram.png shu urlga reqaust yuborilsa rasm qaytishi kerak

    String youTubeIconUrl;

    String creatorIconUrl;

    @OneToMany(cascade = CascadeType.ALL)
    List<FooterOption> footerOptions;

    String copyright;


}
