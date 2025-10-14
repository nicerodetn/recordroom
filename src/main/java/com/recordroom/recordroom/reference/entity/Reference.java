package com.recordroom.recordroom.reference.entity;


import com.recordroom.recordroom.closed.entity.Section;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reference {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Long serialNo;
    private Integer type;

    @ManyToOne
    @JoinColumn(name = "section_id",referencedColumnName = "id")
    private Section section;

    private String description;
    private Integer file_year;
    private Long serial_no_user;

    private Boolean is_active;

    private String in_sectionDealingHandName;
    private String in_sectionDealingHandPhoneNo;
    private String in_recordRoomDealingHandName;

    private String out_sectionDealingHandName;
    private String out_sectionDealingHandPhoneNo;
    private String out_recordRoomDealingHandName;

    private LocalDate created_date;
    private LocalDate updated_date;

    @PrePersist
    protected void onCreate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        String serialStr = LocalDateTime.now().format(formatter);
        this.serialNo = Long.parseLong(serialStr);
        this.created_date=LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated_date = LocalDate.now();
    }


}
