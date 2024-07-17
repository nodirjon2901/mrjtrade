package org.example.mrj.repository;

import org.example.mrj.domain.entity.Navbar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NavbarRepository extends JpaRepository<Navbar, Long> {

    @Query(value = "select photo_url from navbar where id=:id", nativeQuery = true)
    String findPhotoUrlById(@Param("id")Long id);

}
