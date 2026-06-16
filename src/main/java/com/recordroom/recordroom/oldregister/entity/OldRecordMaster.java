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

    private Boolean deleted = false;
    private LocalDate deletedDate;

    @Transient
    private boolean outstanding;

    @Transient
    private Long remainingDays;

    public Long getRemainingDays() {
        if (deletedDate == null) return null;
        long daysPassed = java.time.temporal.ChronoUnit.DAYS.between(deletedDate, LocalDate.now());
        long remaining = 7 - daysPassed;
        return remaining < 0 ? 0 : remaining;
    }
}