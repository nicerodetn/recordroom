package com.recordroom.recordroom.reference.entity;


import com.recordroom.recordroom.closed.entity.Section;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReferenceDTO {


    private Integer id;

    private Long serialNo;
    private Integer type;
    private String section;
    private String typeee;



    private String description;
    private Integer file_year;
    private Long serial_no_user;

    private String in_sectionDealingHandName;
    private String in_sectionDealingHandPhoneNo;
    private String in_recordRoomDealingHandName;

    private String out_sectionDealingHandName;
    private String out_sectionDealingHandPhoneNo;
    private String out_recordRoomDealingHandName;

    private LocalDate created_date;
    private LocalDate updated_date;

}
