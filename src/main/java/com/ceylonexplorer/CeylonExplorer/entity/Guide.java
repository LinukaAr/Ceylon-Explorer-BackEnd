package com.ceylonexplorer.CeylonExplorer.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "guides")
public class Guide {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String experience;
    private String languages;
    private String phone;
    private String area;
    private String ageRange;
    private String gender;
//    private String rating;

    @Lob
    private byte[] image; // Use @Lob to store binary data
}
