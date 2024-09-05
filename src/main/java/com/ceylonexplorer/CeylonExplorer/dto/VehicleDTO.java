package com.ceylonexplorer.CeylonExplorer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VehicleDTO {
    private Long id;
    private String type;
    private String model;
    private String pricePerDay;
    private String phone;
    private byte[] image;
}
