package com.recordroom.recordroom.oldregister.service;

import com.recordroom.recordroom.oldregister.entity.OldRecordMaster;
import com.recordroom.recordroom.oldregister.repo.OldRecordMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.recordroom.recordroom.dto.DataTableRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;


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


// Gets the list of dynamic record types (RSR, SLR...) from the database.

    public List<String> getRecordTypes() {
        return repository.findDistinctRecordTypes();
    }



//  Finds records using server-side pagination, filtering.

    public Page<OldRecordMaster> findByFilters(DataTableRequest request, String recordType) {


        Specification<OldRecordMaster> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            if (recordType != null && !recordType.isEmpty()) {
                predicates.add(cb.equal(root.get("types_of_record"), recordType));
            }


            String searchValue = request.getSearch().getValue();
            if (searchValue != null && !searchValue.isEmpty()) {

                try {

                    Long searchSerialNo = Long.parseLong(searchValue);


                    predicates.add(cb.equal(root.get("record_serial_no"), searchSerialNo));

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
        return repository.findAll(spec, pageable);
    }


}