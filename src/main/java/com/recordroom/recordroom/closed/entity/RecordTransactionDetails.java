package com.recordroom.recordroom.closed.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordTransactionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "record_id")
    private FileRecord fileRecord;

    private Long drSerialNo;
    private Integer dr_year;
    private Boolean active;
    private LocalDate dateOfFileOutgoing;
    private String purposeOfTakingFile;

    private String sectionDealingHandName;
    private String sectionDealingHandPhoneNo;
    private String recordRoomDealingHandName;

    private LocalDate dateOfReturn;
    private String purposeOfReturn;
    private String sectionDealingHandName_InWard;
    private String sectionDealingHandPhoneNo_InWard;
    private String recordRoomDealingHandName_InWard;
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
