package com.recordroom.recordroom.closed.service;


import com.recordroom.recordroom.closed.entity.FileRecord;
import com.recordroom.recordroom.closed.entity.Section;
import com.recordroom.recordroom.closed.repo.RecordRepository;
import com.recordroom.recordroom.closed.repo.RecordTransactionRepository;
import com.recordroom.recordroom.closed.repo.SectionRepository;
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
        if (recordRepo.findByDrSerialNoAndYear(r.getDrSerialNo(),r.getDr_year()).isPresent()) {
            return Optional.empty();
        } else {
            return Optional.of(recordRepo.save(r));
        }
    }

    public Optional<FileRecord> findBydrSerialNoAndYear(Long drSerialNo,Integer dr_year) {
        return recordRepo.findByDrSerialNoAndYear(drSerialNo,dr_year);
    }

    public Optional<FileRecord> findById(Integer id) {
        return recordRepo.findById(id);
    }

    public void deleteRecord(Integer id) {
        recordRepo.deleteById(id);
    }


}
