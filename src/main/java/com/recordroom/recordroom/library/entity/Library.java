package com.recordroom.recordroom.library.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @CreationTimestamp
    @Column(name = "created_date", updatable = false) // updatable=false ensures it's never changed after creation
    private LocalDate createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private LocalDate updatedDate;

    private Boolean deleted = false;
    private LocalDate deletedDate;

    @Transient
    private boolean outstanding;

    @Transient
    private Long remainingDays;

    public Long getRemainingDays() {
        if (deletedDate == null) return null;
        long daysPassed = java.time.temporal.ChronoUnit.DAYS.between(deletedDate, LocalDate.now());
        long remaining = 7 - daysPassed;
        return remaining < 0 ? 0 : remaining;
    }

}
