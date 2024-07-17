package org.example.mrj.repository;

import org.example.mrj.domain.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findByIdOrName(Long id, String name);

}
