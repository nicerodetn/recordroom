package com.recordroom.recordroom.reference.controller;


import com.recordroom.recordroom.closed.service.RecordService;
import com.recordroom.recordroom.reference.entity.Reference;
import com.recordroom.recordroom.reference.entity.ReferenceDTO;
import com.recordroom.recordroom.reference.service.ReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

import com.recordroom.recordroom.dto.DataTableRequest;
import com.recordroom.recordroom.dto.DataTableResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;



@Controller
@RequestMapping("/reference")
public class ReferenceController {

    @Autowired
    private ReferenceService referenceService;

    @Autowired
    private RecordService recordService;


    @GetMapping("/reference_in_entry")
    public String inEntry(Model model) {
        return "fragments/reference/in_entry :: searchrecord";
    }


    @GetMapping("/reference_out_entry")
    public String loadForm(Model model) {
        model.addAttribute("reference", new Reference()); // new empty form
        model.addAttribute("sections", recordService.getAllSections());
        return "fragments/reference/out_entry :: loadFormFragment"; // return only fragment
    }

    @PostMapping("/save")
    public String saveRecord(@ModelAttribute Reference reference, Model model) {

            reference.setIs_active(true);
            Optional<Reference> r = referenceService.saveRecord(reference);
            model.addAttribute("reference", new Reference());
            model.addAttribute("sections", recordService.getAllSections());
            model.addAttribute("successMessage", "✅ Record saved successfully! "+"Note Serial No"+r.get().getSerialNo());
            return "fragments/reference/out_entry :: loadFormFragment";
    }

    @GetMapping("/searchForInWardEntry")
    public String searchForInWardEntry(@RequestParam String drSerialNo, Model model) {

        Optional<Reference> reference = referenceService.search(Long.parseLong(drSerialNo));
        if(reference.isPresent()){
            model.addAttribute("reference", reference.get());
            model.addAttribute("referencedto", new ReferenceDTO());

            return "fragments/reference/in_entry :: outwardform";
        }
        else {
            model.addAttribute("errorMessage", "✅ File is not Outstanding!");
            return "fragments/reference/in_entry :: searchrecord";
        }
    }

    @PostMapping("/saveInWardEntry")
    public String saveInWardEntry(@ModelAttribute ReferenceDTO referenceDTO, @RequestParam String seriolmo ,Model model) {

        Reference reference = referenceService.search(Long.parseLong(seriolmo)).get();
        reference.setIs_active(false);
        reference.setIn_recordRoomDealingHandName(referenceDTO.getIn_recordRoomDealingHandName());
        reference.setIn_sectionDealingHandName(referenceDTO.getIn_sectionDealingHandName());
        reference.setIn_sectionDealingHandPhoneNo(referenceDTO.getIn_sectionDealingHandPhoneNo());
        referenceService.saveRecord(reference);
        model.addAttribute("successMessage", "✅ Record Updated successfully!");
        return "fragments/reference/in_entry :: searchrecord";
    }

    @GetMapping("/masterReport")
    public String masterReport(Model model) {
        // Create the file types map for the dropdown
        Map<Integer, String> fileTypes = new HashMap<>();
        fileTypes.put(1, "DR");
        fileTypes.put(2, "PR");
        fileTypes.put(3, "Files");
        fileTypes.put(4, "Others");

        model.addAttribute("fileTypes", fileTypes);
        return "fragments/reference/report :: tabler";
    }


    @PostMapping("/load_reference_data")
    @ResponseBody
    public DataTableResponse<Reference> loadReferenceData(
            @RequestBody DataTableRequest request,
            @RequestParam(name = "fileType", required = false) Integer fileType
    ) {
        Page<Reference> page = referenceService.findByFilters(request, fileType);

        DataTableResponse<Reference> response = new DataTableResponse<>();
        response.setDraw(request.getDraw());
        response.setRecordsTotal(page.getTotalElements());
        response.setRecordsFiltered(page.getTotalElements());
        response.setData(page.getContent());

        return response;
    }

}
