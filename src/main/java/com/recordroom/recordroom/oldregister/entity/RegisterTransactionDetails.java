package com.recordroom.recordroom.oldregister.entity;
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
@Table(name = "register_transaction_details")
public class RegisterTransactionDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "master_id", referencedColumnName = "id")
    private OldRecordMaster master;

    private Boolean is_active_status;
    private Integer is_out_in;
    private LocalDate dateOfOutGoing;
    private String purposeTakingOut;
    private String sectionPersonName_out;
    private String sectionPersonName_number;
    private String recordPersonName_out;
    private LocalDate dateOfReturn;
    private String sectionPersonName_in;
    private Long SectionPersonPhNumber_in;
    private String recordPersonName_in;
    private LocalDate transactionDate;

    private LocalDate created_date;
    private LocalDate updated_date;
}
