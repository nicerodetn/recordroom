package com.recordroom.recordroom.library.controller;


import com.recordroom.recordroom.dto.DataTableRequest;
import com.recordroom.recordroom.dto.DataTableResponse;
import com.recordroom.recordroom.library.entity.Library;
import com.recordroom.recordroom.library.entity.LibraryTransaction;
import com.recordroom.recordroom.library.service.LibraryService;
import com.recordroom.recordroom.library.service.LibraryTransactionService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/library")
public class LibraryController {
    private final LibraryService libraryService;
    private final LibraryTransactionService transactionService;

    public LibraryController(LibraryService libraryService, LibraryTransactionService transactionService) {
        this.libraryService = libraryService;
        this.transactionService = transactionService;
    }

    @GetMapping("/load_form")
    public String loadForm(Model model) {
        model.addAttribute("library", new Library()); // new empty form
        return "fragments/library/master_creation :: loadFormFragment"; // return only fragment
    }

    @PostMapping("/save")
    public String saveLibrary(@ModelAttribute("library") Library library, Model model) {
        Optional<Library> existingBook = libraryService.findByBookSerialNo(library.getBook_serial_no());

        if (existingBook.isPresent()) {
            model.addAttribute("errorMessage", "Error: A book with serial number '" + library.getBook_serial_no() + "' already exists.");
            model.addAttribute("library", library);
        } else {
            libraryService.saveLibrary(library);
            model.addAttribute("successMessage", "✅ Book saved successfully!");
            model.addAttribute("library", new Library());
        }
        return "fragments/library/master_creation :: loadFormFragment";
    }



    @GetMapping("/show_table")
    public String showLibraryTable(Model model) {
             return "fragments/library/view_master_report :: libraryTableFragment";
    }


    @PostMapping("/load_library_data")
    @ResponseBody
    public DataTableResponse<Library> loadLibraryData(@RequestBody DataTableRequest request) {

        Page<Library> page = libraryService.findByFilters(request);

        DataTableResponse<Library> response = new DataTableResponse<>();
        response.setDraw(request.getDraw());
        response.setRecordsTotal(page.getTotalElements());
        response.setRecordsFiltered(page.getTotalElements());
        response.setData(page.getContent());

        return response;
    }




    @GetMapping("/outward_entry")
    public String showOutwardEntryPage() {
        return "fragments/library/out_entry :: searchBookFragment";
    }

    @GetMapping("/searchForOutward")
    public String searchBookForOutward(@RequestParam Integer book_serial_no, Model model) {
        Optional<Library> bookOptional = libraryService.findByBookSerialNo(book_serial_no);

        if (bookOptional.isPresent()) {
            Library book = bookOptional.get();
            Optional<LibraryTransaction> activeTransaction = transactionService.findActiveTransactionByBookId(book.getId());

            if (activeTransaction.isPresent()) {
                model.addAttribute("errorMessage", "Error: Book '" + book.getName_of_book() + "' is already checked out.");
                return "fragments/library/out_entry :: searchBookFragment";
            } else {
                LibraryTransaction newTransaction = new LibraryTransaction();
                newTransaction.setLibrary(book);
                newTransaction.setDate_of_taking(LocalDate.now());

                model.addAttribute("book", book);
                model.addAttribute("libraryTransaction", newTransaction);
                return "fragments/library/out_entry :: bookOutwardFormFragment";
            }
        } else {
            model.addAttribute("errorMessage", "No book found with serial number: " + book_serial_no);
            return "fragments/library/out_entry :: searchBookFragment";
        }
    }

    @PostMapping("/saveOutward")
    public String saveOutwardEntry(@ModelAttribute LibraryTransaction libraryTransaction, Model model) {
        if (libraryTransaction.getLibrary() != null) {
            libraryTransaction.setBook_name(libraryTransaction.getLibrary().getName_of_book());
        }
        transactionService.save(libraryTransaction);
        model.addAttribute("successMessage", "✅ Book outward entry saved successfully!");
        return "fragments/library/out_entry :: searchBookFragment";
    }

    @GetMapping("/outstanding_report")
    public String showOutstandingReport(Model model) {
            return "fragments/library/out_entry_report :: outstandingBooksFragment";
    }


    @PostMapping("/load_outstanding_data")
    @ResponseBody
    public DataTableResponse<LibraryTransaction> loadOutstandingData(@RequestBody DataTableRequest request) {

        Page<LibraryTransaction> page = transactionService.findByFilters(request);

        DataTableResponse<LibraryTransaction> response = new DataTableResponse<>();
        response.setDraw(request.getDraw());
        response.setRecordsTotal(page.getTotalElements());
        response.setRecordsFiltered(page.getTotalElements());
        response.setData(page.getContent());

        return response;
    }

    @PostMapping("/returnBook/{id}")
    @ResponseBody
    public String returnBook(@PathVariable("id") Integer transactionId) {
        transactionService.returnBook(transactionId);
        return "";
    }

    @GetMapping("/inward_entry")
    public String showInwardEntryPage() {
        return "fragments/library/in_entry :: searchBookFragment";
    }

    @GetMapping("/searchForInward")
    public String searchBookForInward(@RequestParam Integer book_serial_no, Model model) {
        Optional<LibraryTransaction> transactionOptional = transactionService.findActiveTransactionByBookSerialNo(book_serial_no);
        if (transactionOptional.isPresent()) {
            LibraryTransaction transaction = transactionOptional.get();
            transaction.setDate_of_return(LocalDate.now());
            model.addAttribute("transaction", transaction);
            return "fragments/library/in_entry :: bookInwardFormFragment";
        } else {
            model.addAttribute("errorMessage", "No outstanding book with serial number " + book_serial_no + " found.");
            return "fragments/library/in_entry :: searchBookFragment";
        }
    }

    @PostMapping("/saveInward")
    public String saveInwardEntry(@ModelAttribute LibraryTransaction formTransaction, Model model) {
        Optional<LibraryTransaction> existingTransactionOpt = transactionService.findById(formTransaction.getId());
        if (existingTransactionOpt.isPresent()) {
            LibraryTransaction existingTransaction = existingTransactionOpt.get();
            existingTransaction.setDate_of_return(formTransaction.getDate_of_return());
            existingTransaction.setRemarks(formTransaction.getRemarks());
            transactionService.save(existingTransaction);
            model.addAttribute("successMessage", "✅ Book has been successfully returned!");
        } else {
            model.addAttribute("errorMessage", "Error: Could not find the transaction to update.");
        }
        return "fragments/library/in_entry :: searchBookFragment";
    }

    @GetMapping("/history")
    public String showHistory(Model model) {

        return "fragments/library/library_trans_history :: historyFragment";
    }


    @PostMapping("/load_history_data")
    @ResponseBody
    public DataTableResponse<LibraryTransaction> loadHistoryData(@RequestBody DataTableRequest request) {

        Page<LibraryTransaction> page = transactionService.findHistoryByFilters(request);

        DataTableResponse<LibraryTransaction> response = new DataTableResponse<>();
        response.setDraw(request.getDraw());
        response.setRecordsTotal(page.getTotalElements());
        response.setRecordsFiltered(page.getTotalElements());
        response.setData(page.getContent());

        return response;
    }
}