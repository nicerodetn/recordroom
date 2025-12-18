package com.recordroom.recordroom.oldregister.service;

import com.recordroom.recordroom.oldregister.entity.RegisterTransactionDetails;
import com.recordroom.recordroom.oldregister.repo.RegisterTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RegisterTransactionService {

    @Autowired
    private RegisterTransactionRepository repository;

    public RegisterTransactionDetails saveTransaction(RegisterTransactionDetails transaction) {
        return repository.save(transaction);
    }


    public Optional<RegisterTransactionDetails> findActiveBySerialNoAndYear(String serialNo, Long year) {
        return repository.findActiveBySerialNoAndYear(serialNo, year);
    }

    public Optional<RegisterTransactionDetails> findById(Integer id) {
        return repository.findById(id);
    }

    public List<RegisterTransactionDetails> getAllActiveTransactions() {
        return repository.findAllActiveTransactions();
    }

    public List<RegisterTransactionDetails> getAllCompletedTransactions() {
        return repository.findAllCompletedTransactions();
    }

    public long getActiveTransactionsCount() {
        return repository.countActiveTransactions();
    }

    public void deleteTransaction(Integer id) {
        repository.deleteById(id);
    }
}