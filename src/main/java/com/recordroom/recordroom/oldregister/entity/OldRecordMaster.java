package com.recordroom.recordroom.oldregister.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "old_record_master")
public class OldRecordMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String details_of_record;

    private String types_of_record;

    private LocalDate created_date;

    @Column(unique = true)
    private String record_serial_no;

    private Long year;

    private Long rack_no;

    private String taluk;

    private String village;


    private Integer quality_status;


    public String getQualityStatusLabel() {
        if (quality_status == null) return "Unknown";
        switch (quality_status) {
            case 1: return "Good";
            case 2: return "Not Available";
            case 3: return "Damaged";
            case 4: return "Fully Damaged";
            default: return String.valueOf(quality_status);
        }
    }
}