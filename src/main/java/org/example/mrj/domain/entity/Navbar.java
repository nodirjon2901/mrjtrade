package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
public class Navbar
{
    @Id
    @GeneratedValue
    Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)//On creating Navbar , front not send "logo":{...} as json data , instead of that send MultipartFile , but we send Navbar to front need "logo":{ "id":id,"url":"http://..."} data . LOGO NOT CREATE WITH JSON DATA!
    Photo logo;

    @OneToMany(cascade = CascadeType.ALL)
    List<NavbarOption> options;

    String phone;
}
