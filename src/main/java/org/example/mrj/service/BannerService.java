package org.example.mrj.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.mrj.domain.BannerWrapper;
import org.example.mrj.domain.dto.ApiResponse;
import org.example.mrj.domain.entity.Banner;
import org.example.mrj.domain.entity.BannerSlider;
import org.example.mrj.repository.BannerRepository;
import org.example.mrj.repository.BannerSliderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BannerService
{

    private final BannerRepository bannerRepository;

    private final PhotoService photoService;

    private final ObjectMapper objectMapper;
    private final BannerSliderRepository sliderRepo;

    public ResponseEntity<ApiResponse<BannerWrapper>> addSlider(String link, Boolean active, MultipartFile photo)
    {
        ApiResponse<BannerWrapper> response = new ApiResponse<>();
        List<Banner> all = bannerRepository.findAll();
        if (all.isEmpty())
        {
            Banner banner = new Banner();
            banner.setSliders(new ArrayList<>());
            banner.getSliders().add(new BannerSlider(link, active, photoService.save(photo)));
            bannerRepository.save(banner);
            response.setData(new BannerWrapper(banner));
            response.setMessage("Created first banner successfully");
            return ResponseEntity.ok(response);
        }
        Banner banner = all.get(0);
        banner.getSliders().add(new BannerSlider(link, active, photoService.save(photo)));
        bannerRepository.save(banner);
        response.setData(new BannerWrapper(banner));
        response.setMessage("Added slider (photo)");
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<ApiResponse<BannerWrapper>> get()
    {
        ApiResponse<BannerWrapper> response = new ApiResponse<>();
        List<Banner> all = bannerRepository.findAll();
        if (all.isEmpty())
        {
            response.setMessage("Banner is null , not created yet");
            return ResponseEntity.status(404).body(response);
        }
        Banner banner = all.get(0);
        List<BannerSlider> sliderList = sliderRepo.findAllById(bannerRepository.getSlidersId());
        banner.setSliders(sliderList);
        response.setData(new BannerWrapper(banner));
        response.setMessage("Found");
        return ResponseEntity.status(200).body(response);
    }

    public ResponseEntity<ApiResponse<BannerWrapper>> update(Banner newBanner)
    {
        ApiResponse<BannerWrapper> response = new ApiResponse<>();
        List<Banner> all = bannerRepository.findAll();
        if (all.isEmpty())
        {
            response.setMessage("Banner is null , not created yet. You can't change any data");
            return ResponseEntity.status(404).body(response);
        }
        Banner fromDb = all.get(0);
        fromDb.setSliders(newBanner.getSliders());

/*        // Step 1 : Find which field are non-null from newBanner. Non-null field are must have changed
        // Step 2 : Find non-null field of newBanner from oldBanner
        // Step 3 : Replace oldBanner fields with non-null fields of newBanner

        for (BannerSlider i : newSlider)
        {
            if (i.getId() != null)
            {
                if (i.getLink() != null) replaceLink(oldSlider, i.getId(), i.getLink());
                if (i.getActive() != null) replaceActive(oldSlider, i.getId(), i.getActive());
            }
        }*/

        response.setData(new BannerWrapper(bannerRepository.save(fromDb)));
        response.setMessage("Updated");
        return ResponseEntity.status(200).body(response);
    }

    private void replaceActive(List<BannerSlider> old, Long to, Boolean value)
    {
        for (BannerSlider i : old)
            if (i.getId().equals(to)) i.setActive(value);
    }

    private void replaceLink(List<BannerSlider> old, Long to, String value)
    {
        for (BannerSlider i : old)
            if (i.getId().equals(to)) i.setLink(value);
    }

    public ResponseEntity<ApiResponse<?>> delete()
    {
        ApiResponse<?> response = new ApiResponse<>();
        List<Banner> all = bannerRepository.findAll();
        if (all.isEmpty())
        {
            response.setMessage("Banner is null , not created yet. You can't delete");
            return ResponseEntity.status(404).body(response);
        }
        bannerRepository.deleteById(all.get(0).getId());
        response.setMessage("Successfully deleted!");
        return ResponseEntity.status(200).body(response);
    }

}
