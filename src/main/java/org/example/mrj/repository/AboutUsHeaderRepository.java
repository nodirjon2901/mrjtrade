package org.example.mrj.repository;

import org.example.mrj.domain.entity.AboutUsHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AboutUsHeaderRepository extends JpaRepository<AboutUsHeader, Long> {
}
