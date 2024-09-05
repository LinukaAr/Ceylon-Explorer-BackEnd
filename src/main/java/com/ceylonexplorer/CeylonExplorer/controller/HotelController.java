package com.ceylonexplorer.CeylonExplorer.controller;

import com.ceylonexplorer.CeylonExplorer.dto.HotelDTO;
import com.ceylonexplorer.CeylonExplorer.entity.Hotel;
import com.ceylonexplorer.CeylonExplorer.service.HotelService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hotels")
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping
    public List<HotelDTO> getAllHotels() {
        return hotelService.getAllHotels().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDTO> getHotelById(@PathVariable Long id) {
        Hotel hotel = hotelService.getHotelById(id);
        return hotel != null ? ResponseEntity.ok(convertToDTO(hotel)) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public HotelDTO createHotel(
            @RequestPart("hotel") String hotelJson,
            @RequestPart("image") MultipartFile image) throws Exception {
        HotelDTO hotelDTO = new ObjectMapper().readValue(hotelJson, HotelDTO.class);
        // Handle the image file as needed
        Hotel hotel = convertToEntity(hotelDTO);
        return convertToDTO(hotelService.saveHotel(hotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.noContent().build();
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
        return hotelDTO;
    }

    private Hotel convertToEntity(HotelDTO hotelDTO) {
        Hotel hotel = new Hotel();
        hotel.setId(hotelDTO.getId());
        hotel.setName(hotelDTO.getName());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setStarRating(hotelDTO.getStarRating());
        hotel.setPhone(hotelDTO.getPhone());
        hotel.setPrice(hotelDTO.getPrice());
        hotel.setAmenities(hotelDTO.getAmenities());
        hotel.setRooms(hotelDTO.getRooms());
        return hotel;
    }
}
