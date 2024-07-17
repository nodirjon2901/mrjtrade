package org.example.mrj.repository;

import org.example.mrj.domain.entity.ApplicationFormText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationTextRepository extends JpaRepository<ApplicationFormText, Long> {



}
