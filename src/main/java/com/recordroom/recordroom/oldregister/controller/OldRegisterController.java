package com.recordroom.recordroom.oldregister.controller;

import com.recordroom.recordroom.oldregister.entity.OldRecordMaster;
import com.recordroom.recordroom.oldregister.entity.RegisterTransactionDetails;
import com.recordroom.recordroom.oldregister.service.OldRecordMasterService;
import com.recordroom.recordroom.oldregister.service.RegisterTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/oldregister")
public class OldRegisterController {

    @Autowired
    private OldRecordMasterService oldRecordMasterService;

    @Autowired
    private RegisterTransactionService registerTransactionService;

    @GetMapping("/master_creation")
    public String loadMasterCreationForm(Model model) {
        model.addAttribute("record", new OldRecordMaster());
        return "fragments/old_register/master_creation :: form";
    }

    @PostMapping("/save_master")
    public String saveMasterRecord(@ModelAttribute OldRecordMaster record, Model model) {
        // created_date is already set from the form via th:field
        Optional<OldRecordMaster> saved = oldRecordMasterService.saveRecord(record);

        if (saved.isPresent()) {
            model.addAttribute("record", new OldRecordMaster());
            model.addAttribute("successMessage", "✅ Record saved successfully!");
        } else {
            model.addAttribute("record", new OldRecordMaster());
            model.addAttribute("errorMessage", "❌ Duplicate Record Serial Number!");
        }
        return "fragments/old_register/master_creation :: form";
    }

    @GetMapping("/inward_entry")
    public String loadInwardEntryForm(Model model) {
        return "fragments/old_register/inward_entry :: searchrecord";
    }

    @GetMapping("/outward_entry")
    public String loadOutwardEntryForm(Model model) {
        return "fragments/old_register/outward_entry :: searchrecord";
    }

    @GetMapping("/searchForInwardEntry")
    public String searchForInwardEntry(@RequestParam Long record_serial_no,
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
            model.addAttribute("errorMessage", "✅ Record is already inside. No outstanding entry!");
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
                                  @RequestParam Integer sectionPersonPhNumber_in,
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
            model.addAttribute("errorMessage", "❌ Transaction not found!");
        }

        return "fragments/old_register/inward_entry :: searchrecord";
    }

    @GetMapping("/searchForOutwardEntry")
    public String searchForOutwardEntry(@RequestParam Long record_serial_no,
                                        @RequestParam Long year,
                                        Model model) {
        Optional<OldRecordMaster> master = oldRecordMasterService.findBySerialNoAndYear(record_serial_no, year);

        if (master.isEmpty()) {
            model.addAttribute("errorMessage", "❌ No record found ");
            return "fragments/old_register/outward_entry :: searchrecord";
        }

        Optional<RegisterTransactionDetails> activeTransaction =
                registerTransactionService.findActiveBySerialNoAndYear(record_serial_no, year);

        if (activeTransaction.isPresent()) {
            model.addAttribute("errorMessage", "❌ Record is already out.");
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
            trans.setIs_out_in(1); // 1 = OUT
            trans.setDateOfOutGoing(dateOfOutGoing);
            trans.setPurposeTakingOut(purposeTakingOut);
            trans.setSectionPersonName_out(sectionPersonName_out);
            trans.setSectionPersonName_number(sectionPersonName_number);
            trans.setRecordPersonName_out(recordPersonName_out);
            trans.setTransactionDate(LocalDate.now());

            registerTransactionService.saveTransaction(trans);
            model.addAttribute("successMessage", "✅ Record issued successfully!");
        } else {
            model.addAttribute("errorMessage", "❌ Master record not found!");
        }

        return "fragments/old_register/outward_entry :: searchrecord";
    }

    @GetMapping("/view_details")
    public String viewAllRecords(Model model) {
        model.addAttribute("records", oldRecordMasterService.getAllRecords());
        return "fragments/old_register/view_details :: tabler";
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
}