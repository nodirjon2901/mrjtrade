package org.example.mrj.repository;

import org.example.mrj.domain.entity.BannerSlider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerSliderRepository extends JpaRepository<BannerSlider, Long>
{

}
