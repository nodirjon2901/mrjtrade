package org.example.mrj.repository;

import org.example.mrj.domain.entity.Footer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FooterRepository extends JpaRepository<Footer, Long> {
}
