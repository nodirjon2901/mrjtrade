package org.example.mrj.repository;

import org.example.mrj.domain.entity.EventCity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCityRepository extends JpaRepository<EventCity, Long> {

    boolean existsByName(String name);

}
