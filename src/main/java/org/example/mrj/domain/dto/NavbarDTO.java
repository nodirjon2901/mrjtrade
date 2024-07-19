package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.Navbar;
import org.example.mrj.domain.entity.NavbarOption;

import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NavbarDTO
{
    Long id;

    PhotoDTO logo;

    List<NavbarOption> option;

    public NavbarDTO(Navbar entity)
    {
        this.id = entity.getId();
        this.logo = new PhotoDTO(entity.getLogo());
        this.option = entity.getOption();
    }
}
