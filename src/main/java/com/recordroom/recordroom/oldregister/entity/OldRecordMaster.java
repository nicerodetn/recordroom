package com.recordroom.recordroom.oldregister.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "old_record_master")
public class OldRecordMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String details_of_record;

    private String types_of_record;

    private LocalDate  created_date;

    @Column(unique = true)
    private Long record_serial_no;

    private Long year;

    private Long rack_no;
}
