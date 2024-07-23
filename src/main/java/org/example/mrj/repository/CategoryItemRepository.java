package org.example.mrj.repository;

import org.example.mrj.domain.entity.CategoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryItemRepository extends JpaRepository<CategoryItem, Long>
{
    @Query("SELECT order FROM category_item  WHERE order IS NOT NULL")
    List<Integer> findAllExistingOrders();


    @Modifying
    @Query(value = "ALTER TABLE category_item SET title = :title WHERE id = :id", nativeQuery = true)
    void updateTitle(@Param("id") Long id, @Param("title") String newTitle);

    @Modifying
    @Query(value = "ALTER TABLE category_item SET slug = :slug WHERE id = :id", nativeQuery = true)
    void updateSlug(Long id, String slug);

    @Modifying
    @Query(value = "ALTER TABLE category_item SET active = :active WHERE id = :id", nativeQuery = true)
    void updateActive(Long id, Boolean active);

    @Modifying
    @Query(value = "ALTER TABLE category_item SET main = :main WHERE id = :id", nativeQuery = true)
    void updateMain(Long id, Boolean main);
}
