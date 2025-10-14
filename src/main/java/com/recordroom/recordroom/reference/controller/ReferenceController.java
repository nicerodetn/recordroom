package com.recordroom.recordroom.reference.controller;


import com.recordroom.recordroom.closed.controller.dto.FileMasterReportDTO;
import com.recordroom.recordroom.closed.controller.dto.FileOutgoingDTO;
import com.recordroom.recordroom.closed.entity.FileRecord;
import com.recordroom.recordroom.closed.entity.RecordTransactionDetails;
import com.recordroom.recordroom.closed.service.RecordService;
import com.recordroom.recordroom.reference.entity.Reference;
import com.recordroom.recordroom.reference.entity.ReferenceDTO;
import com.recordroom.recordroom.reference.service.ReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        List<Reference> records = referenceService.allListActive();

        List<ReferenceDTO> referenceDTOList = records.stream().map(record -> {
            ReferenceDTO dto = new ReferenceDTO();

            dto.setSerialNo(record.getSerialNo());
            dto.setOut_sectionDealingHandName(record.getOut_sectionDealingHandName());
            dto.setOut_sectionDealingHandPhoneNo(record.getOut_sectionDealingHandPhoneNo());
            dto.setCreated_date(record.getCreated_date());

            dto.setDescription(record.getDescription());
            dto.setSerial_no_user(record.getSerial_no_user());
            dto.setFile_year(record.getFile_year());

            if (record.getType()==1)
            {
                dto.setTypeee("DR");
            }
            if (record.getType()==2)
            {
                dto.setTypeee("PR");
            }
            if (record.getType()==3)
            {
                dto.setTypeee("Files");
            }
            if (record.getType()==4)
            {
                dto.setTypeee("Others");
            }

            return dto;

        }).collect(Collectors.toList());

        model.addAttribute("reference", referenceDTOList);
        return "fragments/reference/report :: tabler"; // Thymeleaf template name for testing
    }


}
