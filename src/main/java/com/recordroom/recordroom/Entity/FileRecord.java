package com.recordroom.recordroom.Entity;


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

    @ManyToOne
    @JoinColumn(name = "section_id",referencedColumnName = "id")
    private Section section;

    private LocalDate fileClosingDate;
    private LocalDate handingOverDate;
    private String remarks;
    private Integer rackDetails;
    private String sectionDealingHandName;
    private String sectionDealingHandPhoneNo;
    private String recordRoomDealingHandName;

}
