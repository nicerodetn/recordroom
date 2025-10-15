package com.recordroom.recordroom.library.controller;


import com.recordroom.recordroom.library.entity.Library;
import com.recordroom.recordroom.library.entity.LibraryTransaction;
import com.recordroom.recordroom.library.service.LibraryService;
import com.recordroom.recordroom.library.service.LibraryTransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

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
            //  If it exists, return an error message.
            model.addAttribute("errorMessage", "Error: A book with serial number '" + library.getBook_serial_no() + "' already exists.");
            // Return the form with the user's original input so they don't have to re-type everything.
            model.addAttribute("library", library);
        } else {
            // If it does not exist, save the new book and show a success message.
            libraryService.saveLibrary(library);
            model.addAttribute("successMessage", "✅ Book saved successfully!");
            // Return a new, empty library object to clear the form for the next entry.
            model.addAttribute("library", new Library());
        }

        return "fragments/library/master_creation :: loadFormFragment";
    }

    @GetMapping("/show_table")
    public String showLibraryTable(Model model) {
        List<Library> allLibraries = libraryService.findAllLibraries();
        model.addAttribute("libraries", allLibraries);
        return "fragments/library/view_master_report :: libraryTableFragment";
    }

    @GetMapping("/outward_entry")
    public String showOutwardEntryPage() {
        // This returns the fragment containing only the search bar.
        return "fragments/library/out_entry :: searchBookFragment";
    }

    @GetMapping("/searchForOutward")
    public String searchBookForOutward(@RequestParam Integer book_serial_no, Model model) {
        // Step A: Find the book using the serial number.
        Optional<Library> bookOptional = libraryService.findByBookSerialNo(book_serial_no);

        if (bookOptional.isPresent()) {
            Library book = bookOptional.get();

            // Step B: Check if this book is already checked out (has a transaction with no return date).

            Optional<LibraryTransaction> activeTransaction = transactionService.findActiveTransactionByBookId(book.getId());

            if (activeTransaction.isPresent()) {
                // CASE 1: Book is found but already issued. Show an error on the search page.
                model.addAttribute("errorMessage", "Error: Book '" + book.getName_of_book() + "' is already checked out.");
                return "fragments/library/out_entry :: searchBookFragment";
            } else {
                // CASE 2: Book is found and available. Prepare the outward entry form.
                LibraryTransaction newTransaction = new LibraryTransaction();
                newTransaction.setLibrary(book); // Link the found book to the transaction.
                newTransaction.setDate_of_taking(LocalDate.now()); // Set today's date by default.

                model.addAttribute("book", book); // The found book details to display.
                model.addAttribute("libraryTransaction", newTransaction); // The empty transaction object for the form.
                return "fragments/library/out_entry :: bookOutwardFormFragment";
            }
        } else {
            // CASE 3: Book with that serial number was not found. Show error on the search page.
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
        List<LibraryTransaction> outstandingTransactions = transactionService.findOutstandingBooks();
        model.addAttribute("outstandingBooks", outstandingTransactions);
        return "fragments/library/out_entry_report :: outstandingBooksFragment";
    }

    @PostMapping("/returnBook/{id}")
    @ResponseBody
    public String returnBook(@PathVariable("id") Integer transactionId) {
        transactionService.returnBook(transactionId);
        return ""; // Return empty response for HTMX to remove the row
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
            transaction.setDate_of_return(LocalDate.now()); // Default return date to today
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
        // 1. Get the list of returned transactions.
        List<LibraryTransaction> returnedTransactions = transactionService.findReturnedTransactions();
        // 2. Add the list to the model.
        model.addAttribute("returnedTransactions", returnedTransactions);
        // 3. Return the new history fragment.
        return "fragments/library/library_trans_history :: historyFragment";
    }

}
