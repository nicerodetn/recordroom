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
public class FileRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer fileType;

    @Column( unique = true)
    private Long drSerialNo;

    private Integer dr_year;

    @ManyToOne
    @JoinColumn(name = "section_id",referencedColumnName = "id")
    private Section section;

    private Integer total_volume;
    private Integer total_pages;
    private Integer note_pages;

    private LocalDate fileClosingDate;
    private LocalDate handingOverDate;
    private String remarks;
    private Integer rackDetails;
    private String sectionDealingHandName;
    private String sectionDealingHandPhoneNo;
    private String recordRoomDealingHandName;


    private LocalDate created_date;

    @PrePersist
    protected void onCreate() {
        this.created_date = LocalDate.now();
    }

}
