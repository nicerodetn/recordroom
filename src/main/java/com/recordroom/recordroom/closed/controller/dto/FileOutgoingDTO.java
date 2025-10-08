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
public class FileOutgoingDTO {

    private Integer fileRecordId; // <-- only the ID of the existing record
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
}
