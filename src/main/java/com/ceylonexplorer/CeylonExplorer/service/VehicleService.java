package com.ceylonexplorer.CeylonExplorer.service;

import com.ceylonexplorer.CeylonExplorer.entity.Guide;
import com.ceylonexplorer.CeylonExplorer.entity.Vehicle;
import com.ceylonexplorer.CeylonExplorer.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }

    public Vehicle saveVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        vehicleRepository.deleteById(id);
    }

    public void uploadImage(Long vehicleId, byte[] imageBytes) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(vehicleId);
    if (optionalVehicle.isPresent()) {
        System.out.println("Received image bytes (service): " + imageBytes.length); // Log size
        Vehicle vehicle = optionalVehicle.get();
        vehicle.setImage(imageBytes);
        vehicleRepository.save(vehicle); 
        System.out.println("Image saved to vehicle entity");
    } else {
            // Handle case where guide with given ID is not found
            throw new RuntimeException("Guide not found with ID: " + vehicleId);
        }
    }
}
