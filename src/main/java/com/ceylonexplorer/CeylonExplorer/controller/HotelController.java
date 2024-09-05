package com.ceylonexplorer.CeylonExplorer.controller;

import com.ceylonexplorer.CeylonExplorer.dto.HotelDTO;
import com.ceylonexplorer.CeylonExplorer.entity.Hotel;
import com.ceylonexplorer.CeylonExplorer.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/hotels")
@CrossOrigin
public class HotelController {

    private final HotelService hotelService;
    private final ObjectMapper objectMapper;

    @Autowired
    public HotelController(HotelService hotelService, ObjectMapper objectMapper) {
        this.hotelService = hotelService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HotelDTO> createHotel(
            @RequestParam("hotel") String hotelJson,
            @RequestParam(value = "image", required = false) MultipartFile file) {

        try {
            // Parse the JSON string into a HotelDTO object
            HotelDTO hotelDTO = objectMapper.readValue(hotelJson, HotelDTO.class);

            // Convert DTO to entity (without image)
            Hotel hotel = convertToEntity(hotelDTO);

            // Save the hotel entity first to get the generated ID
            Hotel savedHotel = hotelService.saveHotel(hotel);

            // Process image if provided
            if (file != null && !file.isEmpty()) {
                byte[] imageBytes = file.getBytes();
                hotelService.uploadImage(savedHotel.getId(), imageBytes);
            }

            // Fetch the updated hotel (with image if uploaded)
            Hotel updatedHotel = hotelService.getHotelById(savedHotel.getId());
            return ResponseEntity.ok(convertToDTO(updatedHotel));

        } catch (IOException e) {
            // Log the error for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new HotelDTO()); // Return an empty HotelDTO or handle the error appropriately
        }
    }

    // Helper methods to convert between DTO and entity
    private Hotel convertToEntity(HotelDTO hotelDTO) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelDTO.getName());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setStarRating(hotelDTO.getStarRating());
        hotel.setPhone(hotelDTO.getPhone());
        hotel.setPrice(hotelDTO.getPrice());
        hotel.setAmenities(hotelDTO.getAmenities());
        hotel.setRooms(hotelDTO.getRooms());
        return hotel;
    }

    private HotelDTO convertToDTO(Hotel hotel) {
        HotelDTO hotelDTO = new HotelDTO();
        hotelDTO.setId(hotel.getId());
        hotelDTO.setName(hotel.getName());
        hotelDTO.setLocation(hotel.getLocation());
        hotelDTO.setStarRating(hotel.getStarRating());
        hotelDTO.setPhone(hotel.getPhone());
        hotelDTO.setPrice(hotel.getPrice());
        hotelDTO.setAmenities(hotel.getAmenities());
        hotelDTO.setRooms(hotel.getRooms());
        // Correctly handle the image conversion:
        if (hotel.getImage() != null) {
            hotelDTO.setImage(hotel.getImage());
        }
        return hotelDTO;
    }
}