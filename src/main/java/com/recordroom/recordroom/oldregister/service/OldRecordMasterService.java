package com.recordroom.recordroom.oldregister.service;

import com.recordroom.recordroom.oldregister.entity.OldRecordMaster;
import com.recordroom.recordroom.oldregister.repo.OldRecordMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OldRecordMasterService {

    @Autowired
    private OldRecordMasterRepository repository;

    public Optional<OldRecordMaster> saveRecord(OldRecordMaster record) {
        if (repository.findBySerialNoAndYear(record.getRecord_serial_no(), record.getYear()).isPresent()) {
            return Optional.empty();
        }
        return Optional.of(repository.save(record));
    }

    public Optional<OldRecordMaster> findBySerialNoAndYear(Long serialNo, Long year) {
        return repository.findBySerialNoAndYear(serialNo, year);
    }

    public Optional<OldRecordMaster> findById(Integer id) {
        return repository.findById(id);
    }

    public List<OldRecordMaster> getAllRecords() {
        return repository.findAll();
    }

    public long getTotalRecordsCount() {
        return repository.countTotalRecords();
    }

    public void deleteRecord(Integer id) {
        repository.deleteById(id);
    }
}