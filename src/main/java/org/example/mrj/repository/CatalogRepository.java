package org.example.mrj.repository;

import org.example.mrj.domain.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long>, JpaSpecificationExecutor<Catalog> {


    @Modifying
    @Query(value = "UPDATE catalog SET name = :name WHERE id = :id", nativeQuery = true)
    void updateName(Long id, String name);
}
