package com.recordroom.recordroom.closed.controller.file_circulation;

import com.recordroom.recordroom.closed.controller.dto.FileMasterReportDTO;
import com.recordroom.recordroom.closed.controller.dto.FileOutgoingDTO;
import com.recordroom.recordroom.closed.controller.dto.FileOutstandingReportDTO;
import com.recordroom.recordroom.closed.entity.FileRecord;
import com.recordroom.recordroom.closed.entity.Section;
import com.recordroom.recordroom.closed.entity.RecordTransactionDetails;
import com.recordroom.recordroom.closed.service.RecordService;
import com.recordroom.recordroom.closed.service.RecordTransactionService;
import com.recordroom.recordroom.dto.DataTableRequest;
import com.recordroom.recordroom.dto.DataTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

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
        return "fragments/closed/master_creation :: form";
    }

    @GetMapping("/file_master_out_entry")
    public String fileOutEntryLoadForm(Model model) {
        return "fragments/closed/out_entry :: searchrecord";
    }

    @GetMapping("/file_master_in_entry")
    public String fileInEntryLoadForm(Model model) {
        return "fragments/closed/in_entry :: searchrecord";
    }

    @GetMapping("/file_master_view")
    public String fileViewMaster(Model model) {
        return "fragments/closed/view_master :: tabler";
    }


    @GetMapping("/load_form")
    public String saveRecord(Model model) {
        model.addAttribute("record", new FileRecord());
        model.addAttribute("sections", recordService.getAllSections());
        return "fragments/closed/master_creation :: loadFormFragment";
    }

    @PostMapping("/save")
    public String saveRecord(@ModelAttribute FileRecord record, Model model) {
        Optional<FileRecord> r = recordService.saveRecord(record);
        if(r.isPresent())
        {
            model.addAttribute("record", new FileRecord());
            model.addAttribute("sections", recordService.getAllSections());
            model.addAttribute("successMessage", "✅ Record saved successfully!");
            return "fragments/closed/master_creation :: loadFormFragment";

        }else {
            model.addAttribute("record", new FileRecord());
            model.addAttribute("sections", recordService.getAllSections());
            model.addAttribute("successMessage", "✅ Dulpicate DR Number given!");
            return "fragments/closed/master_creation :: loadFormFragment";
        }
    }

    @GetMapping("/api/section/{id}")
    @ResponseBody
    public ResponseEntity<Section> getSectionDetails(@PathVariable Integer id) {
        return recordService.getSectionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/searchForOutWardEntry")
    public String searchFile(@RequestParam String drSerialNo,@RequestParam String dr_year, Model model) {

        Optional<FileRecord> record = recordService.findBydrSerialNoAndYear(Long.parseLong(drSerialNo),Integer.parseInt(dr_year));
        Optional<RecordTransactionDetails> recordTransactionDetails = recordTransactionService.findBydrSerialNoAndActiveAndYear(Long.parseLong(drSerialNo),Integer.parseInt(dr_year));

        if (record.isPresent()) {
            if(recordTransactionDetails.isPresent()){
                model.addAttribute("errorMessage", "✅ File record already  Outward !");
                return "fragments/closed/out_entry :: searchrecord";
            }
            else {
                model.addAttribute("record", record.get());
                FileOutgoingDTO fileOutgoingDTO = new FileOutgoingDTO();
                fileOutgoingDTO.setFileRecordId(record.get().getId());
                model.addAttribute("recordTransactionDetails", fileOutgoingDTO);
                return "fragments/closed/out_entry :: outwardform";
            }
        } else {
            model.addAttribute("errorMessage", "✅ No file record found with that number!");
            return "fragments/closed/out_entry :: searchrecord";
        }
    }

    @PostMapping("/saveOutWardEntry")
    public String saveRecordTransactionDetails(@ModelAttribute FileOutgoingDTO fileOutgoingDTO, Model model) {

        Optional<FileRecord> fileRecord = recordService.findById(fileOutgoingDTO.getFileRecordId());
        RecordTransactionDetails outgoing = new RecordTransactionDetails();
        outgoing.setActive(true);
        outgoing.setDr_year(fileRecord.get().getDr_year());
        outgoing.setDrSerialNo(fileRecord.get().getDrSerialNo());
        outgoing.setFileRecord(fileRecord.get());
        outgoing.setDateOfFileOutgoing(fileOutgoingDTO.getDateOfFileOutgoing());
        outgoing.setPurposeOfTakingFile(fileOutgoingDTO.getPurposeOfTakingFile());
        outgoing.setSectionDealingHandName(fileOutgoingDTO.getSectionDealingHandName());
        outgoing.setSectionDealingHandPhoneNo(fileOutgoingDTO.getSectionDealingHandPhoneNo());
        outgoing.setRecordRoomDealingHandName(fileOutgoingDTO.getRecordRoomDealingHandName());
        recordTransactionService.saveTransaction(outgoing);
        model.addAttribute("successMessage", "✅ Record saved successfully!");
        return "fragments/closed/out_entry :: searchrecord";
    }

    @GetMapping("/searchForInWardEntry")
    public String searchFileTransaction(@RequestParam String drSerialNo,@RequestParam String dr_year, Model model) {

        Optional<FileRecord> record = recordService.findBydrSerialNoAndYear(Long.parseLong(drSerialNo),Integer.parseInt(dr_year));
        Optional<RecordTransactionDetails> recordTransactionDetails = recordTransactionService.findBydrSerialNoAndActiveAndYear(Long.parseLong(drSerialNo),Integer.parseInt(dr_year));

        if(recordTransactionDetails.isPresent()){
            model.addAttribute("record", record.get());
            FileOutgoingDTO fileOutgoingDTO = new FileOutgoingDTO();
            fileOutgoingDTO.setFileRecordId(record.get().getId());
            model.addAttribute("recordTransactionDetails", fileOutgoingDTO);
            return "fragments/closed/in_entry :: outwardform";
        }
        else {
            model.addAttribute("errorMessage", "✅ File is not Outstanding!");
            return "fragments/closed/in_entry :: searchrecord";
        }
    }

    @PostMapping("/saveInWardEntry")
    public String saveRecordTransactionDetailsInWard(@ModelAttribute FileOutgoingDTO fileOutgoingDTO, Model model) {

        Optional<FileRecord> fileRecord = recordService.findById(fileOutgoingDTO.getFileRecordId());
        Optional<RecordTransactionDetails> recordTransactionDetails = recordTransactionService.findBydrSerialNoAndActiveAndYear(fileRecord.get().getDrSerialNo(),fileRecord.get().getDr_year());
        RecordTransactionDetails outgoing = recordTransactionDetails.get();
        outgoing.setActive(false);
        outgoing.setDrSerialNo(fileRecord.get().getDrSerialNo());
        outgoing.setFileRecord(fileRecord.get());
        outgoing.setDateOfReturn(fileOutgoingDTO.getDateOfReturn());
        outgoing.setPurposeOfReturn(fileOutgoingDTO.getPurposeOfReturn());
        outgoing.setSectionDealingHandName_InWard(fileOutgoingDTO.getSectionDealingHandName());
        outgoing.setSectionDealingHandPhoneNo_InWard(fileOutgoingDTO.getSectionDealingHandPhoneNo_InWard());
        outgoing.setRecordRoomDealingHandName_InWard(fileOutgoingDTO.getRecordRoomDealingHandName_InWard());
        recordTransactionService.saveTransaction(outgoing);
        model.addAttribute("successMessage", "✅ Record saved successfully!");
        return "fragments/closed/in_entry :: searchrecord";
    }


    @GetMapping("/outWardReport")
    public String showReport(Model model) {
        return "fragments/closed/out_entry_report :: tabler";
    }

    @PostMapping("/load_outward_data")
    @ResponseBody
    public DataTableResponse<FileOutstandingReportDTO> loadOutwardData(
            @RequestBody DataTableRequest request
    ) {

        Page<RecordTransactionDetails> page = recordTransactionService.findActiveByFilters(request);


        List<FileOutstandingReportDTO> dtoList = page.getContent().stream().map(record -> {
            FileOutstandingReportDTO dto = new FileOutstandingReportDTO();
            dto.setDr_year(record.getDr_year());
            dto.setDrSerialNo(record.getDrSerialNo());
            dto.setDateOfFileOutgoing(record.getDateOfFileOutgoing());
            dto.setPurposeOfTakingFile(record.getPurposeOfTakingFile());

            if(record.getFileRecord() != null && record.getFileRecord().getSection() != null) {
                dto.setSection(record.getFileRecord().getSection().getSection());
            } else {
                dto.setSection("N/A");
            }

            dto.setSectionDealingHandName(record.getSectionDealingHandName());
            dto.setSectionDealingHandPhoneNo(record.getSectionDealingHandPhoneNo());
            dto.setRecordRoomDealingHandName(record.getRecordRoomDealingHandName());
            return dto;
        }).collect(Collectors.toList());


        DataTableResponse<FileOutstandingReportDTO> response = new DataTableResponse<>();
        response.setDraw(request.getDraw());
        response.setRecordsTotal(page.getTotalElements());
        response.setRecordsFiltered(page.getTotalElements());
        response.setData(dtoList);

        return response;
    }



    @GetMapping("/masterReport")
    public String masterReport(
            @RequestParam(name = "sectionCategory", required = false) String sectionCategory,
            Model model
    ) {

        model.addAttribute("selectedCategory", sectionCategory);
        model.addAttribute("categories", recordService.getSectionCategories());


        return "fragments/closed/view_master_report :: tabler";
    }


    @PostMapping("/load_master_data")
    @ResponseBody
    public DataTableResponse<FileMasterReportDTO> loadMasterData(
            @RequestBody DataTableRequest request,
            @RequestParam(name = "sectionCategory", required = false) String sectionCategory
    ) {

        Page<FileRecord> page = recordService.findByFilters(request, sectionCategory);


        List<FileMasterReportDTO> dtoList = page.getContent().stream().map(record -> {
            FileMasterReportDTO dto = new FileMasterReportDTO();
            dto.setDrSerialNo(record.getDrSerialNo());
            dto.setDr_year(record.getDr_year());
            dto.setFileType(record.getFileType());

            if (record.getSection() != null) {
                dto.setSection(record.getSection().getSection());
            } else {
                dto.setSection("N/A");
            }
            dto.setFileClosingDate(record.getFileClosingDate());
            dto.setHandingOverDate(record.getHandingOverDate());
            dto.setRemarks(record.getRemarks());
            dto.setRackDetails(record.getRackDetails());
            dto.setSectionDealingHandName(record.getSectionDealingHandName());
            dto.setSectionDealingHandPhoneNo(record.getSectionDealingHandPhoneNo());
            dto.setRecordRoomDealingHandName(record.getRecordRoomDealingHandName());
            dto.setTotal_volume(record.getTotal_volume());
            dto.setTotal_pages(record.getTotal_pages());
            dto.setNote_pages(record.getNote_pages());
            return dto;
        }).collect(Collectors.toList());


        DataTableResponse<FileMasterReportDTO> response = new DataTableResponse<>();
        response.setDraw(request.getDraw());
        response.setRecordsTotal(page.getTotalElements());
        response.setRecordsFiltered(page.getTotalElements());
        response.setData(dtoList);

        return response;
    }

    @GetMapping("/destructionreport")
    public String destructionReport(Model model) {
        List<FileRecord> records = recordService.destructionReport();

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
        return "fragments/closed/destrucion :: tabler"; // Thymeleaf template name
    }



}