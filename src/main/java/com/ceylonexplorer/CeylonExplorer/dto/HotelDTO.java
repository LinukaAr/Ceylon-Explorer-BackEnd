package com.ceylonexplorer.CeylonExplorer.dto;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HotelDTO {
    private Long id;
    private String name;
    private String location;
    private int starRating;
    private String phone;

}
