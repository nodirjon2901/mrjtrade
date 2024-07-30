package org.example.mrj.repository;

import org.example.mrj.domain.entity.Product2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface Product2Repository extends JpaRepository<Product2, Long>
{
    Optional<Product2> findBySlug(String slug);

    @Query(value = "SELECT * FROM product2 WHERE catalog_id = :catalogId", nativeQuery = true)
    List<Product2> findByCatalogId(Long catalogId);

    @Query(value = "SELECT * FROM product2 WHERE catalog_id IN :catalogIds", nativeQuery = true)
    List<Product2> findByCatalogIds(List<Long> catalogIds);

    List<Product2> findByTagContaining(String tag);

    List<Product2> findByCategoryItemId(Long categoryItemId);

    boolean existsByCategoryItemId(Long catalogId);

    @Query(value = "SELECT COUNT(id) FROM product2 WHERE catalog_id IN :catalogIds", nativeQuery = true)
    Integer findCountByCatalogIds(List<Long> catalogIds);
}
