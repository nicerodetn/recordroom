package com.recordroom.recordroom.oldregister.service;

import com.recordroom.recordroom.oldregister.entity.OldRecordMaster;
import com.recordroom.recordroom.oldregister.repo.OldRecordMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class OldRecordMasterService {

    @Autowired
    private OldRecordMasterRepository repository;

    @Autowired
    private com.recordroom.recordroom.oldregister.repo.RegisterTransactionRepository transactionRepository;

    public Optional<OldRecordMaster> saveRecord(OldRecordMaster record) {


        if (record.getRecord_serial_no() == null || record.getRecord_serial_no().trim().isEmpty()) {
            record.setRecord_serial_no(generateUniqueSerialNumber());
        }
        else {

            if (repository.countByRecordSerialNo(record.getRecord_serial_no()) > 0) {
                return Optional.empty(); // Manual entry is a duplicate
            }
        }


        if (repository.findBySerialNoAndYear(record.getRecord_serial_no(), record.getYear()).isPresent()) {
            return Optional.empty();
        }

        return Optional.of(repository.save(record));
    }


    private String generateUniqueSerialNumber() {
        Random random = new Random();
        String serialNumber;
        do {

            int number = 10000 + random.nextInt(90000);
            serialNumber = String.valueOf(number);


        } while (repository.countByRecordSerialNo(serialNumber) > 0);

        return serialNumber;
    }



    public Optional<OldRecordMaster> findBySerialNoAndYear(String serialNo, Long year) {
        return repository.findBySerialNoAndYear(serialNo, year);
    }

    public Optional<OldRecordMaster> findById(Integer id) {
        return repository.findById(id);
    }

    public List<OldRecordMaster> getAllRecords() {
        return repository.findByDeletedFalseOrDeletedIsNull();
    }

    public List<OldRecordMaster> getDeletedRecords() {
        return repository.findByDeletedTrue();
    }

    public long getTotalRecordsCount() {
        return repository.countTotalRecords();
    }

    public Optional<OldRecordMaster> updateRecord(OldRecordMaster record) {
        Optional<OldRecordMaster> existing = repository.findBySerialNoAndYear(record.getRecord_serial_no(), record.getYear());
        if (existing.isPresent() && !existing.get().getId().equals(record.getId())) {
            return Optional.empty();
        }
        return Optional.of(repository.save(record));
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteRecord(Integer id) {
        transactionRepository.deleteByMasterId(id);
        repository.deleteById(id);
    }

    public void deleteRecordSoft(Integer id) {
        Optional<OldRecordMaster> recordOpt = repository.findById(id);
        if (recordOpt.isPresent()) {
            OldRecordMaster record = recordOpt.get();
            record.setDeleted(true);
            record.setDeletedDate(java.time.LocalDate.now());
            repository.save(record);
        }
    }

    public void restoreRecord(Integer id) {
        Optional<OldRecordMaster> recordOpt = repository.findById(id);
        if (recordOpt.isPresent()) {
            OldRecordMaster record = recordOpt.get();
            record.setDeleted(false);
            record.setDeletedDate(null);
            repository.save(record);
        }
    }

    @org.springframework.transaction.annotation.Transactional
    @org.springframework.scheduling.annotation.Scheduled(cron = "0 0 0 * * ?") // Daily at midnight
    public void cleanExpiredDeletedRecords() {
        java.time.LocalDate cutoff = java.time.LocalDate.now().minusDays(7);
        List<OldRecordMaster> expiredRecords = repository.findByDeletedTrueAndDeletedDateLessThanEqual(cutoff);
        for (OldRecordMaster r : expiredRecords) {
            deleteRecord(r.getId());
        }
    }
}