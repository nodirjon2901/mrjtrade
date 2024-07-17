package org.example.mrj.repository;

import org.example.mrj.domain.entity.SchemeWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchemeWorkRepository extends JpaRepository<SchemeWork, Long> {

    @Query(value = "select photo_url from scheme_work where id=:id", nativeQuery = true)
    String findPhotoUrlById(@Param("id")Long id);

    List<SchemeWork> findAllByOrderByIdAsc();

}
