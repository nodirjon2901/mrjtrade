package org.example.mrj.repository;

import org.example.mrj.domain.entity.SchemeWorkItems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchemeWorkItemsRepository extends JpaRepository<SchemeWorkItems, Long> {
}
