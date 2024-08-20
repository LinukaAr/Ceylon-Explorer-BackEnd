package com.ceylonexplorer.CeylonExplorer.controller;

import com.ceylonexplorer.CeylonExplorer.dto.GuideDTO;
import com.ceylonexplorer.CeylonExplorer.entity.Guide;
import com.ceylonexplorer.CeylonExplorer.service.GuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/guides")
public class GuideController {

    @Autowired
    private GuideService guideService;

    @GetMapping
    public List<GuideDTO> getAllGuides() {
        return guideService.getAllGuides().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuideDTO> getGuideById(@PathVariable Long id) {
        Guide guide = guideService.getGuideById(id);
        return guide != null ? ResponseEntity.ok(convertToDTO(guide)) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public GuideDTO createGuide(@RequestBody GuideDTO guideDTO) {
        Guide guide = convertToEntity(guideDTO);
        return convertToDTO(guideService.saveGuide(guide));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuide(@PathVariable Long id) {
        guideService.deleteGuide(id);
        return ResponseEntity.noContent().build();
    }

    private GuideDTO convertToDTO(Guide guide) {
        GuideDTO guideDTO = new GuideDTO();
        guideDTO.setId(guide.getId());
        guideDTO.setName(guide.getName());
        guideDTO.setExperience(guide.getExperience());
        guideDTO.setLanguages(guide.getLanguages());
        guideDTO.setPhone(guide.getPhone());
        guideDTO.setArea(guide.getArea());
        guideDTO.setAgeRange(guide.getAgeRange());
        guideDTO.setGender(guide.getGender());
        guideDTO.setImage(guide.getImage());//error
        return guideDTO;
    }

    private Guide convertToEntity(GuideDTO guideDTO) {
        Guide guide = new Guide();
        guide.setId(guideDTO.getId());
        guide.setName(guideDTO.getName());
        guide.setExperience(guideDTO.getExperience());
        guide.setLanguages(guideDTO.getLanguages());
        guide.setPhone(guideDTO.getPhone());
        guide.setArea(guideDTO.getArea());
        guide.setAgeRange(guideDTO.getAgeRange());
        guide.setGender(guideDTO.getGender());
        guide.setImage(guideDTO.getImage());//error
        return guide;
    }
}
