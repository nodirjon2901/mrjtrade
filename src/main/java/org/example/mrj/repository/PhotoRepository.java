package org.example.mrj.repository;

import org.example.mrj.domain.entity.Photo;
import org.example.mrj.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long>, JpaSpecificationExecutor<Product> {

    Photo findByIdOrName(Long id, String name);

}
