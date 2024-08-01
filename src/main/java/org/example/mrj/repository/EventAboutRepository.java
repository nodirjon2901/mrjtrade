package org.example.mrj.repository;

import org.example.mrj.domain.entity.EventAbout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface EventAboutRepository extends JpaRepository<EventAbout, Long>
{
    @Transactional
    @Modifying
    @Query(value = "DELETE FROM event_about WHERE id= :id", nativeQuery = true)
    void delete(Long id);
}
