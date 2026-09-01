package com.recordroom.recordroom.oldregister.controller;

import com.recordroom.recordroom.oldregister.entity.OldRecordMaster;
import com.recordroom.recordroom.oldregister.entity.RegisterTransactionDetails;
import com.recordroom.recordroom.oldregister.service.DistrictMasterService; // UPDATED: Import Service
import com.recordroom.recordroom.oldregister.service.OldRecordMasterService;
import com.recordroom.recordroom.oldregister.service.RegisterTransactionService;
import com.recordroom.recordroom.staff.service.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/oldregister")
public class OldRegisterController {

    @Autowired
    private OldRecordMasterService oldRecordMasterService;

    @Autowired
    private RegisterTransactionService registerTransactionService;

    @Autowired
    private DistrictMasterService districtMasterService;

    @Autowired
    private StaffService staffService;

    @ModelAttribute("staffList")
    public List<String> staffList() {
        return staffService.getActiveStaffNames();
    }



    @GetMapping("/master_creation")
    public String loadMasterCreationForm(Model model) {
        model.addAttribute("record", new OldRecordMaster());

        List<String> taluks = districtMasterService.getAllTaluks();
        model.addAttribute("talukList", taluks);

        return "fragments/old_register/master_creation :: form";
    }


    @GetMapping("/get_villages")
    public String getVillages(@RequestParam("taluk") String taluk, Model model) {

        List<String> villages = districtMasterService.getVillagesByTaluk(taluk);
        model.addAttribute("villageList", villages);

        return "fragments/old_register/master_creation :: villageOptions";
    }

    @PostMapping("/save_master")
    public String saveMasterRecord(@ModelAttribute OldRecordMaster record, Model model) {
        if (record.getId() != null) {
            Optional<OldRecordMaster> oldRecord = oldRecordMasterService.findById(record.getId());
            if (oldRecord.isPresent()) {
                Optional<RegisterTransactionDetails> activeTrans = registerTransactionService.findActiveBySerialNoAndYear(
                        oldRecord.get().getRecord_serial_no(), oldRecord.get().getYear()
                );
                if (activeTrans.isPresent()) {
                    model.addAttribute("record", record);
                    model.addAttribute("talukList", districtMasterService.getAllTaluks());
                    model.addAttribute("errorMessage", "❌ Cannot edit record. Record is outstanding!");
                    return "fragments/old_register/master_creation :: form";
                }
            }
        }

        Optional<OldRecordMaster> saved;
        if (record.getId() != null) {
            saved = oldRecordMasterService.updateRecord(record);
        } else {
            saved = oldRecordMasterService.saveRecord(record);
        }

        model.addAttribute("talukList", districtMasterService.getAllTaluks());

        if (saved.isPresent()) {
            OldRecordMaster newRecord = saved.get();
            model.addAttribute("record", new OldRecordMaster());
            model.addAttribute("successMessage", record.getId() != null ? "✅ Record updated successfully!" : "✅ Record Saved! Generated Serial No: " + newRecord.getRecord_serial_no());
        } else {
            model.addAttribute("record", record);
            model.addAttribute("errorMessage", "❌ Error: Duplicate Entry.");
        }
        return "fragments/old_register/master_creation :: form";
    }



    @GetMapping("/inward_entry")
    public String loadInwardEntryForm(Model model) {
        return "fragments/old_register/inward_entry :: searchrecord";
    }

    @GetMapping("/searchForInwardEntry")
    public String searchForInwardEntry(@RequestParam String record_serial_no,
                                       @RequestParam Long year,
                                       Model model) {
        Optional<OldRecordMaster> master = oldRecordMasterService.findBySerialNoAndYear(record_serial_no, year);

        if (master.isEmpty()) {
            model.addAttribute("errorMessage", "❌ No record found with that Serial Number and Year!");
            return "fragments/old_register/inward_entry :: searchrecord";
        }

        Optional<RegisterTransactionDetails> activeTransaction =
                registerTransactionService.findActiveBySerialNoAndYear(record_serial_no, year);

        if (activeTransaction.isEmpty()) {
            model.addAttribute("errorMessage", "✅ Record is already inside.");
            return "fragments/old_register/inward_entry :: searchrecord";
        }

        model.addAttribute("master", master.get());
        model.addAttribute("transaction", activeTransaction.get());
        return "fragments/old_register/inward_entry :: inwardform";
    }

    @PostMapping("/saveInwardEntry")
    public String saveInwardEntry(@RequestParam Integer transactionId,
                                  @RequestParam LocalDate dateOfReturn,
                                  @RequestParam String sectionPersonName_in,
                                  @RequestParam Long sectionPersonPhNumber_in,
                                  @RequestParam String recordPersonName_in,
                                  Model model) {

        Optional<RegisterTransactionDetails> transaction = registerTransactionService.findById(transactionId);

        if (transaction.isPresent()) {
            RegisterTransactionDetails trans = transaction.get();
            trans.setIs_active_status(false);
            trans.setIs_out_in(0); // 0 = IN
            trans.setDateOfReturn(dateOfReturn);
            trans.setSectionPersonName_in(sectionPersonName_in);
            trans.setSectionPersonPhNumber_in(sectionPersonPhNumber_in);
            trans.setRecordPersonName_in(recordPersonName_in);
            trans.setTransactionDate(LocalDate.now());

            registerTransactionService.saveTransaction(trans);
            model.addAttribute("successMessage", "✅ Record received successfully!");
        } else {
            model.addAttribute("errorMessage", " Transaction not found!");
        }

        return "fragments/old_register/inward_entry :: searchrecord";
    }



    @GetMapping("/outward_entry")
    public String loadOutwardEntryForm(Model model) {
        return "fragments/old_register/outward_entry :: searchrecord";
    }

    @GetMapping("/searchForOutwardEntry")
    public String searchForOutwardEntry(@RequestParam String record_serial_no,
                                        @RequestParam Long year,
                                        Model model) {
        Optional<OldRecordMaster> master = oldRecordMasterService.findBySerialNoAndYear(record_serial_no, year);

        if (master.isEmpty()) {
            model.addAttribute("errorMessage", " No record found");
            return "fragments/old_register/outward_entry :: searchrecord";
        }

        Optional<RegisterTransactionDetails> activeTransaction =
                registerTransactionService.findActiveBySerialNoAndYear(record_serial_no, year);

        if (activeTransaction.isPresent()) {
            model.addAttribute("errorMessage", " Record is already out.");
            return "fragments/old_register/outward_entry :: searchrecord";
        }

        model.addAttribute("master", master.get());
        model.addAttribute("transaction", new RegisterTransactionDetails());
        return "fragments/old_register/outward_entry :: outwardform";
    }

    @PostMapping("/saveOutwardEntry")
    public String saveOutwardEntry(@RequestParam Integer masterId,
                                   @RequestParam LocalDate dateOfOutGoing,
                                   @RequestParam String purposeTakingOut,
                                   @RequestParam String sectionPersonName_out,
                                   @RequestParam String sectionPersonName_number,
                                   @RequestParam String recordPersonName_out,
                                   Model model) {

        Optional<OldRecordMaster> master = oldRecordMasterService.findById(masterId);

        if (master.isPresent()) {
            RegisterTransactionDetails trans = new RegisterTransactionDetails();
            trans.setMaster(master.get());
            trans.setIs_active_status(true);
            trans.setIs_out_in(1);
            trans.setDateOfOutGoing(dateOfOutGoing);
            trans.setPurposeTakingOut(purposeTakingOut);
            trans.setSectionPersonName_out(sectionPersonName_out);
            trans.setSectionPersonName_number(sectionPersonName_number);
            trans.setRecordPersonName_out(recordPersonName_out);
            trans.setTransactionDate(LocalDate.now());

            registerTransactionService.saveTransaction(trans);
            model.addAttribute("successMessage", "✅ Record issued successfully!");
        } else {
            model.addAttribute("errorMessage", " Master record not found!");
        }

        return "fragments/old_register/outward_entry :: searchrecord";
    }



    @GetMapping("/transaction_details")
    public String viewTransactionDetails(Model model) {
        model.addAttribute("transactions", registerTransactionService.getAllCompletedTransactions());
        return "fragments/old_register/transaction_details :: tabler";
    }

    @GetMapping("/outstanding_records")
    public String viewOutstandingRecords(Model model) {
        model.addAttribute("outstandingRecords", registerTransactionService.getAllActiveTransactions());
        return "fragments/old_register/outstanding_records :: tabler";
    }

    // ----- Edit Record -----
    @GetMapping("/edit/{id}")
    public String editRecord(@PathVariable Integer id, Model model) {
        var optional = oldRecordMasterService.findById(id);
        if (optional.isPresent()) {
            model.addAttribute("record", optional.get());
            model.addAttribute("talukList", districtMasterService.getAllTaluks());
            return "fragments/old_register/master_creation :: form";
        }
        model.addAttribute("errorMessage", "Record not found");
        return "fragments/old_register/master_creation :: form";
    }

    @PostMapping("/edit")
    public String updateRecord(@ModelAttribute OldRecordMaster record, Model model) {
        oldRecordMasterService.updateRecord(record);
        model.addAttribute("successMessage", "✅ Record updated successfully!");
        return "redirect:/oldregister/view_details";
    }


    // ----- View Details -----
    @GetMapping("/view_details")
    public String viewAllRecords(Model model) {
        List<OldRecordMaster> records = oldRecordMasterService.getAllRecords();
        for (OldRecordMaster record : records) {
            boolean isOutstanding = registerTransactionService.findActiveBySerialNoAndYear(
                    record.getRecord_serial_no(), record.getYear()
            ).isPresent();
            record.setOutstanding(isOutstanding);
        }
        model.addAttribute("records", records);
        return "fragments/old_register/view_details :: tabler";
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteRecord(@PathVariable Integer id) {
        Optional<OldRecordMaster> record = oldRecordMasterService.findById(id);
        if (record.isEmpty()) {
            return ResponseEntity.status(404).body("Record not found");
        }
        Optional<RegisterTransactionDetails> activeTrans = registerTransactionService.findActiveBySerialNoAndYear(
                record.get().getRecord_serial_no(), record.get().getYear()
        );
        if (activeTrans.isPresent()) {
            return ResponseEntity.badRequest().body("Cannot delete outstanding record");
        }
        oldRecordMasterService.deleteRecord(id);
        return ResponseEntity.ok("Record deleted successfully");
    }
}