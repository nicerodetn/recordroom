package com.recordroom.recordroom.library.service;

import com.recordroom.recordroom.library.entity.LibraryTransaction;
import com.recordroom.recordroom.library.repo.LibraryTransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service

public class LibraryTransactionService {

    private final LibraryTransactionRepository transactionRepository;

    public LibraryTransactionService(LibraryTransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Optional<LibraryTransaction> findActiveTransactionByBookId(Integer bookId) {
        return transactionRepository.findByLibraryIdAndDateOfReturnIsNull(bookId);
    }

    public void save(LibraryTransaction transaction) {
        // The return date is null by default, indicating the book is out.
        transactionRepository.save(transaction);
    }

    public List<LibraryTransaction> findOutstandingBooks() {
        return transactionRepository.findByDate_of_returnIsNull();
    }

    public Optional<LibraryTransaction> findActiveTransactionByBookSerialNo(Integer serialNo) {
        return transactionRepository.findActiveTransactionByBookSerialNo(serialNo);
    }

    public void returnBook(Integer transactionId) {
        // Find the transaction by its unique ID.
        Optional<LibraryTransaction> transactionOptional = transactionRepository.findById(transactionId);
        if (transactionOptional.isPresent()) {
            LibraryTransaction transaction = transactionOptional.get();
            // Set the return date to the current date.
            transaction.setDate_of_return(LocalDate.now());
            // Save the updated transaction back to the database.
            transactionRepository.save(transaction);
        }
    }

    public List<LibraryTransaction> findReturnedTransactions() {
        return transactionRepository.findReturnedTransactions();
    }

    public Optional<LibraryTransaction> findById(Integer id) {
        return transactionRepository.findById(id);
    }

}
