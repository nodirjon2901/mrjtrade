package org.example.mrj.repository;

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
public interface PartnerRepository extends JpaRepository<Partner, Long>, JpaSpecificationExecutor<Partner> {

    @Modifying
    @Query(value = "update partner set active=:active where id=:id", nativeQuery = true)
    void changeActive(@Param("id") Long id, boolean active);

    @Modifying
    @Query(value = "update partner set slug = :slug where id = :id", nativeQuery = true)
    void updateSlug(@Param("slug") String slug, @Param("id") Long partnerId);

    @Query(value = "select * from partner order by id asc", nativeQuery = true)
    List<Partner> findAllByOrderByIdAsc();


    @Query(value = "select * from partner where slug = :slug", nativeQuery = true)
    Optional<Partner> findBySlug(@Param("slug") String slug);

    Optional<Partner> findByOrderNum(Integer orderNum);

    @Query(value = "SELECT MAX(order_num) FROM partner", nativeQuery = true)
    Optional<Integer> getMaxOrderNum();

    @Query(value = "select * from partner order by order_num", nativeQuery = true)
    List<Partner> findAllByOrderByOrderNum();


}
