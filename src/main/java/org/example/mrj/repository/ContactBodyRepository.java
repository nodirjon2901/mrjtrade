package org.example.mrj.repository;

import org.example.mrj.domain.entity.ContactBody;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactBodyRepository extends JpaRepository<ContactBody, Long> {


}
