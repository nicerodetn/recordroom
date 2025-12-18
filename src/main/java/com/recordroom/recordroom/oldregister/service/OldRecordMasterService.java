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
        return repository.findAll();
    }

    public long getTotalRecordsCount() {
        return repository.countTotalRecords();
    }

    public void deleteRecord(Integer id) {
        repository.deleteById(id);
    }
}