package com.recordroom.recordroom.dashbord;


import com.recordroom.recordroom.callbook.repo.CallBookRepository;
import com.recordroom.recordroom.closed.repo.RecordRepository;
import com.recordroom.recordroom.closed.repo.RecordTransactionRepository;
import com.recordroom.recordroom.reference.repo.ReferenceRepo;
import com.recordroom.recordroom.rti.repo.RtiRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DashboardSnapshotService {

    @Autowired
    private CallBookRepository callBookRepository;

    @Autowired
    private RecordRepository recordRepo;

    @Autowired
    private RecordTransactionRepository transRepo;

    @Autowired
    private RtiRepository rtiRepository;

    @Autowired
    private ReferenceRepo referenceRepo;

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private DashboardSnapshotRepository dashboardSnapshotRepository;




    @Scheduled(cron = "0 */30 * * * *")
    @Transactional
    public void saveTodaySnapshot() {
        DashboardDTO dto = dashboardService.getDashboardStats();
        LocalDate today = LocalDate.now();
        dashboardSnapshotRepository.deleteBySnapshotDate(today);
        DashboardSnapshot snap = new DashboardSnapshot();
        snap.setTotalClosedFiles(dto.getTotal_closed_files());
        snap.setTotalClosedFilesOutstanding(dto.getTotal_closed_files_outstanding());
        snap.setTotalCallbookFiles(dto.getTotal_callbook_files());
        snap.setTotalCallbookFilesOutstanding(dto.getTotal_callbook_files_outstanding());
        snap.setTotalReferenceOutstanding(dto.getTotal_reference_outstanding());
        snap.setTotalRti(dto.getTotal_rti());
        dashboardSnapshotRepository.save(snap);
    }

    public DashboardSnapshot findBySnapshotDate(LocalDate date)
    {
        return dashboardSnapshotRepository.findBySnapshotDate(date);
    }

}
