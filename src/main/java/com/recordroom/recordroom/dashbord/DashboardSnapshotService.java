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
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

@Service
public class DashboardSnapshotService {

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
        snap.setTotalBooks(dto.getTotal_books());
        snap.setTotalbooksOutstanding(dto.getTotal_books_outstanding());
        snap.setTotalRegisters(dto.getTotalRegisters());
        snap.setTotalRegistersOutstanding(dto.getTotalRegistersOutstanding());
        dashboardSnapshotRepository.save(snap);
    }

    public DashboardSnapshot findBySnapshotDate(LocalDate date) {
        return dashboardSnapshotRepository.findBySnapshotDate(date);
    }

    public List<DashboardComparisonDTO> getComparisonReport(LocalDate fromDate, LocalDate toDate) {
        DashboardSnapshot fromSnap = dashboardSnapshotRepository.findBySnapshotDate(fromDate);
        DashboardSnapshot toSnap = dashboardSnapshotRepository.findBySnapshotDate(toDate);

        List<DashboardComparisonDTO> list = new ArrayList<>();

        
        list.add(new DashboardComparisonDTO("Total Closed Files",
                getValue(fromSnap, DashboardSnapshot::getTotalClosedFiles),
                getValue(toSnap, DashboardSnapshot::getTotalClosedFiles)));

        list.add(new DashboardComparisonDTO("Closed Files Outstanding",
                getValue(fromSnap, DashboardSnapshot::getTotalClosedFilesOutstanding),
                getValue(toSnap, DashboardSnapshot::getTotalClosedFilesOutstanding)));

        list.add(new DashboardComparisonDTO("Total Call Book Files",
                getValue(fromSnap, DashboardSnapshot::getTotalCallbookFiles),
                getValue(toSnap, DashboardSnapshot::getTotalCallbookFiles)));

        list.add(new DashboardComparisonDTO("Call Book Outstanding",
                getValue(fromSnap, DashboardSnapshot::getTotalCallbookFilesOutstanding),
                getValue(toSnap, DashboardSnapshot::getTotalCallbookFilesOutstanding)));

        list.add(new DashboardComparisonDTO("Reference Files Outstanding",
                getValue(fromSnap, DashboardSnapshot::getTotalReferenceOutstanding),
                getValue(toSnap, DashboardSnapshot::getTotalReferenceOutstanding)));

        list.add(new DashboardComparisonDTO("Total RTI",
                getValue(fromSnap, DashboardSnapshot::getTotalRti),
                getValue(toSnap, DashboardSnapshot::getTotalRti)));

        list.add(new DashboardComparisonDTO("Total Library Books",
                getValue(fromSnap, DashboardSnapshot::getTotalBooks),
                getValue(toSnap, DashboardSnapshot::getTotalBooks)));

        list.add(new DashboardComparisonDTO("Total Outstanding Books",
                getValue(fromSnap, DashboardSnapshot::getTotalbooksOutstanding),
                getValue(toSnap, DashboardSnapshot::getTotalbooksOutstanding)));

        list.add(new DashboardComparisonDTO("Total Old Registers",
                getValue(fromSnap, DashboardSnapshot::getTotalRegisters),
                getValue(toSnap, DashboardSnapshot::getTotalRegisters)));

        list.add(new DashboardComparisonDTO("Total Old Registers Outstanding",
                getValue(fromSnap, DashboardSnapshot::getTotalRegistersOutstanding),
                getValue(toSnap, DashboardSnapshot::getTotalRegistersOutstanding)));

        return list;
    }

    // This is the method being called above
    private Long getValue(DashboardSnapshot snap, Function<DashboardSnapshot, Long> getter) {
        return (snap == null || getter.apply(snap) == null) ? 0L : getter.apply(snap);
    }
}