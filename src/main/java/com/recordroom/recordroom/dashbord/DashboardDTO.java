package com.recordroom.recordroom.dashbord;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {

    private Long total_closed_files;
    private Long total_closed_files_outstanding;

    private Long total_callbook_files_outstanding;
    private Long total_callbook_files;

    private Long total_reference_outstanding;

    private Long total_rti;

    private Long total_books;
    private Long total_books_outstanding;

    private Long totalRegisters;
    private Long totalRegistersOutstanding;

}
