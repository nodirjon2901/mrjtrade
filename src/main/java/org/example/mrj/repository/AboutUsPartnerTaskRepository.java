package org.example.mrj.repository;

import org.example.mrj.domain.entity.AboutUsPartnerTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutUsPartnerTaskRepository extends JpaRepository<AboutUsPartnerTask, Long> {

    @Query(value = "select icon_url from about_us_partner_task where id=:id", nativeQuery = true)
    String findPhotoUrlById(@Param("id")Long id);

    @Modifying
    @Query(value = "update about_us_partner_task set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id")Long id, boolean active);

}
