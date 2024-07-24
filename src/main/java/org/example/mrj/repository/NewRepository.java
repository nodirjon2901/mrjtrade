package org.example.mrj.repository;

import org.example.mrj.domain.entity.New;
import org.example.mrj.domain.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewRepository extends JpaRepository<New, Long>, JpaSpecificationExecutor<New> {

    @Modifying
    @Query(value = "update news set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id") Long id, boolean active);

    @Modifying
    @Query(value = "update news set slug = :slug where id = :id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long newId);

    @Query(value = "select slug from news where id = :id", nativeQuery = true)
    String findSlugById(@Param("id") Long newId);


    @Query(value = "select * from news where slug = :slug", nativeQuery = true)
    Optional<New> findBySlug(@Param("slug") String slug);

    @Query(value = "select * from news order by id asc", nativeQuery = true)
    List<New> findAllByOrderByIdAsc();

    @Query(value = "SELECT MAX(order_num) FROM news", nativeQuery = true)
    Optional<Integer> getMaxOrderNum();

}
