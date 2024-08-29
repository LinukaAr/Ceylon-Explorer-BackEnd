package com.ceylonexplorer.CeylonExplorer.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter 
public class GuideDTO {
    private Long id;
    private String name;
    private String experience;
    private String languages;
    private String phone;
    private String area;
    private String ageRange;
    private String gender;
    private byte[] image; // Consider using String for Base64 encoded images
}

