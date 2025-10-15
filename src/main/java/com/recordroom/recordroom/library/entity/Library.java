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
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name_of_book;
    private String publication;
    private String description;
    private Integer book_serial_no;
    private Integer rack_no;
    private String remarks;
    private LocalDate date_in;

}
