package com.recordroom.recordroom.Service;


import com.recordroom.recordroom.Entity.RecordTransactionDetails;
import com.recordroom.recordroom.Repository.RecordTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordTransactionService {

    @Autowired
    private RecordTransactionRepository recordTransactionRepository;


    // Transaction
    public List<RecordTransactionDetails> getAllTransactions() { return recordTransactionRepository.findAll(); }
    public RecordTransactionDetails saveTransaction(RecordTransactionDetails t) { return recordTransactionRepository.save(t); }
    public void deleteTransaction(Integer id) { recordTransactionRepository.deleteById(id); }

    public Optional<RecordTransactionDetails> findBydrSerialNo(Long drSerialNo) {
        return recordTransactionRepository.findByDrSerialNo(drSerialNo);
    }

    public Optional<RecordTransactionDetails> findBydrSerialNoAndActive(Long drSerialNo) {
        return recordTransactionRepository.findActiveByDrSerialNo(drSerialNo);
    }

    public List<RecordTransactionDetails> getActiveRecords() {
        return recordTransactionRepository.findByActiveTrue();
    }


}
