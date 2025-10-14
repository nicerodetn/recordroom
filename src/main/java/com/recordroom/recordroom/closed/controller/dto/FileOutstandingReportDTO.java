package com.recordroom.recordroom.closed.controller.dto;

import com.recordroom.recordroom.closed.entity.FileRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileOutstandingReportDTO {



    private Long drSerialNo;
    private Integer dr_year;
    private Boolean active;
    private LocalDate dateOfFileOutgoing;
    private String purposeOfTakingFile;

    private String section;

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
}
