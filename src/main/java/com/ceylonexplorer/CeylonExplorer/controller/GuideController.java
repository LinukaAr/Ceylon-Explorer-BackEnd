package com.ceylonexplorer.CeylonExplorer.controller;

import com.ceylonexplorer.CeylonExplorer.dto.GuideDTO;
import com.ceylonexplorer.CeylonExplorer.entity.Guide;
import com.ceylonexplorer.CeylonExplorer.service.GuideService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/guides")
@CrossOrigin
public class GuideController {

    private final GuideService guideService;
    private final ObjectMapper objectMapper;

    @Autowired
    public GuideController(GuideService guideService, ObjectMapper objectMapper) {
        this.guideService = guideService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<GuideDTO>> getAllGuides() {
        List<Guide> guides = guideService.getAllGuides();
        List<GuideDTO> guideDTOs = guides.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(guideDTOs);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<GuideDTO> createGuide(
            @RequestParam("guide") String guideJson,
            @RequestParam(value = "image", required = false) MultipartFile file) {

        try {
            // Parse the JSON string into a GuideDTO object
            GuideDTO guideDTO = objectMapper.readValue(guideJson, GuideDTO.class);

            // Convert DTO to entity (without image)
            Guide guide = convertToEntity(guideDTO);

            // Save the guide entity first to get the generated ID
            Guide savedGuide = guideService.saveGuide(guide);

            // Process image if provided
            if (file != null && !file.isEmpty()) {
                byte[] imageBytes = file.getBytes();
                guideService.uploadImage(savedGuide.getId(), imageBytes);
            }

            // Fetch the updated guide (with image if uploaded)
            Guide updatedGuide = guideService.getGuideById(savedGuide.getId());
            return ResponseEntity.ok(convertToDTO(updatedGuide));

        } catch (IOException e) {
            // Log the error for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GuideDTO()); // Return an empty GuideDTO or handle the error appropriately
        }
    }

    // Helper methods to convert between DTO and entity
    private Guide convertToEntity(GuideDTO guideDTO) {
        Guide guide = new Guide();
        guide.setName(guideDTO.getName());
        guide.setExperience(guideDTO.getExperience());
        guide.setLanguages(guideDTO.getLanguages());
        guide.setPhone(guideDTO.getPhone());
        guide.setArea(guideDTO.getArea());
        guide.setAgeRange(guideDTO.getAgeRange());
        guide.setGender(guideDTO.getGender());
        return guide;
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
        // Correctly handle the image conversion:
        if (guide.getImage() != null) {
            guideDTO.setImage(guide.getImage());
        }
        return guideDTO;
    }
}