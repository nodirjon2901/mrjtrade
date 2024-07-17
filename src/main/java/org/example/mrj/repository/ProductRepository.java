package org.example.mrj.repository;

import org.example.mrj.domain.entity.New;
import org.example.mrj.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query(value = "update product set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id") Long id, boolean active);

    @Modifying
    @Query(value = "update product set slug = :slug where id = :id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long productId);

    @Query(value = "select slug from product where id = :id", nativeQuery = true)
    String findSlugById(@Param("id") Long productId);

    Optional<Product> findBySlug(String slug);

}
