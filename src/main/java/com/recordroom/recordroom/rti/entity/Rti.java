package com.recordroom.recordroom.rti.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Rti {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate rtiDate;
    private LocalTime in_time ;
    private LocalTime out_time ;
    private String pettitioner_name;
    private String pettitioner_address;
    private String pettitioner_phoneno;
    private String letter_no;
    private String subject;
    private String e_challan_no;
    private Long amount;
    private String rr_dealing_name;
    private String remarks;


    private LocalDate created_date;
    private LocalDate updated_date;

    @PrePersist
    protected void onCreate() {
        this.created_date = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_date = LocalDate.now();
    }

}
