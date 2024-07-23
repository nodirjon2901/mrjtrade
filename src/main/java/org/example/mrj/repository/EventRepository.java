package org.example.mrj.repository;

import org.example.mrj.domain.entity.Event;
import org.example.mrj.domain.entity.New;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {

    @Query(value = "select photo_url from event where id=:id", nativeQuery = true)
    String findPhotoUrlById(@Param("id")Long id);

    @Modifying
    @Query(value = "update event set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id")Long id, boolean active);

    @Modifying
    @Query(value = "update event set slug = :slug where id = :id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long newId);

    @Query(value = "select slug from event where id = :id", nativeQuery = true)
    String findSlugById(@Param("id") Long eventId);


    @Query(value = "select * from event where slug = :slug", nativeQuery = true)
    Optional<Event> findBySlug(@Param("slug") String slug);

}
