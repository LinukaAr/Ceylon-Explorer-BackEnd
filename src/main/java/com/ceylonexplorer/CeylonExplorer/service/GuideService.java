package com.ceylonexplorer.CeylonExplorer.service;


import com.ceylonexplorer.CeylonExplorer.entity.Guide;
import com.ceylonexplorer.CeylonExplorer.repository.GuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuideService {
    @Autowired
    private GuideRepository guideRepository;

    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }

    public Guide getGuideById(Long id) {
        return guideRepository.findById(id).orElse(null);
    }

    public Guide saveGuide(Guide guide) {
        return guideRepository.save(guide);
    }

    public void deleteGuide(Long id) {
        guideRepository.deleteById(id);
    }

    public void uploadImage(Long guideId, byte[] imageBytes) {
        Optional<Guide> optionalGuide = guideRepository.findById(guideId);
    if (optionalGuide.isPresent()) {
        System.out.println("Received image bytes (service): " + imageBytes.length); // Log size
        Guide guide = optionalGuide.get();
        guide.setImage(imageBytes);
        guideRepository.save(guide); 
        System.out.println("Image saved to Guide entity");
    } else {
            // Handle case where guide with given ID is not found
            throw new RuntimeException("Guide not found with ID: " + guideId);
        }
    }

}
