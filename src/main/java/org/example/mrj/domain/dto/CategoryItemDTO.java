package org.example.mrj.domain.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.mrj.domain.entity.CategoryItem;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryItemDTO
{
    String title;

    List<CatalogDTO> catalog;

    public CategoryItemDTO(CategoryItem entity)
    {
        this.title = entity.getTitle();
        this.catalog = new ArrayList<>();
        entity.getCatalogList().forEach(i -> this.catalog.add(new CatalogDTO(i.getId(), i.getName())));
    }
}
