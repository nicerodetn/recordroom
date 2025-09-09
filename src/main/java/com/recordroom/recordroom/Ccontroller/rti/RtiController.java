package com.recordroom.recordroom.Ccontroller.rti;


import com.recordroom.recordroom.Entity.closed.FileRecord;
import com.recordroom.recordroom.Entity.closed.RecordTransactionDetails;
import com.recordroom.recordroom.Entity.rti.Rti;
import com.recordroom.recordroom.Service.rti.RtiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping("/rti_Report")
    public String showReport(Model model) {
        List<Rti> rti = rtiService.getAllRti();
        model.addAttribute("rti", rti);
        return "fragments/rti/rti_report :: tabler"; // Thymeleaf template name
    }

}
