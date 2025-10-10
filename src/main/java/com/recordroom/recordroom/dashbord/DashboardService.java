package com.recordroom.recordroom.dashbord;


import com.recordroom.recordroom.callbook.repo.CallBookRepository;
import com.recordroom.recordroom.closed.repo.RecordRepository;
import com.recordroom.recordroom.closed.repo.RecordTransactionRepository;
import com.recordroom.recordroom.closed.repo.SectionRepository;
import com.recordroom.recordroom.rti.repo.RtiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private CallBookRepository callBookRepository;

    @Autowired
    private RecordRepository recordRepo;

    @Autowired
    private RecordTransactionRepository transRepo;

    @Autowired
    private RtiRepository rtiRepository;

    public DashboardDTO getDashboardStats() {
        DashboardDTO dto = new DashboardDTO();
        // Example data fetching - adjust based on your actual queries
        dto.setTotal_closed_files(recordRepo.countClosedFiles());
        dto.setTotal_closed_files_outstanding(transRepo.countClosedFilesOutstanding());
        dto.setTotal_callbook_files_outstanding(callBookRepository.countCallBookOutstanding());
        dto.setTotal_rti(rtiRepository.countAllRti());
        return dto;
    }



}
