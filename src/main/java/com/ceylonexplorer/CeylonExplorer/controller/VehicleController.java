package com.ceylonexplorer.CeylonExplorer.controller;

import com.ceylonexplorer.CeylonExplorer.dto.VehicleDTO;
import com.ceylonexplorer.CeylonExplorer.entity.Vehicle;
import com.ceylonexplorer.CeylonExplorer.service.VehicleService;
import com.fasterxml.jackson.databind.ObjectMapper;

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
@RequestMapping("/vehicles")
@CrossOrigin
public class VehicleController {

    private final VehicleService vehicleService;
    private final ObjectMapper objectMapper;

    @Autowired
    public VehicleController(VehicleService vehicleService, ObjectMapper objectMapper) {
        this.vehicleService = vehicleService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        List<VehicleDTO> vehicleDTOs = vehicles.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vehicleDTOs);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<VehicleDTO> createVehicle(
            @RequestParam("vehicle") String vehicleJson,
            @RequestParam(value = "image", required = false) MultipartFile file) {

        try {
            // Parse the JSON string into a VehicleDTO object
            VehicleDTO vehicleDTO = objectMapper.readValue(vehicleJson, VehicleDTO.class);

            // Convert DTO to entity (without image)
            Vehicle vehicle = convertToEntity(vehicleDTO);

            // Save the vehicle entity first to get the generated ID
            Vehicle savedVehicle = vehicleService.saveVehicle(vehicle);

            // Process image if provided
            if (file != null && !file.isEmpty()) {
                byte[] imageBytes = file.getBytes();
                vehicleService.uploadImage(savedVehicle.getId(), imageBytes);
            }

            // Fetch the updated vehicle (with image if uploaded)
            Vehicle updatedVehicle = vehicleService.getVehicleById(savedVehicle.getId());
            return ResponseEntity.ok(convertToDTO(updatedVehicle));

        } catch (IOException e) {
            // Log the error for debugging
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new VehicleDTO()); // Return an empty VehicleDTO or handle the error appropriately
        }
    }

    // Helper methods to convert between DTO and entity
    private Vehicle convertToEntity(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setType(vehicleDTO.getType());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setPricePerDay(vehicleDTO.getPricePerDay());
        vehicle.setPhone(vehicleDTO.getPhone());
        return vehicle;
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setType(vehicle.getType());
        vehicleDTO.setModel(vehicle.getModel());
        vehicleDTO.setPricePerDay(vehicle.getPricePerDay());
        vehicleDTO.setPhone(vehicle.getPhone());
        // Correctly handle the image conversion:
        if (vehicle.getImage() != null) {
            vehicleDTO.setImage(vehicle.getImage());
        }
        return vehicleDTO;
    }
}