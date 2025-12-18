package com.recordroom.recordroom.oldregister.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "district_master")
public class DistrictMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(name = "district_code")
    private String districtCode;


    @Column(name = "district_name")
    private String districtName;


    @Column(name = "taluk_code")
    private String talukCode;


    @Column(name = "taluk_name")
    private String talukName;


    @Column(name = "village_name")
    private String villageName;


    @Column(name = "village_code")
    private String villageCode;
}