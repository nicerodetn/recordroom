package com.recordroom.recordroom.closed.service;


import com.recordroom.recordroom.closed.entity.RecordTransactionDetails;
import com.recordroom.recordroom.closed.repo.RecordTransactionRepository;
import com.recordroom.recordroom.dto.DataTableRequest;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecordTransactionService {

    @Autowired
    private RecordTransactionRepository recordTransactionRepository;



    public List<RecordTransactionDetails> getAllTransactions() { return recordTransactionRepository.findAll(); }
    public RecordTransactionDetails saveTransaction(RecordTransactionDetails t) { return recordTransactionRepository.save(t); }
    public void deleteTransaction(Integer id) { recordTransactionRepository.deleteById(id); }

    public Optional<RecordTransactionDetails> findBydrSerialNo(Long drSerialNo) {
        return recordTransactionRepository.findByDrSerialNo(drSerialNo);
    }

    public Optional<RecordTransactionDetails> findBydrSerialNoAndActiveAndYear(Long drSerialNo,Integer dr_year) {
        return recordTransactionRepository.findActiveByDrSerialNoAndYear(drSerialNo,dr_year);
    }


    public List<RecordTransactionDetails> getActiveRecords() {
        return recordTransactionRepository.findByActiveTrue();
    }

    public Page<RecordTransactionDetails> findActiveByFilters(DataTableRequest request) {

        Specification<RecordTransactionDetails> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();


            predicates.add(cb.isTrue(root.get("active")));


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

                var fileRecordFetch = root.fetch("fileRecord", jakarta.persistence.criteria.JoinType.LEFT);

                fileRecordFetch.fetch("section", jakarta.persistence.criteria.JoinType.LEFT);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };


        Pageable pageable = PageRequest.of(
                request.getStart() / request.getLength(),
                request.getLength(),
                Sort.unsorted()
        );

        return recordTransactionRepository.findAll(spec, pageable);
    }
}