package com.recordroom.recordroom.Service;


import com.recordroom.recordroom.Entity.FileRecord;
import com.recordroom.recordroom.Entity.RecordTransactionDetails;
import com.recordroom.recordroom.Entity.Section;
import com.recordroom.recordroom.Repository.RecordRepository;
import com.recordroom.recordroom.Repository.RecordTransactionRepository;
import com.recordroom.recordroom.Repository.SectionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecordService {

    private final RecordRepository recordRepo;
    private final SectionRepository sectionRepo;
    private final RecordTransactionRepository transRepo;

    public RecordService(RecordRepository recordRepo, SectionRepository sectionRepo, RecordTransactionRepository transRepo) {
        this.recordRepo = recordRepo;
        this.sectionRepo = sectionRepo;
        this.transRepo = transRepo;
    }


    public List<Section> getAllSections() {
        return sectionRepo.findAll();
    }

    public Section saveSection(Section s) {
        return sectionRepo.save(s);
    }

    public void deleteSection(Integer id) {
        sectionRepo.deleteById(id);
    }

    // Record
    public List<FileRecord> getAllRecords() {
        return recordRepo.findAll();
    }


    public Optional<FileRecord> saveRecord(FileRecord r) {
        if (recordRepo.findByDrSerialNo(r.getDrSerialNo()).isPresent()) {
            return Optional.empty();
        } else {
            return Optional.of(recordRepo.save(r));

        }


    }

    public Optional<FileRecord> findBydrSerialNo(Long drSerialNo) {
        return recordRepo.findByDrSerialNo(drSerialNo);
    }

    public Optional<FileRecord> findById(Integer id) {
        return recordRepo.findById(id);
    }

    public void deleteRecord(Integer id) {
        recordRepo.deleteById(id);
    }


}
