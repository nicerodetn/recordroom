package com.recordroom.recordroom.callbook.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer new_drSerialNo;
    private Integer new_dr_year;


    private Integer call_book_no;

    private Integer old_drSerialNo;
    private Integer old_dr_year;

    private String sectionDealingHandName;
    private String sectionDealingHandPhoneNo;
    private String recordRoomDealingHandName;

    private LocalDate possible_out_date;

    private Integer unique_key;
    private Boolean is_current;
    private Integer in_out_status;
    //0=in ,1=out
    private Date out_going_date;
    private Date in_coming_date;

    private String purpose_Of_Incoming_File;
    private String purpose_Of_Outgoing_Fle;

    private Integer rack_no;

    private Boolean closed_status;

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
