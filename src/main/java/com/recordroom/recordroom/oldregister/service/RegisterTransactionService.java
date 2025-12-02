package com.recordroom.recordroom.oldregister.service;

import com.recordroom.recordroom.oldregister.entity.OldRecordMaster;
import com.recordroom.recordroom.oldregister.entity.RegisterTransactionDetails;
import com.recordroom.recordroom.oldregister.repo.OldRecordMasterRepository;
import com.recordroom.recordroom.oldregister.repo.RegisterTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.recordroom.recordroom.dto.DataTableRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@Service
public class RegisterTransactionService {

    @Autowired
    private RegisterTransactionRepository transactionRepository;

    @Autowired
    private OldRecordMasterRepository masterRepository;


    public RegisterTransactionDetails saveTransaction(RegisterTransactionDetails transaction) {
        return transactionRepository.save(transaction);
    }

    public Optional<RegisterTransactionDetails> findActiveBySerialNoAndYear(Long serialNo, Long year) {
        return transactionRepository.findActiveBySerialNoAndYear(serialNo, year);
    }

    public Optional<RegisterTransactionDetails> findById(Integer id) {
        return transactionRepository.findById(id);
    }

    public List<RegisterTransactionDetails> getAllActiveTransactions() {
        return transactionRepository.findAllActiveTransactions();
    }

    public List<RegisterTransactionDetails> getAllCompletedTransactions() {
        return transactionRepository.findAllCompletedTransactions();
    }

    public long getActiveTransactionsCount() {
        return transactionRepository.countActiveTransactions();
    }

    public void deleteTransaction(Integer id) {
        transactionRepository.deleteById(id);
    }

    public List<String> getRecordTypes() {
        return masterRepository.findDistinctRecordTypes();
    }


// Finds ACTIVE transactions with search by record serial number

    public Page<RegisterTransactionDetails> findActiveByFilters(DataTableRequest request) {

        Specification<RegisterTransactionDetails> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            Join<RegisterTransactionDetails, OldRecordMaster> masterJoin = root.join("master");


            predicates.add(cb.isTrue(root.get("is_active_status")));


            String searchValue = request.getSearch().getValue();
            if (searchValue != null && !searchValue.isEmpty()) {
                try {
                    Long searchSerialNo = Long.parseLong(searchValue);
                    predicates.add(cb.equal(masterJoin.get("record_serial_no"), searchSerialNo));
                } catch (NumberFormatException e) {
                    predicates.add(cb.isFalse(cb.literal(true)));
                }
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

// Finds COMPLETED transactions with search by record serial number

    public Page<RegisterTransactionDetails> findCompletedByFilters(DataTableRequest request) {

        Specification<RegisterTransactionDetails> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            Join<RegisterTransactionDetails, OldRecordMaster> masterJoin = root.join("master");


            predicates.add(cb.isFalse(root.get("is_active_status")));


            predicates.add(cb.isNotNull(root.get("dateOfReturn")));


            String searchValue = request.getSearch().getValue();
            if (searchValue != null && !searchValue.isEmpty()) {
                try {
                    Long searchSerialNo = Long.parseLong(searchValue);
                    predicates.add(cb.equal(masterJoin.get("record_serial_no"), searchSerialNo));
                } catch (NumberFormatException e) {
                    predicates.add(cb.isFalse(cb.literal(true)));
                }
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