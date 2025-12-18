package com.recordroom.recordroom.closed.service;


import com.recordroom.recordroom.closed.entity.FileRecord;
import com.recordroom.recordroom.closed.entity.Section;
import com.recordroom.recordroom.closed.repo.RecordRepository;
import com.recordroom.recordroom.closed.repo.RecordTransactionRepository;
import com.recordroom.recordroom.closed.repo.SectionRepository;
import com.recordroom.recordroom.dto.DataTableRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;

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

    public List<String> getSectionCategories() {
        return sectionRepo.findDistinctCategories();
    }

    public Optional<Section> getSectionById(Integer id) {
        return sectionRepo.findById(id);
    }

    public Page<FileRecord> findByFilters(DataTableRequest request, String sectionCategory) {

        Specification<FileRecord> spec = (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();


            if (sectionCategory != null && !sectionCategory.isEmpty()) {
                predicates.add(cb.equal(
                        root.get("section").get("description"),
                        sectionCategory
                ));
            }


            String searchValue = request.getSearch().getValue();
            if (searchValue != null && !searchValue.isEmpty()) {
                try {

                    Long searchSerialNo = Long.parseLong(searchValue);
                    predicates.add(cb.equal(root.get("drSerialNo"), searchSerialNo));
                } catch (NumberFormatException e) {

                    predicates.add(cb.isFalse(cb.literal(true)));
                }
            }


            if (query.getResultType() != Long.class && query.getResultType() != long.class) {
                root.fetch("section", jakarta.persistence.criteria.JoinType.LEFT);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };


        Pageable pageable = PageRequest.of(
                request.getStart() / request.getLength(),
                request.getLength(),
                Sort.unsorted()
        );

        return recordRepo.findAll(spec, pageable);
    }

    public List<FileRecord> destructionReport() {
        return recordRepo.findRecordsExceedingYearLimit();
    }
}