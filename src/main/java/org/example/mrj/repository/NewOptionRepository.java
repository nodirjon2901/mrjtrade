package org.example.mrj.repository;

import jakarta.transaction.Transactional;
import org.example.mrj.domain.entity.NewOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NewOptionRepository extends JpaRepository<NewOption, Long> {

    @Query(value = "SELECT MAX(order_num) FROM new_option", nativeQuery = true)
    Optional<Integer> getMaxOrderNum();


    @Modifying
    @Query(value = "UPDATE new_option SET newness_id=null WHERE id = :id", nativeQuery = true)
    void setNewsIdNull(@Param("id") Long id);
}
