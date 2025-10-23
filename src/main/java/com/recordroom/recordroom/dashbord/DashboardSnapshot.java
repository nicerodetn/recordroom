package com.recordroom.recordroom.dashbord;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long totalClosedFiles;
    private Long totalClosedFilesOutstanding;
    private Long totalCallbookFiles;
    private Long totalCallbookFilesOutstanding;
    private Long totalReferenceOutstanding;
    private Long totalRti;

    private Long totalBooks;
    private Long totalbooksOutstanding;


    private Long totalRegisters;
    private Long totalRegistersOutstanding;


    private LocalDate snapshotDate;

    @PrePersist
    @PreUpdate
    public void setSnapshotDate() {

            this.snapshotDate = LocalDate.now();

            }

}
