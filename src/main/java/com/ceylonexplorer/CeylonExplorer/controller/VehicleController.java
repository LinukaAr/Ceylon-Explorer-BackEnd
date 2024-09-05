package com.ceylonexplorer.CeylonExplorer.controller;

import com.ceylonexplorer.CeylonExplorer.dto.VehicleDTO;
import com.ceylonexplorer.CeylonExplorer.entity.Vehicle;
import com.ceylonexplorer.CeylonExplorer.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public List<VehicleDTO> getAllVehicles() {
        return vehicleService.getAllVehicles().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return vehicle != null ? ResponseEntity.ok(convertToDTO(vehicle)) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = { "multipart/form-data" })
    public VehicleDTO createVehicle(
            @RequestPart("vehicle") String vehicleJson,
            @RequestPart("image") MultipartFile image) throws Exception {
        VehicleDTO vehicleDTO = new ObjectMapper().readValue(vehicleJson, VehicleDTO.class);
        // Handle the image file as needed
        Vehicle vehicle = convertToEntity(vehicleDTO);
        return convertToDTO(vehicleService.saveVehicle(vehicle));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(vehicle.getId());
        vehicleDTO.setType(vehicle.getType());
        vehicleDTO.setModel(vehicle.getModel());
        vehicleDTO.setPricePerDay(vehicle.getPricePerDay());
        vehicleDTO.setPhone(vehicle.getPhone());
        return vehicleDTO;
    }

    private Vehicle convertToEntity(VehicleDTO vehicleDTO) {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleDTO.getId());
        vehicle.setType(vehicleDTO.getType());
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setPricePerDay(vehicleDTO.getPricePerDay());
        vehicle.setPhone(vehicleDTO.getPhone());
        return vehicle;
    }
}
