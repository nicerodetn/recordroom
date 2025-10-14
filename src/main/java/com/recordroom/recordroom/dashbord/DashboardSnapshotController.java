package com.recordroom.recordroom.dashbord;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/dashboard")
public class DashboardSnapshotController {

    @Autowired
    private DashboardSnapshotService dashboardSnapshotService;

    @GetMapping("/load_search")
    public String fileInEntryLoadForm(Model model) {
        return "fragments/summary_report/date_wise_report :: tabler";
    }


    @GetMapping("/snapshots")
    public String getSnapshotsInRange(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        DashboardSnapshot snap = dashboardSnapshotService.findBySnapshotDate(date);

        if (snap == null) {
            model.addAttribute("noData", true);
        } else {
            model.addAttribute("snap", snap);
            model.addAttribute("noData", false);
        }


        return "fragments/summary_report/snapshot-table :: tabler"; // returns just the <table>
    }



}
