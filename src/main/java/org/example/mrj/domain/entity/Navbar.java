package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity(name = "navbar")
public class Navbar extends BaseEntity
{

    @OneToOne(cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)//On creating Navbar , front not send "logo":{...} as json data , instead of that send MultipartFile , but we send Navbar to front need "logo":{ "id":id,"url":"http://..."} data . LOGO NOT CREATE WITH JSON DATA!
    Photo logo;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    List<NavbarOption> options;

    String phone;
}
