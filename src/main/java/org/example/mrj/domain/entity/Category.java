package org.example.mrj.domain.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostPersist;
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
@Entity(name = "category")
public class Category extends BaseEntity
{
    String header;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    @JsonProperty(value = "item")
    List<CategoryItem> items;

    Boolean addable = true;

    public Category(Long id, String header, List<CategoryItem> itemList, Boolean addable)
    {
        super(id);
        this.header = header;
        this.items = itemList;
        this.addable = addable;
    }

    @PostPersist
    private void setItemsId()
    {
        if (this.items != null)
            this.items.forEach(i -> i.setCategory(this));
    }
}