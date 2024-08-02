package org.example.mrj.repository;

import org.example.mrj.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product>
{
    Optional<Product> findBySlug(String slug);

    @Query(value = "SELECT * FROM product WHERE catalog_id = :catalogId", nativeQuery = true)
    List<Product> findByCatalogId(Long catalogId);

    @Query(value = "SELECT * FROM product WHERE catalog_id IN :catalogIds", nativeQuery = true)
    List<Product> findByCatalogIds(List<Long> catalogIds);

    List<Product> findByTagContaining(String tag);

    List<Product> findByCategoryItemId(Long categoryItemId);

    boolean existsByCategoryItemId(Long catalogId);

    @Query(value = "SELECT COUNT(id) FROM product WHERE catalog_id IN :catalogIds", nativeQuery = true)
    Integer findCountByCatalogIds(List<Long> catalogIds);

    List<Product> findByProfessional(boolean professional);

    List<Product> findByTagContainingAndProfessional(String tag, boolean professional);
}
