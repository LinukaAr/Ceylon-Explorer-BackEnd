package com.ceylonexplorer.CeylonExplorer.controller;

import com.ceylonexplorer.CeylonExplorer.dto.HotelDTO;
import com.ceylonexplorer.CeylonExplorer.entity.Hotel;
import com.ceylonexplorer.CeylonExplorer.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public HotelDTO createHotel(@RequestBody HotelDTO hotelDTO) {
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
        return hotelDTO;
    }

    private Hotel convertToEntity(HotelDTO hotelDTO) {
        Hotel hotel = new Hotel();
        hotel.setId(hotelDTO.getId());
        hotel.setName(hotelDTO.getName());
        hotel.setLocation(hotelDTO.getLocation());
        hotel.setStarRating(hotelDTO.getStarRating());
        hotel.setPhone(hotelDTO.getPhone());
        return hotel;
    }
}
