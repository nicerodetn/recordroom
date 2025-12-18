package com.recordroom.recordroom.rti.controller;


import com.recordroom.recordroom.rti.entity.Rti;
import com.recordroom.recordroom.rti.service.RtiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/rti")
public class RtiController {

    @Autowired
    private RtiService rtiService;


    @GetMapping("/load_form")
    public String loadForm(Model model) {
        model.addAttribute("rti", new Rti()); // new empty form
        return "fragments/rti/rti_entry :: loadFormFragment"; // return only fragment
    }


    @PostMapping("/save")
    public String saveRti(@ModelAttribute Rti rti, Model model) {
        Optional<Rti> r = rtiService.saveRti(rti);
        if (r.isPresent()) {
            model.addAttribute("rti", new Rti());
            model.addAttribute("successMessage", "✅ Record saved successfully!");
            return "fragments/rti/rti_entry :: loadFormFragment";
        } else {
            model.addAttribute("rti", new Rti());
            model.addAttribute("successMessage", "✅ Failed to Save!");
            return "fragments/rti/rti_entry :: loadFormFragment";
        }
    }

    @GetMapping("/rti_report")
    public String showReport(Model model) {
        List<Rti> rti = rtiService.getAllRti();
        model.addAttribute("rti", rti);
        return "fragments/rti/rti_report :: tabler";
    }

    @GetMapping("/rti_monthly_report")
    public String showMonthlyReport(
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model
    ) {
        List<Rti> rtiList = rtiService.getFilteredRti(month, year);

        model.addAttribute("rti", rtiList);
        model.addAttribute("selectedYear", year);
        model.addAttribute("selectedMonth", month);

        // Pointing to the NEW file created in Step 1
        return "fragments/rti/rti_monthly_report :: tabler";
    }

}
