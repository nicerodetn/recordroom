package com.recordroom.recordroom.library.service;

import com.recordroom.recordroom.dto.DataTableRequest;
import com.recordroom.recordroom.library.entity.Library;
import com.recordroom.recordroom.library.entity.LibraryTransaction;
import com.recordroom.recordroom.library.repo.LibraryTransactionRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryTransactionService {

    @Autowired
    private LibraryTransactionRepository transactionRepository;

    public Optional<LibraryTransaction> findActiveTransactionByBookId(Integer bookId) {
        return transactionRepository.findByLibraryIdAndDateOfReturnIsNull(bookId);
    }

    public LibraryTransaction save(LibraryTransaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<LibraryTransaction> findOutstandingBooks() {
        return transactionRepository.findByDate_of_returnIsNull();
    }

    public void returnBook(Integer transactionId) {
        Optional<LibraryTransaction> transactionOpt = transactionRepository.findById(transactionId);
        if (transactionOpt.isPresent()) {
            LibraryTransaction transaction = transactionOpt.get();
            transaction.setDate_of_return(LocalDate.now());
            transactionRepository.save(transaction);
        }
    }

    public Optional<LibraryTransaction> findActiveTransactionByBookSerialNo(Integer serialNo) {
        return transactionRepository.findActiveTransactionByBookSerialNo(serialNo);
    }

    public Optional<LibraryTransaction> findById(Integer id) {
        return transactionRepository.findById(id);
    }


    public List<LibraryTransaction> findReturnedTransactions() {
        return transactionRepository.findReturnedTransactions();
    }

    public Page<LibraryTransaction> findByFilters(DataTableRequest request) {

        Specification<LibraryTransaction> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.isNull(root.get("date_of_return")));

            String searchValue = request.getSearch().getValue();
            if (searchValue != null && !searchValue.isEmpty()) {
                try {
                    Integer searchSerialNo = Integer.parseInt(searchValue);
                    Join<LibraryTransaction, Library> libraryJoin = root.join("library");
                    predicates.add(cb.equal(libraryJoin.get("book_serial_no"), searchSerialNo));
                } catch (NumberFormatException e) {
                    predicates.add(cb.isFalse(cb.literal(true)));
                }
            }

            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("library");
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(
                request.getStart() / request.getLength(),
                request.getLength(),
                Sort.unsorted()
        );
        return transactionRepository.findAll(spec, pageable);
    }



    public Page<LibraryTransaction> findHistoryByFilters(DataTableRequest request) {

        Specification<LibraryTransaction> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            predicates.add(cb.isNotNull(root.get("date_of_return")));


            String searchValue = request.getSearch().getValue();
            if (searchValue != null && !searchValue.isEmpty()) {
                try {
                    Integer searchSerialNo = Integer.parseInt(searchValue);
                    Join<LibraryTransaction, Library> libraryJoin = root.join("library");
                    predicates.add(cb.equal(libraryJoin.get("book_serial_no"), searchSerialNo));
                } catch (NumberFormatException e) {
                    predicates.add(cb.isFalse(cb.literal(true)));
                }
            }

            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("library");
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable = PageRequest.of(
                request.getStart() / request.getLength(),
                request.getLength(),
                Sort.unsorted()
        );
        return transactionRepository.findAll(spec, pageable);
    }
}