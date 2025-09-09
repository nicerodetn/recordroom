package com.recordroom.recordroom.Ccontroller.closed.file_circulation;

import com.recordroom.recordroom.Ccontroller.closed.dto.FileMasterReportDTO;
import com.recordroom.recordroom.Ccontroller.closed.dto.FileOutgoingDTO;
import com.recordroom.recordroom.Entity.closed.FileRecord;
import com.recordroom.recordroom.Entity.closed.RecordTransactionDetails;
import com.recordroom.recordroom.Service.closed.RecordService;
import com.recordroom.recordroom.Service.closed.RecordTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/circulation")
public class CirculationController {


    private final RecordService recordService;

    @Autowired
    private RecordTransactionService recordTransactionService;

    public CirculationController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/file_master_creation")
    public String fileMasterCreationLoadForm(Model model) {
        return "fragments/file_master/master_creation :: form";
    }

    @GetMapping("/file_master_out_entry")
    public String fileOutEntryLoadForm(Model model) {
        return "fragments/file_master/out_entry :: searchrecord";
    }

    @GetMapping("/file_master_in_entry")
    public String fileInEntryLoadForm(Model model) {
        return "fragments/file_master/in_entry :: searchrecord";
    }

    @GetMapping("/file_master_view")
    public String fileViewMaster(Model model) {
        return "fragments/file_master/view_master :: tabler";
    }


    @GetMapping("/load_form")
    public String saveRecord(Model model) {
        // recordService.saveRecord(record);

        // model.addAttribute("successMessage", "✅ Record saved successfully!");
        model.addAttribute("record", new FileRecord()); // new empty form
        model.addAttribute("sections", recordService.getAllSections());
        return "fragments/file_master/master_creation :: loadFormFragment"; // return only fragment
    }

    @PostMapping("/save")
    public String saveRecord(@ModelAttribute FileRecord record, Model model) {



       Optional<FileRecord> r = recordService.saveRecord(record);

        // Prepare fresh form after save


        if(r.isPresent())
        {
            model.addAttribute("record", new FileRecord());
            model.addAttribute("sections", recordService.getAllSections());
            model.addAttribute("successMessage", "✅ Record saved successfully!");
            return "fragments/file_master/master_creation :: loadFormFragment";

        }else {
            model.addAttribute("record", new FileRecord());
            model.addAttribute("sections", recordService.getAllSections());
            model.addAttribute("successMessage", "✅ Dulpicate DR Number given!");
            return "fragments/file_master/master_creation :: loadFormFragment";
        }

        // return fragment only (HTMX swap)

    }

    @GetMapping("/searchForOutWardEntry")
    public String searchFile(@RequestParam String drSerialNo,@RequestParam String dr_year, Model model) {

        Optional<FileRecord> record = recordService.findBydrSerialNoAndYear(Long.parseLong(drSerialNo),Integer.parseInt(dr_year));

        Optional<RecordTransactionDetails> recordTransactionDetails = recordTransactionService.findBydrSerialNoAndActiveAndYear(Long.parseLong(drSerialNo),Integer.parseInt(dr_year));

        System.out.println("Inside serach");
        if (record.isPresent()) {
            if(recordTransactionDetails.isPresent()){
                model.addAttribute("errorMessage", "✅ File record already  Outward !");
                return "fragments/file_master/out_entry :: searchrecord";
            }
            else {
                model.addAttribute("record", record.get());
                FileOutgoingDTO fileOutgoingDTO = new FileOutgoingDTO();
                fileOutgoingDTO.setFileRecordId(record.get().getId());
                model.addAttribute("recordTransactionDetails", fileOutgoingDTO);
                return "fragments/file_master/out_entry :: outwardform";
            }
        } else {
            model.addAttribute("errorMessage", "✅ No file record found with that number!");
            return "fragments/file_master/out_entry :: searchrecord";
        }
    }

    @PostMapping("/saveOutWardEntry")
    public String saveRecordTransactionDetails(@ModelAttribute FileOutgoingDTO fileOutgoingDTO, Model model) {
        Optional<FileRecord> fileRecord = recordService.findById(fileOutgoingDTO.getFileRecordId());
        System.out.println("pppppppp::::::"+fileOutgoingDTO.getFileRecordId());
        RecordTransactionDetails outgoing = new RecordTransactionDetails();
        outgoing.setActive(true);
        outgoing.setDr_year(fileRecord.get().getDr_year());
        outgoing.setDrSerialNo(fileRecord.get().getDrSerialNo());
        outgoing.setFileRecord(fileRecord.get());  // Set the existing record
        outgoing.setDateOfFileOutgoing(fileOutgoingDTO.getDateOfFileOutgoing());
        outgoing.setPurposeOfTakingFile(fileOutgoingDTO.getPurposeOfTakingFile());
        outgoing.setSectionDealingHandName(fileOutgoingDTO.getSectionDealingHandName());
        outgoing.setSectionDealingHandPhoneNo(fileOutgoingDTO.getSectionDealingHandPhoneNo());
        outgoing.setRecordRoomDealingHandName(fileOutgoingDTO.getRecordRoomDealingHandName());
        recordTransactionService.saveTransaction(outgoing);
        model.addAttribute("successMessage", "✅ Record saved successfully!");
        return "fragments/file_master/out_entry :: searchrecord";
    }


    @GetMapping("/searchForInWardEntry")
    public String searchFileTransaction(@RequestParam String drSerialNo,@RequestParam String dr_year, Model model) {

        Optional<FileRecord> record = recordService.findBydrSerialNoAndYear(Long.parseLong(drSerialNo),Integer.parseInt(dr_year));

        Optional<RecordTransactionDetails> recordTransactionDetails = recordTransactionService.findBydrSerialNoAndActiveAndYear(Long.parseLong(drSerialNo),Integer.parseInt(dr_year));
        System.out.println("Inside serach");
            if(recordTransactionDetails.isPresent()){
                model.addAttribute("record", record.get());
                FileOutgoingDTO fileOutgoingDTO = new FileOutgoingDTO();
                fileOutgoingDTO.setFileRecordId(record.get().getId());
                model.addAttribute("recordTransactionDetails", fileOutgoingDTO);
                return "fragments/file_master/in_entry :: outwardform";
            }
            else {
                model.addAttribute("errorMessage", "✅ File is not Outstanding!");
                return "fragments/file_master/in_entry :: searchrecord";
            }
        }

    @PostMapping("/saveInWardEntry")
    public String saveRecordTransactionDetailsInWard(@ModelAttribute FileOutgoingDTO fileOutgoingDTO, Model model) {

        Optional<FileRecord> fileRecord = recordService.findById(fileOutgoingDTO.getFileRecordId());

        Optional<RecordTransactionDetails> recordTransactionDetails = recordTransactionService.findBydrSerialNoAndActiveAndYear(fileRecord.get().getDrSerialNo(),fileRecord.get().getDr_year());

        System.out.println("pppppppp::::::"+fileOutgoingDTO.getFileRecordId());
        RecordTransactionDetails outgoing = recordTransactionDetails.get();
        outgoing.setActive(false);
        outgoing.setDrSerialNo(fileRecord.get().getDrSerialNo());
        outgoing.setFileRecord(fileRecord.get());  // Set the existing record

        outgoing.setDateOfReturn(fileOutgoingDTO.getDateOfReturn());
        outgoing.setPurposeOfReturn(fileOutgoingDTO.getPurposeOfReturn());
        outgoing.setSectionDealingHandName_InWard(fileOutgoingDTO.getSectionDealingHandName());
        outgoing.setSectionDealingHandPhoneNo_InWard(fileOutgoingDTO.getSectionDealingHandPhoneNo_InWard());
        outgoing.setRecordRoomDealingHandName_InWard(fileOutgoingDTO.getRecordRoomDealingHandName_InWard());
        recordTransactionService.saveTransaction(outgoing);
        model.addAttribute("successMessage", "✅ Record saved successfully!");
        return "fragments/file_master/in_entry :: searchrecord";
    }

    @GetMapping("/outWardReport")
    public String showReport(Model model) {
        List<RecordTransactionDetails> records = recordTransactionService.getActiveRecords();
        model.addAttribute("records", records);
        return "fragments/file_master/out_entry_report :: tabler"; // Thymeleaf template name
    }

    @GetMapping("/masterReport")
    public String masterReport(Model model) {
        List<FileRecord> records = recordService.getAllRecords();

        List<FileMasterReportDTO> fileMasterReportDTOList = records.stream().map(record -> {
            FileMasterReportDTO dto = new FileMasterReportDTO();
            dto.setDrSerialNo(record.getDrSerialNo());
            dto.setFileType(record.getFileType());
            dto.setSection(record.getSection().getDescription());
            dto.setFileClosingDate(record.getFileClosingDate());
            dto.setHandingOverDate(record.getHandingOverDate());
            dto.setRemarks(record.getRemarks());
            dto.setRackDetails(record.getRackDetails());
            dto.setSectionDealingHandName(record.getSectionDealingHandName());
            dto.setSectionDealingHandPhoneNo(record.getSectionDealingHandPhoneNo());
            dto.setRecordRoomDealingHandName(record.getRecordRoomDealingHandName());
            return dto;
        }).collect(Collectors.toList());

        model.addAttribute("records", fileMasterReportDTOList);
        return "fragments/file_master/view_master_report :: tabler"; // Thymeleaf template name for testing
    }



}




