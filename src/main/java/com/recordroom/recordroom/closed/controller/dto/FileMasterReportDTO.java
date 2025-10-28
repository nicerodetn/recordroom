package com.recordroom.recordroom.closed.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileMasterReportDTO {

    private Integer fileType;
    private Long drSerialNo;
    private Integer dr_year;
    private String section;
    private LocalDate fileClosingDate;
    private LocalDate handingOverDate;
    private String remarks;
    private Integer rackDetails;
    private String sectionDealingHandName;
    private String sectionDealingHandPhoneNo;
    private String recordRoomDealingHandName;

    private Integer total_volume;
    private Integer total_pages;
    private Integer note_pages;

}
