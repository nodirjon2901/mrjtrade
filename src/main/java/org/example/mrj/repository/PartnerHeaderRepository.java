package org.example.mrj.repository;

import org.example.mrj.domain.entity.PartnerHeader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerHeaderRepository extends JpaRepository<PartnerHeader, Long> {
}
