package com.ceylonexplorer.CeylonExplorer.service;

import com.ceylonexplorer.CeylonExplorer.entity.Guide;
import com.ceylonexplorer.CeylonExplorer.entity.Hotel;
import com.ceylonexplorer.CeylonExplorer.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelService {
    @Autowired
    private HotelRepository hotelRepository;

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id).orElse(null);
    }

    public Hotel saveHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }
    public void uploadImage(Long hotelId, byte[] imageBytes) {
        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
    if (optionalHotel.isPresent()) {
        System.out.println("Received image bytes (service): " + imageBytes.length); // Log size
        Hotel hotel = optionalHotel.get();
        hotel.setImage(imageBytes);
        hotelRepository.save(hotel); 
        System.out.println("Image saved to Hotel entity");
    } else {
            // Handle case where guide with given ID is not found
            throw new RuntimeException("Guide not found with ID: " + hotelId);
        }
    }
}