package org.example.mrj.repository;

import org.example.mrj.domain.entity.MainAboutUs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MainAboutUsRepository extends JpaRepository<MainAboutUs, Long> {

    @Query(value = "select photo_url from main_about_us where id=:id", nativeQuery = true)
    String findPhotoUrlById(@Param("id")Long id);

    @Modifying
    @Query(value = "update main_about_us set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id")Long id, boolean active);

}
