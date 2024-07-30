package org.example.mrj.repository;

import org.example.mrj.domain.entity.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacteristicRepository extends JpaRepository<Characteristic, Long>
{
}
