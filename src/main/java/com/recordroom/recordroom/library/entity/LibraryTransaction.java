package com.recordroom.recordroom.library.entity;

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
public class LibraryTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "book_master")
    private Library library;

    private String purpose_of_taking;
    private String nameOfStaffTaking;
    private String ph_no_of_staff_taking;
    private String book_name;
    private LocalDate date_of_taking;
    private LocalDate date_of_return;
    private String remarks;

}
